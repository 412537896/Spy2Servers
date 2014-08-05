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

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmatthew.spy2servers.component.spy.jmx.ActiveMQJmxSpyComponent;

/**
 * @author Matthew Xie
 *
 */
public class ActiveMQJmxSpyComponentParser extends SunJVMJmxSpyComponentParser {
	
	private static final String DESTINATION_NAMES_TO_WATCH_PROPERTY = "destinationNamesToWatch";
	private static final String LLEGAL_IP_PROPERTY = "llegalIps";
	
	protected Class<?> getBeanClass(Element element) {
		return ActiveMQJmxSpyComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);
        
		Set destinationNames = null;
		Set llegalIps = null;
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (DESTINATION_NAMES_TO_WATCH_PROPERTY.equals(localName)) {
					destinationNames = IntegrationNamespaceUtils.parseDestinationNamesToWatch((Element) child);
				} else if (LLEGAL_IP_PROPERTY.equals(localName)) {
					llegalIps = IntegrationNamespaceUtils.parseLegalIps((Element) child);
				}
			}
		} 

		if (destinationNames != null) {
			builder.addPropertyValue(DESTINATION_NAMES_TO_WATCH_PROPERTY, destinationNames);
		}
		if (llegalIps != null) {
			builder.addPropertyValue(LLEGAL_IP_PROPERTY, llegalIps);
		}
        
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
	}
}
