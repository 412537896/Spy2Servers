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
import org.xmatthew.spy2servers.component.spy.jmx.FileSpy;
import org.xmatthew.spy2servers.component.spy.jmx.MemorySpy;
import org.xmatthew.spy2servers.component.spy.jmx.SunJVMJmxSpyComponent;
import org.xmatthew.spy2servers.config.ComponentParser;

/**
 * @author Matthew Xie
 *
 */
public class SunJVMJmxSpyComponentParser extends
		ComponentParser implements SunJVMBeanElementDefination {

	protected Class<?> getBeanClass(Element element) {
		return SunJVMJmxSpyComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);
        
		String host = element.getAttribute("host");
		builder.addPropertyValue("host", host);
		
		if (element.hasAttribute("port")) {
			String port = element.getAttribute("port");
			builder.addPropertyValue("port", port);
		}
		if (element.hasAttribute("detectInterval")) {
			String detectInterval = element.getAttribute("detectInterval");
			builder.addPropertyValue("detectInterval", detectInterval);
		}		
		if (element.hasAttribute("queueSuspendNotifyTime")) {
			String queueSuspendNotifyTime = element.getAttribute("queueSuspendNotifyTime");
			builder.addPropertyValue("queueSuspendNotifyTime", queueSuspendNotifyTime);
		}	
		
		MemorySpy heapMemorySpy = null;
		MemorySpy noneHeapMemorySpy = null;
		FileSpy fileSpy = null;
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (HEAP_MEMORY_ELEMENT.equals(localName)) {
					heapMemorySpy = IntegrationNamespaceUtils.parseMemorySpy((Element) child);
				} else if (NONE_HEAP_MEMORY_ELEMENT.equals(localName)) {
					noneHeapMemorySpy = IntegrationNamespaceUtils.parseMemorySpy((Element) child);
				} else if (FILE_SPY_ELEMENT.equals(localName)) {
					fileSpy = IntegrationNamespaceUtils.parseFileSpy((Element) child);
				} 
			}
		} 
		
		if (heapMemorySpy != null) {
			builder.addPropertyValue("heapMemorySpy", heapMemorySpy);
		}
		if (noneHeapMemorySpy != null) {
			builder.addPropertyValue("noneHeapMemorySpy", noneHeapMemorySpy);
		}
		if (fileSpy != null) {
			builder.addPropertyValue(FILE_SPY_ELEMENT, fileSpy);
		}
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
	}
}
