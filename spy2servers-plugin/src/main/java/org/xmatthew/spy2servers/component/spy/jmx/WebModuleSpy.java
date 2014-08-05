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
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class WebModuleSpy {
	
	private Set<String> webModules;
	private Map<String, Integer> webModulesStatus;
	
	public final static int STATUS_INIT = 0;
	public final static int STATUS_RUN = 1;
	public final static int STATUS_STOP = 2;

	/**
	 * 
	 */
	public WebModuleSpy() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param webModules the webModules to set
	 */
	public void setWebModules(Set<String> webModules) {
		this.webModules = webModules;
		init();
	}

	/**
	 * @param webModule
	 * @param isStatusRun
	 * @return true if need to warn
	 */
	public boolean spyWebModule(String webModule, boolean isStatusRun) {
		if (StringUtils.isBlank(webModule)) {
			return false;
		}
		String webModuleUC = webModule.toUpperCase();
		if (webModulesStatus.containsKey(webModuleUC)) {
			int status = webModulesStatus.get(webModuleUC);
			if (isStatusRun) {
				webModulesStatus.put(webModuleUC, STATUS_RUN);
				if (status == STATUS_STOP) {return true;}
			} else {
				webModulesStatus.put(webModuleUC, STATUS_STOP);
				if (status != STATUS_STOP) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the webModules
	 */
	public Set<String> getWebModules() {
		return webModules;
	}

	public void init() {
		if (CollectionUtils.isBlankCollection(this.webModules)) {
			return;
		}
		webModulesStatus = new HashMap<String, Integer>(this.webModules.size());
		for (String webModule : webModules) {
			webModulesStatus.put(webModule.toUpperCase(), STATUS_INIT);
		}
		
	}
}
