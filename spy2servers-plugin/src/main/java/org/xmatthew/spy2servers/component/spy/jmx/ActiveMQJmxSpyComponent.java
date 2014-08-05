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

import static org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxConstant.ACTIVEMQ_CONNECTION_MBEAN_CLASS;
import static org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxConstant.ACTIVEMQ_QUEUE_MBEAN_CLASS;
import static org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxConstant.CONSUMER_COUNT_NAME;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;

import org.apache.commons.lang.StringUtils;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.CollectionUtils;
import org.xmatthew.spy2servers.util.StringConstant;

/**
 * @author Matthew Xie
 * 
 */
public class ActiveMQJmxSpyComponent extends SunJVMJmxSpyComponent {
    /**
     * Logger for this class
     */
//    private static final Logger LOGGER = Logger.getLogger(ActiveMQJmxSpyComponent.class);

    public Map<String, DestinationStatus> destinationStatusMap;
    
    private Set<String> destinationNamesToWatch;
    
    private Set<String> llegalIps;
    
    private static final String QUEUE_CONSUMER_ACTIVE = "QueueConsumerActive";
    private static final String QUEUE_CONSUMER_SUSPEND = "QueueConsumerSuspend";
    private static final String NAME = "Name";
    
    public final static String REMOTEADDRESS = "RemoteAddress";
    public final static String ADDRESS_PREFIX = "/";
    public final static String COLON_SIGN = ":";
    
    public final static String ILLEGAL_ADDRESS = "IllegalIPAddress";
    
    public final static String CONNECTION_STATUS = "ConnectionStatus";

    //3 minutes
    private int queueSuspendNotifyTime = 3*60*1000;
    
    private Set<String> invalidIps;
    
    public static final String QUEUE_INSPECT_TYPE = "QueueInspect";
    public static final String IP_INSPECT_TYPE = "IpAddressInspect";
    

    /**
     * 
     */
    public ActiveMQJmxSpyComponent() {
        destinationStatusMap = new HashMap<String, DestinationStatus>();
    }

    
    /* (non-Javadoc)
	 * @see org.xmatthew.spy2servers.component.spy.jmx.SunJVMJmxSpyComponent#inspectMBean(javax.management.ObjectInstance, javax.management.MBeanServerConnection)
	 */
	@Override
	protected void inspectMBean(ObjectInstance objectInstance, MBeanServerConnection mbsc) throws Exception {
		super.inspectMBean(objectInstance, mbsc);
		
		String className = objectInstance.getClassName();
		
		if (ACTIVEMQ_QUEUE_MBEAN_CLASS.equals(className)) {
			inspectQueueMBean(objectInstance, mbsc);
		}
        
		if (ACTIVEMQ_CONNECTION_MBEAN_CLASS.equals(className)) {
			inspectConnectionMBean(objectInstance, mbsc);
		}
        
	}

	/**
	 * @param objectInstance
	 * @param mbsc
	 * @throws Exception
	 */
	private void inspectConnectionMBean(ObjectInstance objectInstance, MBeanServerConnection mbsc) throws Exception {
		Map<String, Object> beansMap = getAttributesAsMap(objectInstance.getObjectName().toString(), mbsc);
        if (beansMap != null) {
            if (CollectionUtils.isBlankCollection(llegalIps)) {
            	return;
            }
            
            String address = String.valueOf(beansMap.get(REMOTEADDRESS));
            if (StringUtils.isBlank(address)) {
            	return;
            }
            address = StringUtils.removeStart(address, ADDRESS_PREFIX);
            address = StringUtils.substringBefore(address, COLON_SIGN);
            
            if (!llegalIps.contains(address)) {
            	if (invalidIps == null) {
            		invalidIps = new LinkedHashSet<String>();
            	}
            	if (!invalidIps.contains(address)) {
            		onSpy(createMessage(address, ILLEGAL_ADDRESS, Message.LV_ERROR, IP_INSPECT_TYPE, beansMap));
            		invalidIps.add(address);
            	}
            }
            
        }
	}

