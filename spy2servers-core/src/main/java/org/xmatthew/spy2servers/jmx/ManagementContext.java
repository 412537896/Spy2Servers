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
package org.xmatthew.spy2servers.jmx;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

/**
 * @Notice this source code is mainly copied from apache activemq source
 * more info please visit http://activemq.apache.org
 * 
 * @author Matthew Xie
 * 
 */
public class ManagementContext {
    /**
     * LOGGERger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ManagementContext.class);

    private MBeanServer beanServer;

    private int connectorPort;

    private JMXConnectorServer connectorServer;

    private String connectorPath = "/jmxrmi";

    public static final String DEFAULT_DOMAIN = "org.xmatthew.spy2servers";

    private String jmxDomainName;

    private boolean locallyCreateMBeanServer = false;
    
    private AtomicBoolean started = new AtomicBoolean(false);

    private ObjectName namingServiceObjectName;

    /**
     * 
     */
    public ManagementContext() {
        this(1099);
    }

    
    
    /**
     * @param connectorPort
     */
    public ManagementContext(int connectorPort) {
        this(connectorPort, "/jmxrmi");
    }



    /**
     * @param connectorPort
     */
    public ManagementContext(int connectorPort, String connectorPath) {
        this(connectorPort, connectorPath, DEFAULT_DOMAIN);
    }
    
