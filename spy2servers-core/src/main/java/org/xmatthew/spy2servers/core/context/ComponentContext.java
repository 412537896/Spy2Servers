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
package org.xmatthew.spy2servers.core.context;

import java.util.List;

import org.xmatthew.spy2servers.core.Component;

/**
 * Component context
 * 
 * @author Matthew Xie
 *
 */
public class ComponentContext {
    
    List<Component> components;
    
	/**
     * get all plugged components list
     * 
     * @see {@link #setComponents(List)}
	 * @return components
	 */
	public List<Component> getComponents() {
		return components;
	}
    
    /**
     * set all plugged components list.
     * it will invoke auto by CoreComponent
     * 
     * @param components all plugged components list
     */
    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