	/**
	 * @param objectInstance
	 * @param mbsc
	 * @throws Exception
	 */
	private void inspectQueueMBean(ObjectInstance objectInstance, MBeanServerConnection mbsc) throws Exception {
		Map<String, Object> beansMap = getAttributesAsMap(objectInstance.getObjectName().toString(), mbsc);
        if (beansMap != null) {
            String queueName = String.valueOf(beansMap.get(NAME));
            if (destinationNamesToWatch != null && !destinationNamesToWatch.contains(queueName)) {
                return;
            }
            
            DestinationStatus destinationStatus = null;
            if (StringConstant.ZORE.equals(String.valueOf(beansMap.get(CONSUMER_COUNT_NAME)))) {
                
                boolean isNeedSpy = true;
                if (!destinationStatusMap.containsKey(queueName)) {
                    destinationStatus = new DestinationStatus(beansMap, new Date());
                    destinationStatusMap.put(queueName, destinationStatus);
                } else {
                    destinationStatus = destinationStatusMap.get(queueName);
                    if (!destinationStatus.isConsumerZero()) {
                        destinationStatus.setStatusStartDate(new Date());
                    }
                }
                
                if (destinationStatus.isNotified() || 
                        destinationStatus.getStatusKeepTime() < queueSuspendNotifyTime) {
                    isNeedSpy = false;
                }
                
                if (isNeedSpy && destinationStatus != null) {
                    //DestinationStatus status = new DestinationStatus(beansMap, new Date());
                    String description = QUEUE_CONSUMER_SUSPEND;
                    destinationStatus.setBeansMap(beansMap);
                    String body = beansMap.get(NAME).toString();
                    onSpy(createMessage(body, description, 
                            QUEUE_INSPECT_TYPE, Message.LV_ERROR, destinationStatus));
                    destinationStatus.doNotifyStatus();
                    //destinationStatus.setStatusStartDate(new Date());
                    
                    //destinationStatusMap.put(queueName, destinationStatus);
                }
            } else {
                if (destinationStatusMap.containsKey(queueName)) {
                    destinationStatus = destinationStatusMap.get(queueName);
                    if (destinationStatus.isConsumerZero()) {
                        if (destinationStatus.isNotified()) {
                            String description = QUEUE_CONSUMER_ACTIVE;
                            String body = beansMap.get(NAME).toString();
                            destinationStatus.setBeansMap(beansMap);
                            onSpy(createMessage(body, description, 
                                    QUEUE_INSPECT_TYPE, Message.LV_INFO, destinationStatus));
                            destinationStatus.setStatusStartDate(new Date());
                        }
                        destinationStatus.cancelNotifyStatus();
                    }
                } else {
                    destinationStatus = new DestinationStatus(beansMap, new Date());
                }
            }
            
            if (destinationStatus != null) {
                destinationStatus.setBeansMap(beansMap);
                destinationStatusMap.put(queueName, destinationStatus);
            }
        }
	}
    
    @SuppressWarnings("unchecked")
    private Message createMessage(String body, String description, 
            String type, int level, DestinationStatus status) {
        Message message = new Message();
        message.setBody(body);
        message.setLevel(level);
        message.setId(UUID.randomUUID().toString());
        message.setType(type);
        message.setDescription(description);
        message.getProperties().putAll(status.getBeansMap());
        message.setProperty("statusKeepTime", status.getStatusKeepTime());
        return message;
    }

      /**
     * @param destinationNamesToWatch the destinationNamesToWatch to set
     */
    public void setDestinationNamesToWatch(Set<String> destinationNamesToWatch) {
        this.destinationNamesToWatch = destinationNamesToWatch;
    }

    /**
     * @param queueSuspendNotifyTime the queueSuspendNotifyTime to set
     */
    public void setQueueSuspendNotifyTime(int queueSuspendNotifyTime) {
        this.queueSuspendNotifyTime = queueSuspendNotifyTime;
    }

    public final static String QUEUEVIEW_PREFIX = "org.apache.activemq:BrokerName=localhost,Type=Queue";
    public final static String CONNECTIONVIEW_PREFIX = "org.apache.activemq:BrokerName=localhost,Type=Connection,ConnectorName=openwire,ViewType=address";
	/* (non-Javadoc)
	 * @see org.xmatthew.spy2servers.component.spy.jmx.SunJVMJmxSpyComponent#getObjectNamesPrefix()
	 */
	@Override
	protected Set<String> getObjectNamesPrefix() {
		Set<String> newObjectNamesPrefix;
		Set<String> objectNamesPrefix = super.getObjectNamesPrefix();
		if (objectNamesPrefix == null) {
			newObjectNamesPrefix = new HashSet<String>(2);
		} else {
			newObjectNamesPrefix = new HashSet<String>(objectNamesPrefix.size() + 2);
			newObjectNamesPrefix.addAll(objectNamesPrefix);
		}
		newObjectNamesPrefix.add(QUEUEVIEW_PREFIX);
		newObjectNamesPrefix.add(CONNECTIONVIEW_PREFIX);
		return newObjectNamesPrefix;
	}


	/**
	 * @param llegalIps the llegalIps to set
	 */
	public void setLlegalIps(Set<String> llegalIps) {
		this.llegalIps = llegalIps;
	}
    
    

}
