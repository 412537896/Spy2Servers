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
package org.xmatthew.spy2servers.component.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmatthew.spy2servers.component.spy.jmx.DataSourcesSpy;
import org.xmatthew.spy2servers.component.spy.jmx.TomcatJmxSpyComponent;
import org.xmatthew.spy2servers.component.spy.jmx.WebModuleSpy;

/**
 * @author Matthew Xie
 *
 */
public class TomcatJmxSpyComponentParser extends SunJVMJmxSpyComponentParser {
	
	private static final String DATASOURCES_SPY_PROPERTY = "dataSourcesSpy";
	private static final String WEB_MODULES_PROPERTY = "webModuleSpy";

	protected Class<?> getBeanClass(Element element) {
		return TomcatJmxSpyComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);

		DataSourcesSpy dataSourcesSpy = null;
		WebModuleSpy webModuleSpy = null;
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (DATASOURCES_SPY_PROPERTY.equals(localName)) {
					dataSourcesSpy = IntegrationNamespaceUtils.parseDataSourcesSpy((Element) child);
				} else if (WEB_MODULES_PROPERTY.equals(localName)) {
					webModuleSpy = IntegrationNamespaceUtils.parseWebModuleSpy((Element) child);
				} 
			}
		} 
		
		if (dataSourcesSpy != null) {
			builder.addPropertyValue(DATASOURCES_SPY_PROPERTY, dataSourcesSpy);
		}
		if (webModuleSpy != null) {
			builder.addPropertyValue(WEB_MODULES_PROPERTY, webModuleSpy);
		}		
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
	}
}
