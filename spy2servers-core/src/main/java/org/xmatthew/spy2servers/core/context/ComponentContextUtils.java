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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.xmatthew.spy2servers.core.Component;

/**
 * @author Matthew Xie
 *
 */
public final class ComponentContextUtils {

    private ComponentContextUtils() {
        
    }
    
    @SuppressWarnings("unchecked")
    public static ComponentContext getComponentContext(ApplicationContext context) {
        Map beansMap = context.getBeansOfType(Component.class);
        if (beansMap != null) {

            List<Component> components = new ArrayList<Component>(beansMap.size());
            components.addAll(beansMap.values());
            ComponentContext componentContext = new ComponentContext();
            componentContext.setComponents(components);
            return componentContext;
        }
        return null;
    }
}
