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

import java.util.Date;
import java.util.Map;

import org.xmatthew.spy2servers.util.CollectionUtils;
import org.xmatthew.spy2servers.util.StringConstant;

import static org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxConstant.CONSUMER_COUNT_NAME;
import static org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxConstant.QUEUE_SIZE_NAME;

/**
 * @author Matthew Xie
 *
 */
public class DestinationStatus {
    
    public static final int CONSUMER_COUNT_ZERO = 1;
    
    public static final int QUEUE_SIZE_NOTZERO = 2;
    
    private Date statusStartDate;
    
    private Map<String, Object> beansMap;
    
    private boolean notified = false;

    /**
     * @param statusStartDate
     * @param beansMap
     */
    public DestinationStatus(Map<String, Object> beansMap, Date statusStartDate) {
        super();
        this.statusStartDate = statusStartDate;
        this.beansMap = beansMap;
    }

    /**
     * @param beansMap the beansMap to set
     */
    public void setBeansMap(Map<String, Object> beansMap) {
        this.beansMap = beansMap;
    }
    
    public boolean isConsumerZero() {
        if (CollectionUtils.isBlankMap(beansMap)) {
            return false;
        }
        if (StringConstant.ZORE.equals(String.valueOf(beansMap.get(CONSUMER_COUNT_NAME)))) {
            return true;
        }
        return false;
    }

    public boolean isQueueSizeNotZero() {
        if (CollectionUtils.isBlankMap(beansMap)) {
            return false;
        }
        if (!StringConstant.ZORE.equals(String.valueOf(beansMap.get(QUEUE_SIZE_NAME)))) {
            return true;
        }
        return false;
    }
    
    /**
     * @param beansMap
     */
    public DestinationStatus(Map<String, Object> beansMap) {
        super();
        this.beansMap = beansMap;
    }
    
    public long getStatusKeepTime() {
        if (statusStartDate == null) {
            return 0;
        }
        return System.currentTimeMillis() - statusStartDate.getTime();
    }

    /**
     * @return the statusStartDate
     */
    public Date getStatusStartDate() {
        return statusStartDate;
    }

    /**
     * @param statusStartDate the statusStartDate to set
     */
    public void setStatusStartDate(Date statusStartDate) {
        this.statusStartDate = statusStartDate;
    }

    /**
     * @return the beansMap
     */
    public Map<String, Object> getBeansMap() {
        return beansMap;
    }

    /**
     * @return the notified
     */
    public boolean isNotified() {
        return notified;
    }

    /**
     * @param notified the notified to set
     */
    public void setNotified(boolean notified) {
        this.notified = notified;
    }
    
    public void doNotifyStatus() {
        this.notified = true;
    }
    
    public void cancelNotifyStatus() {
        this.notified = false;
    }
}
