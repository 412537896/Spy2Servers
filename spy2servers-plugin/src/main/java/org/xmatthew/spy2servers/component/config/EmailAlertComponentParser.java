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
import org.xmatthew.spy2servers.component.alert.EmailAccount;
import org.xmatthew.spy2servers.component.alert.EmailAlertComponent;
import org.xmatthew.spy2servers.config.ComponentParser;

/**
 * @author Matthew Xie
 *
 */
public class EmailAlertComponentParser extends ComponentParser {
	
	private static final String EMAIL_ACCOUNT_PROPERTY = "emailAccount";
	private static final String EMAILS_PROPERTY = "emails";

	protected Class<?> getBeanClass(Element element) {
		return EmailAlertComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		super.doParse(element, builder);
        
		EmailAccount emailAccount = null;
		Set emails = null;
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (EMAIL_ACCOUNT_PROPERTY.equals(localName)) {
					emailAccount = IntegrationNamespaceUtils.parseEmailAccount((Element) child);
				} else if (EMAILS_PROPERTY.equals(localName)) {
					emails = IntegrationNamespaceUtils.parseEmails((Element) child);
				}  
			}
		} 
		
		if (emails != null) {
			builder.addPropertyValue(EMAILS_PROPERTY, emails);
		}
		if (emailAccount == null) {
			throw new RuntimeException("emailAccount must set in beans defination <emailAlert />");
		}
		builder.addPropertyValue(EMAIL_ACCOUNT_PROPERTY, emailAccount);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
	}
}
