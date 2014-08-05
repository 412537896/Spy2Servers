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

import org.springframework.util.Assert;

/**
 * @author Matthew Xie
 *
 */
public class MessageAlertChannel {

    private SpyComponent spyComponent;
    
    private AlertComponent alertComponent;
    
    private Message message;
    
    private boolean isLastAlertSuccess;

    /**
     * @return the alertComponent
     */
    public AlertComponent getAlertComponent() {
        return alertComponent;
    }

    /**
     * @param alertComponent the alertComponent to set
     */
    public void setAlertComponent(AlertComponent alertComponent) {
        this.alertComponent = alertComponent;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return the spyComponent
     */
    public SpyComponent getSpyComponent() {
        return spyComponent;
    }

    /**
     * @param spyComponent the spyComponent to set
     */
    public void setSpyComponent(SpyComponent spyComponent) {
        this.spyComponent = spyComponent;
    }
    
    /**
     * do alert action
     */
    public void alert() {
        Assert.notNull(message, "message is null");
        Assert.notNull(alertComponent, "alert component is null");
        
        if (!alertComponent.isActive()) {
            isLastAlertSuccess = false;
            return;
        }
        try {
            //mark message
            Message msg = message.clone();
            msg.setProperty("from", spyComponent.getName());
            msg.setProperty("to", alertComponent.getName());
            alertComponent.alert(msg);
            isLastAlertSuccess = true;
        } catch (Exception e) {
            isLastAlertSuccess = false;
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @return the isLastAlertSuccess
     */
    public boolean isLastAlertSuccess() {
        return isLastAlertSuccess;
    }
    
}
