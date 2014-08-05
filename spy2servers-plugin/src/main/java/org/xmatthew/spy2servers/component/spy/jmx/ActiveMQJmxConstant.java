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
package org.xmatthew.spy2servers.component.spy.jmx;

/**
 * @author Matthew Xie
 *
 */
public final class ActiveMQJmxConstant {

    /**
     * 
     */
    private ActiveMQJmxConstant() {
        // TODO Auto-generated constructor stub
    }

    public final static String QUEUE_SIZE_NAME = "QueueSize";
    public final static String CONSUMER_COUNT_NAME = "ConsumerCount";
    public final static String NAME = "Name";
    
    public final static String ACTIVEMQ_QUEUE_MBEAN_CLASS = "org.apache.activemq.broker.jmx.QueueView";
    public final static String ACTIVEMQ_TOPIC_MBEAN_CLASS = "org.apache.activemq.broker.jmx.TopicView";
    
    public final static String ACTIVEMQ_CONNECTION_MBEAN_CLASS = "org.apache.activemq.broker.jmx.ConnectionView";
    
}
