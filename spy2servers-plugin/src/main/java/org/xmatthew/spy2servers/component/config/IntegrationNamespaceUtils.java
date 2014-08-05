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

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmatthew.spy2servers.component.alert.EmailAccount;
import org.xmatthew.spy2servers.component.spy.jmx.DataSourceSpy;
import org.xmatthew.spy2servers.component.spy.jmx.DataSourcesSpy;
import org.xmatthew.spy2servers.component.spy.jmx.FileSpy;
import org.xmatthew.spy2servers.component.spy.jmx.MemorySpy;
import org.xmatthew.spy2servers.component.spy.jmx.WebModuleSpy;

/**
 * @author Matthew Xie
 *
 */
@SuppressWarnings("unchecked")
public final class IntegrationNamespaceUtils {
	
	private static final String QUEUE_ELEMENT = "queue";
	private static final String IP_ELEMENT = "ip";
	private static final String MODULE_ELEMENT = "module";

	/**
	 * 
	 */
	private IntegrationNamespaceUtils() {
		
	}

	public static MemorySpy parseMemorySpy(Element element) {
		MemorySpy memorySpy = new MemorySpy();
		if (element.hasAttribute("memoryUsedPercentToAlert")) {
			float memoryUsedPercentToAlert = Float.valueOf(element.getAttribute("memoryUsedPercentToAlert"));
			memorySpy.setMemoryUsedPercentToAlert(memoryUsedPercentToAlert);
		}
		if (element.hasAttribute("memoryUsedToAlert")) {
			long memoryUsedToAlert = Long.valueOf(element.getAttribute("memoryUsedToAlert"));
			memorySpy.setMemoryUsedToAlert(memoryUsedToAlert);
		}
		if (element.hasAttribute("alertAfterKeepTimeLive")) {
			long alertAfterKeepTimeLive = Long.valueOf(element.getAttribute("alertAfterKeepTimeLive"));
			memorySpy.setAlertAfterKeepTimeLive(alertAfterKeepTimeLive);
		}
		return memorySpy;
	}
	
	public static FileSpy parseFileSpy(Element element) {
		FileSpy fileSpy = new FileSpy();
		if (element.hasAttribute("filesOpenedPercentToAlert")) {
			float filesOpenedPercentToAlert = Float.valueOf(element.getAttribute("filesOpenedPercentToAlert"));
			fileSpy.setFilesOpenedPercentToAlert(filesOpenedPercentToAlert);
		}
		if (element.hasAttribute("filesOpenedToAlert")) {
			long filesOpenedToAlert = Long.valueOf(element.getAttribute("filesOpenedToAlert"));
			fileSpy.setFilesOpenedToAlert(filesOpenedToAlert);
		}
		if (element.hasAttribute("alertAfterKeepTimeLive")) {
			long alertAfterKeepTimeLive = Long.valueOf(element.getAttribute("alertAfterKeepTimeLive"));
			fileSpy.setAlertAfterKeepTimeLive(alertAfterKeepTimeLive);
		}
		return fileSpy;
	}

    public static Set parseDestinationNamesToWatch(Element element) {
		Set destinationNames = new HashSet();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (QUEUE_ELEMENT.equals(localName)) {
					String destinationName = ((Element) child).getAttribute("value");
					destinationNames.add(destinationName);
				}
			}
		}
		return destinationNames;
	}	
	
	public static Set parseLegalIps(Element element) {
		Set legalIps = new HashSet();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (IP_ELEMENT.equals(localName)) {
					String destinationName = ((Element) child).getAttribute("value");
					legalIps.add(destinationName);
				}
			}
		}
		return legalIps;
		
	}

	public static DataSourcesSpy parseDataSourcesSpy(Element element) {
		DataSourcesSpy dataSourcesSpy = new DataSourcesSpy();
		
		Set dataSourceSpys = new HashSet();
		NodeList childNodes = element.getChildNodes();
		DataSourceSpy dataSourceSpy;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (MODULE_ELEMENT.equals(localName)) {
					dataSourceSpy = parseDataSourceSpy((Element) child);
					dataSourceSpys.add(dataSourceSpy);
				}
			}
		}
		dataSourcesSpy.setDataSourceSpys(dataSourceSpys);
		return dataSourcesSpy;
	}

	private static DataSourceSpy parseDataSourceSpy(Element element) {
		DataSourceSpy dataSourceSpy = new DataSourceSpy();
		if (element.hasAttribute("dataSourceName")) {
			String dataSourceName = element.getAttribute("dataSourceName");
			dataSourceSpy.setDataSourceName(dataSourceName);
		}
		if (element.hasAttribute("numActiveToAlert")) {
			long numActiveToAlert = Long.valueOf(element.getAttribute("numActiveToAlert"));
			dataSourceSpy.setNumActiveToAlert(numActiveToAlert);
		}
		if (element.hasAttribute("numActivePercentToAlert")) {
			float numActivePercentToAlert = Float.valueOf(element.getAttribute("numActivePercentToAlert"));
			dataSourceSpy.setNumActivePercentToAlert(numActivePercentToAlert);
		}
		return dataSourceSpy;
	}

	public static WebModuleSpy parseWebModuleSpy(Element element) {
		WebModuleSpy webModuleSpy = new WebModuleSpy();
		
		Set webModules = new HashSet();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (MODULE_ELEMENT.equals(localName)) {
					String moduleName = ((Element) child).getAttribute("value");
					webModules.add(moduleName);
				}
			}
		}
		webModuleSpy.setWebModules(webModules);
		return webModuleSpy;
	}

	public static EmailAccount parseEmailAccount(Element element) {
		EmailAccount emailAccount = new EmailAccount();
		if (element.hasAttribute("server")) {
			String server = element.getAttribute("server");
			emailAccount.setServer(server);
		}
		
		if (element.hasAttribute("serverPort")) {
			int serverPort = Integer.valueOf(element.getAttribute("serverPort"));
			emailAccount.setServerPort(serverPort);
		}

		if (element.hasAttribute("loginName")) {
			String loginName = element.getAttribute("loginName");
			emailAccount.setLoginName(loginName);
		}
		if (element.hasAttribute("loginPwd")) {
			String loginPwd = element.getAttribute("loginPwd");
			emailAccount.setLoginPwd(loginPwd);
		}
		
		if (element.hasAttribute("sender")) {
			String sender = element.getAttribute("sender");
			emailAccount.setSender(sender);
		}

		if (element.hasAttribute("sendNick")) {
			String sendNick = element.getAttribute("sendNick");
			emailAccount.setSendNick(sendNick);
		}
		return emailAccount;
	}

	private static final String EMAIL_ELEMENT = "email";
	public static Set parseEmails(Element element) {
		Set emails = new HashSet();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (EMAIL_ELEMENT.equals(localName)) {
					String email = ((Element) child).getAttribute("value");
					emails.add(email);
				}
			}
		}
		return emails;
	}
}
