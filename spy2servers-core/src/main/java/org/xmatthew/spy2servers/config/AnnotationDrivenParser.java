/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the JLERP License, Version 1.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.xmatthew.spy2servers.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author XieMaLin
 *
 */
public class AnnotationDrivenParser implements BeanDefinitionParser{

    private void createComponentPostProcessor(ParserContext parserContext) {
        BeanDefinition bd = new RootBeanDefinition(ComponentPostProcessor.class);
//        bd.getConstructorArgumentValues().addGenericArgumentValue(
//                new RuntimeBeanReference(MessageBusParser.MESSAGE_BUS_BEAN_NAME));
        BeanComponentDefinition bcd = new BeanComponentDefinition(
                bd, "simplebean");
        parserContext.registerBeanComponent(bcd);
        
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        this.createComponentPostProcessor(parserContext);
        return null;
    }
}
