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
package org.xmatthew.spy2servers.core;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xmatthew.spy2servers.core.context.ComponentContext;
import org.xmatthew.spy2servers.core.context.ComponentContextUtils;
import org.xmatthew.spy2servers.rule.AlertRule;
import org.xmatthew.spy2servers.thread.ComponentInvokeTask;
import org.xmatthew.spy2servers.thread.DedicatedTaskRunner;
import org.xmatthew.spy2servers.thread.Task;
import org.xmatthew.spy2servers.thread.TaskRunner;
import org.xmatthew.spy2servers.util.Assert;
import org.xmatthew.spy2servers.util.CollectionUtils;
import org.xmatthew.spy2servers.util.ComponentUtils;

/**
 * <pre>
 *  core component. it privode follow functions:
 *   register/unregister all component.
 *   invoke ComponentContext object setting method to each component
 *   implement SpyCallback interface
 *   invoke MessageChannel object on message receiving
 * 
 * </pre>
 * 
 * @author Matthew Xie
 * 
 */
public class CoreComponent extends AbstractComponent implements
    ApplicationContextAware, InitializingBean, DisposableBean, SpyCallback {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(CoreComponent.class);

    private ApplicationContext context;

    private static final String NAME = "CoreComponent";

    private List<TaskRunner> tasks;

    private List<AlertComponent> alertComponents;
    
    private List<SpyComponent> spyComponents;
    
    private AlertRule alertRule;
    
    private List<MessageAlertChannelActiveAwareComponent> channelActiveWareComponents;
    
    /**
     * 
     */
    public CoreComponent() {
        super();
        tasks = new LinkedList<TaskRunner>();
        alertComponents = new LinkedList<AlertComponent>();
        spyComponents = new LinkedList<SpyComponent>();
        channelActiveWareComponents = new LinkedList<MessageAlertChannelActiveAwareComponent>();
    }

    /**
     * 
     */
    public void startup() {
        //ingore
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public String getName() {
        return NAME;
    }

    public void afterPropertiesSet() throws Exception {
        if (context == null) {
            LOGGER.error("context is null");
        }
        
        ComponentContext componentContext;
        componentContext = ComponentContextUtils.getComponentContext(context);
        
        //start CoreComponent
        fireComponent(this, componentContext);
        
        for (Component component : componentContext.getComponents()) {
            if (component instanceof CoreComponent) {
                continue;
            }
            fireComponent(component, componentContext);
        }
    }
    
    protected void fireComponent(Component component, ComponentContext componentContext) {
        component.setContext(componentContext);
        detectComponent(component);
        startComponent(component);
    }
    
    protected void detectComponent(Component component) {
        
        if (component instanceof AlertComponent) {
            alertComponents.add((AlertComponent) component);
        } 
        if (component instanceof SpyComponent) {
            SpyComponent spyComponent = (SpyComponent) component;
            spyComponents.add(spyComponent);
            spyComponent.setSpyCallback(this);
        } 
        if (component instanceof MessageAlertChannelActiveAwareComponent) {
            MessageAlertChannelActiveAwareComponent channelAwareComponent;
            channelAwareComponent = (MessageAlertChannelActiveAwareComponent) component;
            channelActiveWareComponents.add(channelAwareComponent);
        }
    }

    private void startComponent(Component component) {
        Assert.notNull(component, "component is null");
        String componentName = ComponentUtils.getComponentName(component);
        try {
            Task task = new ComponentInvokeTask(component);
            Date startupDate = new Date();
            component.setStartupDate(startupDate);
            TaskRunner taskRunner = new DedicatedTaskRunner(task, componentName);
            LOGGER.info("plug component " + componentName);
            tasks.add(taskRunner);
        } catch (Exception e) {
            LOGGER.error("plug component " + componentName + " failed");
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void stopComponent(Component component) {
        Assert.notNull(component, "component is null");
        try {
            LOGGER.info("unplug component " + ComponentUtils.getComponentName(component));
            component.stop();
        } catch (Exception e) {
            LOGGER.error("unplug component " + ComponentUtils.getComponentName(component) + " failed");
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void destroy() throws Exception {
        if (tasks != null) {
            for (TaskRunner task : tasks) {
                task.shutdownNoWait();
            }
        }

        if (getContext() == null) {
            return;
        }

        List<Component> components = getContext().getComponents();
        if (components != null) {
            for (Component component : components) {
                if (component instanceof CoreComponent) {
                    continue;
                }
                stopComponent(component);
            }
            //final stop CoreComponent
            stopComponent(this);
        }
    }

    public void stop() {
        //needn't to implement
    }

    
    protected void doAlert(SpyComponent spyComponent, Message message) {
        if (CollectionUtils.isBlankCollection(alertComponents)) {
            return;
        }
        
        MessageAlertChannel channel;
        for (AlertComponent alertComponent : alertComponents) {
            channel = new MessageAlertChannel();
            channel.setMessage(message);
            channel.setSpyComponent(spyComponent);
            channel.setAlertComponent(alertComponent);
            fireAlert(channel);
        }
    }
    
    protected void awareChannelComponent(MessageAlertChannel channel) {
        if (CollectionUtils.isBlankCollection(channelActiveWareComponents)) {
            return;
        }
        for (MessageAlertChannelActiveAwareComponent channelActiveWareComponent : channelActiveWareComponents) {
            channelActiveWareComponent.onMessageAlertChannelActive(channel);
        }
    }
    
    /**
     * fire alert action
     * @param channel
     */
    protected void fireAlert(MessageAlertChannel channel) {
        
        if (alertRule != null && !alertRule.isAlertAllow(channel)) {
            return;
        }
        try {
            channel.alert();
            if (channel.isLastAlertSuccess()) {
                awareChannelComponent(channel);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * @param alertRule the alertRule to set
     */
    public void setAlertRule(AlertRule alertRule) {
        this.alertRule = alertRule;
    }
    
    synchronized
    public void onSpy(SpyComponent spyComponent, Message message) {
        doAlert(spyComponent, message);
    }

	/**
	 * @return the alertRule
	 */
	public AlertRule getAlertRule() {
		return alertRule;
	}

	//visitor pattern
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#accept(org.xmatthew.spy2servers.core.IVisitor)
     */
    public void accept(IVisitor visitor) {
        //do nothing
    }
    


}
