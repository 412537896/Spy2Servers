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

import org.xmatthew.spy2servers.core.context.ComponentContext;

/**
 * Component interface.
 * 组件接口，提供组件的基本管理服务和状态监控。 所有组件必须要实现该接口。
 * 
 * @author Matthew Xie
 *
 */
public interface Component {
    
    /**
     * component run status
     * 
     */
    final static int ST_RUN = 1;
    
    /**
     * component run status name
     */
    final static String ST_RUN_NAME = "Active";
    
    /**
     * component stop status
     */
    final static int ST_STOP = 2;
    
    /**
     * component stop status name
     */
    final static String ST_STOP_NAME = "Decctive";
    
    /**
     * get component status
     * <p>
     * One of ST_RUN, ST_STOP. 
     * @return component status
     */
    int getStatus();
    
    /**
     * get component status
     * <p>
     * One of ST_RUN_NAME, ST_STOP_NAME. 
     * 
     * @return component status name
     */
    String getStatusName();
    
    /**
     * if component active return true
     * 
     * @return true if component is in status ST_RUN
     */
    boolean isActive();
    
	/**
	 * will be invoked after component plugs.
	 */
	void startup();

	/**
	 * will be invoked after component unplugs.
	 */
	void stop();

	/**
     * set component context to the component.
     * it will auto invoke by CoreComponent
     * 
	 * @param context set the component context
	 * 
	 */
	void setContext(ComponentContext context);

	/**
	 * @return get the component context
	 */
	ComponentContext getContext();
    
    /**
     * get component name
     * @return get component name
     */
    String getName();
    
    /**
     * get component strartup date
     * @return get component strartup date
     */
    Date getStartupDate();
    
    /**
     * set startup date to the component.
     * it will auto invoke by CoreComponent
     * 
     * @param date startup date
     */
    void setStartupDate(Date date);
    
    /**
     * @param name set the component name
     */ 
    void setName(String name);
    
    //visitor pattern
    /**
     * @param visitor {@link IVisitor} call back
     */
    void accept(IVisitor visitor);
}
