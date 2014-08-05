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

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmatthew.spy2servers.core.CoreComponent;
import org.xmatthew.spy2servers.rule.AlertRule;
import org.xmatthew.spy2servers.rule.Channel;
import org.xmatthew.spy2servers.rule.SimpleAlertRule;

/**
 * @author Matthew Xie
 *
 */
public class CoreComponentParser extends ComponentParser {

	private static final String SIMPLE_ALERT_ELEMENT = "simple-alertRule";
	private static final String CHANNEL_ELEMENT = "channel";
	
	private static final String FROM_ELEMENT = "from";
	private static final String TO_ELEMENT = "to";

	protected Class<?> getBeanClass(Element element) {
		return CoreComponent.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		super.doParse(element, builder);
        
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (SIMPLE_ALERT_ELEMENT.equals(localName)) {
					builder.addPropertyValue("alertRule", parseSimpleAlertRule((Element) child));
				}
			}
		} 
	}
	
	@SuppressWarnings("unchecked")
    protected AlertRule parseSimpleAlertRule(Element element) {
		SimpleAlertRule alertRule = new SimpleAlertRule();
		alertRule.setChannles(parseChannels(element, alertRule));
		return alertRule;
	}
	
	@SuppressWarnings("unchecked")
    protected Set parseChannels(Element element, SimpleAlertRule simpleAlertRule) {
		Set channels = new HashSet();
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (CHANNEL_ELEMENT.equals(localName)) {
					channels.add(parseChannel((Element) child));
				}
			}
		}
		return channels;
	}
	
	@SuppressWarnings("unchecked")
    protected Channel parseChannel(Element element) {
		Channel channel = new Channel();

		Set from = new HashSet();
		Set to = new HashSet();
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (FROM_ELEMENT.equals(localName)) {
					String ref = ((Element) child).getAttribute("value");
					from.add(ref);
				} else if (TO_ELEMENT.equals(localName)) {
					String ref = ((Element) child).getAttribute("value");
					to.add(ref);
				}
			}
		}		
		channel.setFrom(from);
		channel.setTo(to);
		return channel;
	}
}
