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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.xmatthew.spy2servers.util.Assert;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class DataSourcesSpy {
	

	private Set<DataSourceSpy> dataSourceSpys;
	
	private Map<String, DataSourceSpy> dataSourceSpysMap;

	/**
	 * @param dataSourceSpys the dataSourceSpys to set
	 */
	public void setDataSourceSpys(Set<DataSourceSpy> dataSourceSpys) {
		this.dataSourceSpys = dataSourceSpys;
		
		if (!CollectionUtils.isBlankCollection(dataSourceSpys)) {
			dataSourceSpysMap = new HashMap<String, DataSourceSpy>(dataSourceSpys.size());
			String dataSourceName;
			for (DataSourceSpy spy : this.dataSourceSpys) {
				dataSourceName = spy.getDataSourceName();
				Assert.notBlank(dataSourceName, "dataSourceName is blank");
				dataSourceSpysMap.put(dataSourceName.toUpperCase(), spy);
			}
		}
	}
	
	public int spyDataSource(String dataSourceName, long numActive, long maxActive) {
		if (CollectionUtils.isBlankMap(dataSourceSpysMap)) {
			return DataSourceSpy.NUM_ACTIVE_GOOD;
		}
		
		DataSourceSpy spy = dataSourceSpysMap.get(dataSourceName.toUpperCase());
		if (spy == null) {
			return DataSourceSpy.NUM_ACTIVE_GOOD;
		}
		
		return spy.spyDataSource(numActive, maxActive);
	}

	public void init() {
		if (CollectionUtils.isBlankMap(dataSourceSpysMap)) {
			return;
		}
		
		Iterator<Map.Entry<String, DataSourceSpy>> iter = dataSourceSpysMap.entrySet().iterator();
		Map.Entry<String, DataSourceSpy> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			entry.getValue().init();
		}
	}


}
