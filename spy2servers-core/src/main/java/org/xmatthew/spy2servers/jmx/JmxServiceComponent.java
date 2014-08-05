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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;

import org.apache.commons.collections.KeyValue;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.MBeanExporter;
import org.xmatthew.spy2servers.core.AbstractComponent;
import org.xmatthew.spy2servers.core.Component;
import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.MessageAlertChannel;
import org.xmatthew.spy2servers.core.MessageAlertChannelActiveAwareComponent;
import org.xmatthew.spy2servers.util.Assert;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class JmxServiceComponent extends AbstractComponent implements MessageAlertChannelActiveAwareComponent  {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(JmxServiceComponent.class);
    
    private MBeanExporter mbeanExporter;
    
    private int port = 1099;
    
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#destory()
     */
    public void stop() {
        try {
            if (mbeanExporter != null) {
                mbeanExporter.destroy();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        status = ST_STOP;
        statusName = ST_STOP_NAME;

    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#startup()
     */
    @SuppressWarnings("unchecked")
    public void startup() {
        status = ST_RUN;
        statusName = ST_RUN_NAME;
        
        if (getContext() == null) {
            return;
        }
        
        //export spyComponentMBean
        List<Component> components = getContext().getComponents();
        if (CollectionUtils.isBlankCollection(components)) {
            return;
        }
        
        mbeanExporter = new MBeanExporter();
        
        Map mbeans = new HashMap(components.size());
        String mbeanComponentName;
        IVisitor visitor = new JmxViewVisitor();
        for (Component component : components) {
            
            component.accept(visitor);
            KeyValue result = visitor.getVisitingResult();
            
            if (result != null) {
                mbeanComponentName = getUniMBeanName(mbeans, (String) result.getKey());
                mbeans.put(mbeanComponentName, result.getValue());
            }
            
        }
        if (mbeans.size() > 0) { // start jmx export server
            ManagementContext managementContext = null;
            if (port > 0) {
                managementContext = new ManagementContext(port);
                MBeanServer beanServer = managementContext.getMBeanServer();
                mbeanExporter.setServer(beanServer);
            }
            mbeanExporter.setBeans(mbeans);
            try {
                if (managementContext != null) managementContext.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mbeanExporter.afterPropertiesSet();
        }
    }
    
    @SuppressWarnings("unchecked")
    protected String getUniMBeanName(Map mbeans, String name) {
        String uniName = name;
        int order = 1;
        while (mbeans.containsKey(uniName)) {
            uniName = name + order;
        }
        return uniName;
    }

    public void onMessageAlertChannelActive(MessageAlertChannel channel) {
        if (channels == null) {
            channels = new LinkedList<MessageAlertChannel>();
        }
        Assert.notNull(channel, "messageAlertChannel is null");
        channels.add(channel);
        
        LOGGER.debug("message channel active.");
    }

    private List<MessageAlertChannel> channels;

    public List<MessageAlertChannel> getChannels() {
        return channels;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}
