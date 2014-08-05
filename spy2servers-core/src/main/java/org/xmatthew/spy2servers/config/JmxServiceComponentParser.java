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
package org.xmatthew.spy2servers.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.xmatthew.spy2servers.jmx.JmxServiceComponent;

/**
 * @author Matthew Xie
 *
 */
public class JmxServiceComponentParser extends ComponentParser {

	protected Class<?> getBeanClass(Element element) {
		return JmxServiceComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);
        
        if (element.hasAttribute("port")) {
            int port = Integer.valueOf(element.getAttribute("port"));
            builder.addPropertyValue("port", port);
        }
	}
}
