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
 * Channel active ware component interface. extends from Component
 * 消息调度监控组件接口，提供消息调度监控的管理。 所有消息调度监控组必须要实现该接口
 * 
 * @author Matthew Xie
 *
 */
public interface MessageAlertChannelActiveAwareComponent extends Component {

    /**
     * will be invoked after alert channel actived. 
     * 
     * @param channel alert active channel
     */
    void onMessageAlertChannelActive(MessageAlertChannel channel);
    
    /**
      * get List of alerted channels.
     * if has no message return null;
     * 
     * @return alerted channels
     */
    List<MessageAlertChannel> getChannels();
}
