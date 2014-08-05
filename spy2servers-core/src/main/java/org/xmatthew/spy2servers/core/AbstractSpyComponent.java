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

import java.util.LinkedList;
import java.util.List;

import org.xmatthew.spy2servers.util.Assert;

/**
 * @author Matthew Xie
 *
 */
public abstract class AbstractSpyComponent extends AbstractComponent implements SpyComponent {
    
    private List<Message> messages;
    
    private SpyCallback spyCallback;
    
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.SpyComponent#getMessages()
     */
    public List<Message> getMessages() {
        return messages;
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.SpyComponent#onSpy(org.xmatthew.spy2servers.core.Message)
     */
    public void onSpy(Message message) {
        Assert.notNull(message, "message is null");
        Assert.notNull(spyCallback, "SpyCallback is null");
        
        if (messages == null) {
            messages = new LinkedList<Message>();
        }
        messages.add(message);
        spyCallback.onSpy(this, message);
    }

    public void setSpyCallback(SpyCallback spyCallback) {
        this.spyCallback = spyCallback;
    }
}
