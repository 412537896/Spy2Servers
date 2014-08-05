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

import java.util.List;

import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;

import org.xmatthew.spy2servers.core.Component;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.core.SpyComponent;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class SpyComponentView extends AbstractComponentViewMBean implements SpyComponentViewMBean {
    
    private SpyComponent spyComponent;

    /**
     * constructor
     */
    public SpyComponentView(SpyComponent spyComponent) {
        this.spyComponent = spyComponent;
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.jmx.SpyComponentViewMBean#browseMessageAsTable()
     */
    public TabularData browseMessageAsTable() throws OpenDataException {
        List<Message> messages = spyComponent.getMessages();
        return tabularDataWrapFromMessages(messages);
    }

 
    public int getMessageCount() {
        List<Message> messages = spyComponent.getMessages();
        if (CollectionUtils.isBlankCollection(messages)) {
            return 0;
        }
        return messages.size();
    }

    @Override
    protected Component getComponent() {
        return spyComponent;
    }

    public void purge() {
        if (spyComponent != null && spyComponent.getMessages() != null) {
            spyComponent.getMessages().clear();
        }
        
    }

}