    public void start() throws IOException {
        // lets force the MBeanServer to be created if needed
        if (started.compareAndSet(false, true)) {
            getMBeanServer();
            if (connectorServer != null) {
                try {
                    getMBeanServer().invoke(namingServiceObjectName, "start", null, null);
                }
                catch (Throwable ignore) {
                }
                Thread t = new Thread("JMX connector") {
                    public void run() {
                        try {
                            JMXConnectorServer server = connectorServer;
                            if (started.get() && server != null) {
                                server.start();
                                LOGGER.info("JMX consoles can connect to " + server.getAddress());
                            }
                        }
                        catch (IOException e) {
                            LOGGER.warn("Failed to start jmx connector: " + e.getMessage());
                        }
                    }
                };
                t.setDaemon(true);
                t.start();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void stop() throws IOException {
        if (started.compareAndSet(true, false)) {
            JMXConnectorServer server = connectorServer;
            connectorServer = null;
            if (server != null) {
                try {
                    server.stop();
                }
                catch (IOException e) {
                    LOGGER.warn("Failed to stop jmx connector: " + e.getMessage());
                }
                try {
                    getMBeanServer().invoke(namingServiceObjectName, "stop", null, null);
                }
                catch (Throwable ignore) {
                }
            }
            if (locallyCreateMBeanServer && beanServer != null) {
                // check to see if the factory knows about this server
                List list = MBeanServerFactory.findMBeanServer(null);
                if (list != null && !list.isEmpty() && list.contains(beanServer)) {
                    MBeanServerFactory.releaseMBeanServer(beanServer);
                }
            }
        }
    }
    
    /**
     * @param connectorPort
     * @param connectorPath
     * @param jmxDomainName
     */
    public ManagementContext(int connectorPort, String connectorPath, String jmxDomainName) {
        super();
        this.connectorPort = connectorPort;
        this.connectorPath = connectorPath;
        this.jmxDomainName = jmxDomainName;
    }

    public MBeanServer getMBeanServer() {
        if (this.beanServer == null) {
            this.beanServer = findMBeanServer();
        }
        return beanServer;
    }

    @SuppressWarnings("unchecked")
    protected synchronized MBeanServer findMBeanServer() {
        MBeanServer result = null;
        // create the mbean server
        try {

            result = findTigerMBeanServer();

            if (result == null) {
                // lets piggy back on another MBeanServer -
                // we could be in an appserver!
                List list = MBeanServerFactory.findMBeanServer(null);
                if (list != null && list.size() > 0) {
                    result = (MBeanServer) list.get(0);
                }
            }
            if (result == null) {
                result = createMBeanServer();
            }

            if (result != null) {
                createConnector(result);
            }
        } catch (NoClassDefFoundError e) {
            LOGGER.error("Could not load MBeanServer", e);
        } catch (Throwable e) {
            // probably don't have access to system properties
            LOGGER.error("Failed to initialize MBeanServer", e);
        }
        return result;
    }

    /**
     * @return
     * @throws NullPointerException
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    protected MBeanServer createMBeanServer() throws MalformedObjectNameException, IOException {
        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer(jmxDomainName);
        locallyCreateMBeanServer = true;
        createConnector(mbeanServer);
        return mbeanServer;
    }

    /**
     * @param mbeanServer
     * @throws MalformedObjectNameException
     * @throws MalformedURLException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private void createConnector(MBeanServer mbeanServer) throws MalformedObjectNameException,
            MalformedURLException, IOException {
        // Create the NamingService, needed by JSR 160
        try {
            LocateRegistry.createRegistry(connectorPort);
            namingServiceObjectName = ObjectName.getInstance("naming:type=rmiregistry");
            // Do not use the createMBean as the mx4j jar may not be in the
            // same class loader than the server
            Class cl = Class.forName("mx4j.tools.naming.NamingService");
            mbeanServer.registerMBean(cl.newInstance(), namingServiceObjectName);
            // mbeanServer.createMBean("mx4j.tools.naming.NamingService",
            // namingServiceObjectName, null);
            // set the naming port
            Attribute attr = new Attribute("Port", new Integer(connectorPort));
            mbeanServer.setAttribute(namingServiceObjectName, attr);
        } catch (Throwable e) {
            LOGGER.debug("Failed to create local registry", e);
        }
        // Create the JMXConnectorServer
        String rmiServer = "";
        // This is handy to use if you have a firewall and need to
        // force JMX to use fixed ports.
        rmiServer = "localhost:" + connectorPort;

        String serviceURL = "service:jmx:rmi://" + rmiServer + "/jndi/rmi://localhost:" + connectorPort + connectorPath;
        JMXServiceURL url = new JMXServiceURL(serviceURL);
        connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbeanServer);
    }

    @SuppressWarnings("unchecked")
    public static MBeanServer findTigerMBeanServer() {
        String name = "java.lang.management.ManagementFactory";
        Class type = loadClass(name, ManagementContext.class.getClassLoader());
        if (type != null) {
            try {
                Method method = type.getMethod("getPlatformMBeanServer", new Class[0]);
                if (method != null) {
                    Object answer = method.invoke(null, new Object[0]);
                    if (answer instanceof MBeanServer) {
                        return (MBeanServer) answer;
                    } else {
                        LOGGER.warn("Could not cast: " + answer
                                + " into an MBeanServer. There must be some classloader strangeness in town");
                    }
                } else {
                    LOGGER.warn("Method getPlatformMBeanServer() does not appear visible on type: " + type.getName());
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to call getPlatformMBeanServer() due to: " + e, e);
            }
        } else {
            LOGGER.trace("Class not found: " + name + " so probably running on Java 1.4");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Class loadClass(String name, ClassLoader loader) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(name);
            } catch (ClassNotFoundException e1) {
                return null;
            }
        }
    }

    /**
     * @return the jmxDomainName
     */
    public String getJmxDomainName() {
        return jmxDomainName;
    }

    /**
     * @param jmxDomainName
     *            the jmxDomainName to set
     */
    public void setJmxDomainName(String jmxDomainName) {
        this.jmxDomainName = jmxDomainName;
    }

    /**
     * @return the connectorPath
     */
    public String getConnectorPath() {
        return connectorPath;
    }

    /**
     * @param connectorPath
     *            the connectorPath to set
     */
    public void setConnectorPath(String connectorPath) {
        this.connectorPath = connectorPath;
    }

    /**
     * @return the connectorPort
     */
    public int getConnectorPort() {
        return connectorPort;
    }

    /**
     * @param connectorPort
     *            the connectorPort to set
     */
    public void setConnectorPort(int connectorPort) {
        this.connectorPort = connectorPort;
    }
}
