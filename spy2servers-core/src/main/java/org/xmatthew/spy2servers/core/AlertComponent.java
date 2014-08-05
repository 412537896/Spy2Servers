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

import java.util.List;

/**
 * Alert component interface. extends from Component
 * 报警组件接口，提供报警消息的管理和报警回调接口机制。 所有报警组件必须要实现该接口
 * 
 * @author Matthew Xie
 */
public interface AlertComponent extends Component {
	/**
     * will be invoked after alert message arrived. 
     * 
	 * @param message alert Message
	 */
	public void alert(Message message);
    
    /**
     * get List of alerted messages.
     * if has no message return null;
     * 
     * @return alerted messages
     */
    public List<Message> getMessages();
}
