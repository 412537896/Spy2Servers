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
package org.xmatthew.spy2servers.thread;

import org.apache.log4j.Logger;

import org.springframework.util.Assert;

import org.xmatthew.spy2servers.core.Component;

/**
 * @author Matthew Xie
 *
 */
public class ComponentInvokeTask implements Task {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentInvokeTask.class);
    
    private Component component;
    
    /**
     * @param component
     */
    public ComponentInvokeTask(Component component) {
        super();
        this.component = component;
    }



    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.thread.Task#execute()
     */
    public boolean executed() {
        Assert.notNull(component, "component is null");
        try {
            component.startup();
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

}
