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

import org.xmatthew.spy2servers.util.Assert;


/**
 * @author Matthew Xie
 *
 */
public class DataSourceSpy {
	
	public final static int NUM_ACTIVE_GOOD = 0;
	public final static int NUM_ACTIVE_WILL_OUT_OF_MAX = 1;
	public final static int NUM_ACTIVE_LOWER_TO_GOOD = 2;
	
	private long numActiveToAlert;
	private float numActivePercentToAlert;
	
	private int status;
	
	private String dataSourceName;
	
	/**
	 * @param numActive
	 * @return
	 */
	public int spyDataSource(long numActive, long maxActive) {
		if (numActiveToAlert == -1 && numActivePercentToAlert <= 0) {
			return NUM_ACTIVE_GOOD;
		}
		
		boolean isSpyed = false;
		if (numActivePercentToAlert > 0) {
			if (numActivePercentToAlert <= (numActive / Double.valueOf(maxActive) * 100)) {
				isSpyed = true;
			}
		} else {
			if (numActive >= this.numActiveToAlert) {
				isSpyed = true;
			}
		}
		
		if (isSpyed) {
			if (status != NUM_ACTIVE_WILL_OUT_OF_MAX) {
				status = NUM_ACTIVE_WILL_OUT_OF_MAX;
				return NUM_ACTIVE_WILL_OUT_OF_MAX;
			}
		} else {
			if (status == NUM_ACTIVE_WILL_OUT_OF_MAX) {
				status = NUM_ACTIVE_GOOD;
				return NUM_ACTIVE_LOWER_TO_GOOD;
			}
		}
		return NUM_ACTIVE_GOOD;
	}
	
	/**
	 * @return the numActivePercentToAlert
	 */
	public float getNumActivePercentToAlert() {
		return numActivePercentToAlert;
	}

	/**
	 * @param numActivePercentToAlert the numActivePercentToAlert to set
	 */
	public void setNumActivePercentToAlert(float numActivePercentToAlert) {
		this.numActivePercentToAlert = numActivePercentToAlert;
	}

	/**
	 * @return the numActiveToAlert
	 */
	public long getNumActiveToAlert() {
		return numActiveToAlert;
	}

	/**
	 * @param numActiveToAlert the numActiveToAlert to set
	 */
	public void setNumActiveToAlert(long numActiveToAlert) {
		this.numActiveToAlert = numActiveToAlert;
	}

	/**
	 * 
	 */
	public DataSourceSpy() {
		numActiveToAlert = -1;
		numActivePercentToAlert = 0;
	}

	/**
	 * @return the dataSourceName
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * @param dataSourceName the dataSourceName to set
	 */
	public void setDataSourceName(String dataSourceName) {
		Assert.notBlank(dataSourceName, "dataSourceName is blank");
		this.dataSourceName = dataSourceName;
	}
	
	public void init() {
		status = NUM_ACTIVE_GOOD;
	}

}
