/*
 * Copyright 2008- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xmatthew.spy2servers.component.spy.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.xmatthew.spy2servers.core.AbstractSpyComponent;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.Assert;
import org.xmatthew.spy2servers.util.CollectionUtils;
import org.xmatthew.spy2servers.util.NetConstant;

/**
 * @author Matthew Xie
 *
 */
public abstract class JmxSpySupportComponent extends AbstractSpyComponent {
	
	/**
	 * Logger for this class
	 */
	static final Logger LOGGER = Logger.getLogger(JmxSpySupportComponent.class);

	boolean isConnectionEstablished;

	MBeanServerConnectionFactoryBean bean;

	MBeanServerConnection mbsc;

	boolean runing;

	boolean connectionFailNotified = false;

	final static String JVM_CONNECT_OK = "JvmConnectOK";
	private NameFilterQueryExp nameFilterQueryExp;
	
	int port = 1099;
	private String host;
	private static final String JVM_CONNECT_FAILED = "JvmConnectFailed";
	private long detectInterval = 5000;
	public static final String SERVER_STATUS = "ServerStatus";
	
	public static final String TYPE = "type";
	public static final String NAME = "name";
	
	public static final String JVM_CONNECTION_SPY = "JvmConnectionStatus";
    
	public JmxSpySupportComponent() {
		super();
	}

	protected String getServerUrl() {
		Assert.notBlank(host, "host is null");
		return JMXConstant.JMX_SERVER_PREFIX + host + NetConstant.PORT_SPLIT
				+ port + JMXConstant.JMX_SERVER_SUFFIX;
	}

	public void startup() {
	
		bean = new MBeanServerConnectionFactoryBean();
		try {
			bean.setServiceUrl(getServerUrl());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	
		runing = true;
		setStatusRun();
		startJmxConnection();
	
		ObjectInstance objInstance;
		while (runing) {
			try {
				// begin to get MBeans
				Set mbeans = mbsc.queryMBeans(null, null);
				if (mbeans != null && mbeans.size() > 0) {
					Iterator iter = mbeans.iterator();
					while (iter.hasNext()) {
						objInstance = (ObjectInstance) iter.next();
						if (!getNameFilterQueryExp().apply(objInstance.getObjectName())) {
							continue;
						}
						inspectMBean(objInstance, mbsc);
					}
				}
				
				//every repeat will call back MBeanServerConnection
				mscOnInterval(mbsc);
				
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				if (e instanceof IOException) {
					try {
						bean.destroy();
					} catch (Exception e1) {
						LOGGER.error(e1.getMessage(), e1);
					} finally {
						bean = new MBeanServerConnectionFactoryBean();
						try {
							bean.setServiceUrl(getServerUrl());
						} catch (MalformedURLException e1) {
						}
						isConnectionEstablished = false;
						reStartJmxConnection();
					}
				}
			}
			
			try {
				Thread.sleep(detectInterval);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	abstract public void mscOnInterval(MBeanServerConnection mbsc) throws Exception;

	abstract protected Set<String> getObjectNamesPrefix();
	
	/**
	 * @return the nameFilterQueryExp
	 */
	public NameFilterQueryExp getNameFilterQueryExp() {
		if (nameFilterQueryExp == null) {
			nameFilterQueryExp = new NameFilterQueryExp(getObjectNamesPrefix());
		}
		
		return nameFilterQueryExp;
	}
	
	protected void reStartJmxConnection() {
		startJmxConnection();
	}

	protected void startJmxConnection() {
		while (!isConnectionEstablished && runing) {
            Map<String, Object> contents = new HashMap<String, Object>(1);
            contents.put("host", host);
            contents.put("port", String.valueOf(port));
			try {
				bean.afterPropertiesSet();
				isConnectionEstablished = true;
				if (connectionFailNotified) {
					onSpy(createMessage(SERVER_STATUS, JVM_CONNECT_OK, Message.LV_INFO,
                            JVM_CONNECTION_SPY, contents));
				}
				connectionFailNotified = false;
				mbsc = (MBeanServerConnection) bean.getObject();
			} catch (Exception e) {
				if (e instanceof IOException) {
					if (!connectionFailNotified) {
                        onSpy(createMessage(SERVER_STATUS, JVM_CONNECT_FAILED, Message.LV_INFO,
                                JVM_CONNECTION_SPY, contents));
						connectionFailNotified = true;
					}
				}
				isConnectionEstablished = false;
				LOGGER.error(e.getMessage(), e);
			}
			try {
				Thread.sleep(detectInterval);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public Map<String, Object> getAttributesAsMap(String mbeanName, MBeanServerConnection mbsc, Set<String> keys) throws Exception {
		if (CollectionUtils.isBlankCollection(keys)) {
			return null;
		}
		
		ObjectName mbean = new ObjectName(mbeanName);
		MBeanInfo info = mbsc.getMBeanInfo(mbean);
		if (info != null) {
			MBeanAttributeInfo[] attributes;
			attributes = info.getAttributes();
			if (attributes == null) {
				return null;
			}
	
			int size = attributes.length;
			if (size == 0) {
				return null;
			}
	
			Map<String, Object> beansMap = new HashMap<String, Object>(keys.size());
	
			String name;
			for (int i = 0; i < size; i++) {
				name = attributes[i].getName();
				if (keys.contains(name)) {
					try {
						beansMap.put(attributes[i].getName(), mbsc.getAttribute(mbean, attributes[i].getName()));
					} catch (Exception e) {
						//ignore it
					}
				}
			}
			return beansMap;
		}
		return null;
	}

	public Map<String, Object> getAttributesAsMap(String mbeanName, MBeanServerConnection mbsc) throws Exception {
		ObjectName mbean = new ObjectName(mbeanName);
        MBeanInfo info = null;
        try {
            info = mbsc.getMBeanInfo(mbean);
        } catch (Exception e) {
            //ignore the exception
        }
		
		if (info != null) {
			MBeanAttributeInfo[] attributes;
			attributes = info.getAttributes();
			if (attributes == null) {
				return null;
			}
	
			int size = attributes.length;
			if (size == 0) {
				return null;
			}
	
			Map<String, Object> beansMap = new HashMap<String, Object>(size);
			for (int i = 0; i < size; i++) {
				try {
					beansMap.put(attributes[i].getName(), mbsc.getAttribute(mbean, attributes[i].getName()));
				} catch (Exception e) {
					//ignore the exception
				}
			}
			return beansMap;
		}
		return null;
	}

	public void stop() {
		runing = false;
	    try {
	        if (bean != null) {
	            bean.destroy();
	        }
	    } catch (Exception e) {
	        LOGGER.error(e.getMessage(), e);
	    } finally {
	        isConnectionEstablished = false;
	        setStatusStop();
	    }
	
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the detectInterval
	 */
	public long getDetectInterval() {
		return detectInterval;
	}

	/**
	 * @param detectInterval the detectInterval to set
	 */
	public void setDetectInterval(long detectInterval) {
		this.detectInterval = detectInterval;
	}
	
	abstract protected void inspectMBean(ObjectInstance objectInstance,
			MBeanServerConnection mbsc) throws Exception;
	
	abstract protected Message createMessage(String body, String description, 
            int level, String type, Map<String, Object> beansMap);

}