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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.xmatthew.spy2servers.core.context.ComponentContext;

/**
 * @author Matthew Xie
 *
 */
public abstract class AbstractComponent implements Component{
    
    private String name;
    
    protected int status;
    
    protected String statusName;
    
    private Date date;
    
    private ComponentContext componentContext;

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#isActive()
     */
    public boolean isActive() {
        return ST_RUN == status;
    }

    protected void setStatusRun() {
        status = ST_RUN;
        statusName = ST_RUN_NAME;
    }

    protected void setStatusStop() {
        status = ST_STOP;
        statusName = ST_STOP_NAME;
    }
    
    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }
    
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#getStartupDate()
     */
    public Date getStartupDate() {
        return date;
    }
    
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#setStartupDate(java.util.Date)
     */
    public void setStartupDate(Date date) {
        this.date = date;
    }
    
    /* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#getContext()
     */
    public ComponentContext getContext() {
        return componentContext;
    }



    /* (non-Javadoc)
	 * @see org.xmatthew.spy2servers.core.Component#getName()
	 */
	public String getName() {
        if (StringUtils.isBlank(name)) {
            name = this.getClass().getSimpleName();
        }
		return name;
	}

	/* (non-Javadoc)
     * @see org.xmatthew.spy2servers.core.Component#setContext(org.xmatthew.spy2servers.core.context.ComponentContext)
     */
    public void setContext(ComponentContext context) {
        componentContext = context;
    }
}
