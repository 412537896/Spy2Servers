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
package org.xmatthew.spy2servers.component.web.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.xbean.JettyFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xml
 *
 */
public class ServerPaser extends AbstractSingleBeanDefinitionParser {
    
    private static final String CONNECTORS_ELEMENT = "connectors";
    private static final String HANDLERS_ELEMENT = "handlers";
    
    private static final String NIO_CONNECTOR_ELEMENT = "nioConnector";
    private static final String HANDLER_ELEMENT = "webAppContext";
    
    private static final String SERVLET_ELEMENT = "servlet";

	protected Class<?> getBeanClass(Element element) {
		return JettyFactoryBean.class;
	}

	protected boolean shouldGenerateId() {
		return false;
	}

	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {
        
        Connector[] connectors = null;
        Handler[] handlers = null;
        
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String localName = child.getLocalName();
                if (CONNECTORS_ELEMENT.equals(localName)) {
                    connectors = parseConnector((Element) child);
                } else if (HANDLERS_ELEMENT.equals(localName)) {
                    handlers = parseHandlers((Element) child);
                } 
            }
        } 
        
        if (connectors == null || connectors.length == 0) {
            throw new RuntimeException("sub element connectors must exsit and connector at least has one");
        }
        builder.addPropertyValue(CONNECTORS_ELEMENT, connectors);
        
        if (handlers != null) {
            builder.addPropertyValue(HANDLERS_ELEMENT, handlers);
        }

        builder.setInitMethodName("run");
	}

    private Handler paserWebViewServlet(Element element) {
        String servletUrl = element.getAttribute("path");
        if (StringUtils.isBlank(servletUrl)) {
            throw new RuntimeException("servlet path can not be blank");
        }
        String serlvet = element.getAttribute("servletClass");
        if (StringUtils.isBlank(serlvet)) {
            throw new RuntimeException("servlet name can not be blank");
        }
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(serlvet, servletUrl);
        
        return handler;
    }

    private Handler[] parseHandlers(Element element) {
        List<Handler> handlerList = new ArrayList<Handler>();
        
        NodeList childNodes = element.getChildNodes();
        Handler handler;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String localName = child.getLocalName();
                if (HANDLER_ELEMENT.equals(localName)) {
                    handler = parseHandler((Element) child);
                    if (handler != null) handlerList.add(handler);
                } else if (SERVLET_ELEMENT.equals(localName)) {
                    handlerList.add(paserWebViewServlet((Element) child));
                }
            }
        }
        if (!handlerList.isEmpty()) {
            return handlerList.toArray(new Handler[handlerList.size()]);
        }
        
        return null;
    }

    private static final String CONTEXT_PATH_PROPERTY = "contextPath";
    private static final String RESOURCE_BASE_PROPERTY = "resourceBase";
    private static final String lOGURL_ON_START_PROPERTY = "logUrlOnStart";
    
    private Handler parseHandler(Element element) {
        WebAppContext webAppContext = new WebAppContext();
        if (element.hasAttribute(CONTEXT_PATH_PROPERTY)) {
            webAppContext.setContextPath(element.getAttribute(CONTEXT_PATH_PROPERTY));
        } else {
            throw new RuntimeException("sub property " + CONTEXT_PATH_PROPERTY + " must exsit");
        }
        if (element.hasAttribute(RESOURCE_BASE_PROPERTY)) {
            webAppContext.setResourceBase(element.getAttribute(RESOURCE_BASE_PROPERTY));
        } else {
            throw new RuntimeException("sub property " + RESOURCE_BASE_PROPERTY + " must exsit");
        }
        if (element.hasAttribute(lOGURL_ON_START_PROPERTY)) {
            boolean logUrlOnStart = Boolean.valueOf(element.getAttribute(lOGURL_ON_START_PROPERTY));
            webAppContext.setLogUrlOnStart(logUrlOnStart);
        }
        return webAppContext;
    }

    private Connector parseNioConnector(Element element) {
        SelectChannelConnector nioConnector = new SelectChannelConnector();
        int port = Integer.valueOf(element.getAttribute("port"));
        nioConnector.setPort(port);
        return nioConnector;
    }

    private Connector[] parseConnector(Element element) {
        List<Connector> connectorList = new ArrayList<Connector>();
        
        NodeList childNodes = element.getChildNodes();
        Connector connector;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String localName = child.getLocalName();
                if (NIO_CONNECTOR_ELEMENT.equals(localName)) {
                    connector = parseNioConnector((Element) child);
                    if (connector != null) connectorList.add(connector);
                } 
            }
        }
        if (!connectorList.isEmpty()) {
            return connectorList.toArray(new Connector[connectorList.size()]);
        }
        
        return null;
    }
}
