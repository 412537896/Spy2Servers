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
package org.xmatthew.spy2servers.rule;

import org.xmatthew.spy2servers.core.MessageAlertChannel;

/**
 * message channle active alert rule interface.
 * 消息调度规则
 * 
 * @author Matthew Xie
 *
 */
public interface AlertRule {

    /**
     * if allow the active alert channel return true.
     * 
     * @param channel MessageAlertChannel channel
     * @return if true message alert channel will be active
     */
    boolean isAlertAllow(MessageAlertChannel channel);
}
