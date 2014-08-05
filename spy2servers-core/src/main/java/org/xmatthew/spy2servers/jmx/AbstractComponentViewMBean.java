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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.xmatthew.spy2servers.core.Component;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.CollectionUtils;
import org.xmatthew.spy2servers.util.ComponentUtils;
import org.xmatthew.spy2servers.util.DateConstant;

/**
 * @author Matthew Xie
 *
 */
public abstract class AbstractComponentViewMBean implements ComponentViewMBean {

    abstract protected Component getComponent();
    
    public String getName() {
        return ComponentUtils.getComponentName(getComponent());
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.jmx.ComponentViewMBean#getStartupDate()
     */
    public String getStartupDate() {
        if (getComponent() == null || getComponent().getStartupDate() == null) {
            return "";
        }
        return DateFormatUtils.format(getComponent().getStartupDate(), DateConstant.FULL_DATE_PATTER);
    }

    public void start() {
        if (getComponent() != null) {
            testStatusIsStart();
            getComponent().startup();
        }
        
    }

    public void stop() {
        if (getComponent() != null) {
            testStatusIsStop();
            getComponent().stop();
        }
        
    }

    public int getStatus() {
        if (getComponent() != null) {
            return getComponent().getStatus();
        }
        return Component.ST_STOP;
    }

    public String getStatusName() {
        if (getComponent() != null) {
            return getComponent().getStatusName();
        }
        return Component.ST_STOP_NAME;
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.jmx.ComponentViewMBean#start()
     */
    protected void testStatusIsStart() {
        if (getStatus() == Component.ST_RUN) {
            throw new RuntimeException("component is in runing status");
        }
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.jmx.ComponentViewMBean#start()
     */
    protected void testStatusIsStop() {
        if (getStatus() == Component.ST_STOP) {
            throw new RuntimeException("component is in stop status");
        }
    }

	protected TabularData tabularDataWrapFromMessages(List<Message> messages) throws OpenDataException {
        if (CollectionUtils.isBlankCollection(messages)) {
            return null;
        }
        
        messages = Collections.synchronizedList(messages);
        
        String[] itemNames = Message.getKeys().toArray(new String[Message.getKeys().size()]);
        String[] itemDescriptions = itemNames;
        OpenType[] itemTypes = new OpenType[] {SimpleType.STRING, SimpleType.STRING,
                SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING};            
        CompositeType compositeType = new CompositeType("MessageList", 
                "list spyed messages", itemNames, itemDescriptions, itemTypes);        
        
        TabularType tt = new TabularType("MessageList",
                "MessageList", compositeType, itemNames);
        
        TabularDataSupport rc = new TabularDataSupport(tt);
        
        for (Message message : messages) {
            try {
                rc.put(new CompositeDataSupport(compositeType, message.getFileds()));
            } catch (Exception e) {
                throw new OpenDataException(e.getMessage());
            }
        }
        
        return rc;
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.jmx.ComponentViewMBean#viewComponentProperties()
     */
    @SuppressWarnings("unchecked")
    public String getComponentProperties() {
        if (getComponent() != null) {
            try {
                Component component = getComponent();
                Map properties = PropertyUtils.describe(component);
                if (properties == null) {
                    return null;
                }
                Iterator iter = properties.entrySet().iterator();
                List<String> keyValues = new ArrayList<String>(properties.size() - 1);
                Object key;
                String keyValue;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    key = entry.getKey();
                    if (((String) key).equals("class")) {
                        continue;
                    }
                    
                    keyValue = key.toString() + "=" + entry.getValue();
                    keyValues.add(keyValue);
                }
                return keyValues.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            } 
        }
        
        return null;
    }
    
    
}
