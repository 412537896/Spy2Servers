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

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.xmatthew.spy2servers.annotation.AlertComponent;
import org.xmatthew.spy2servers.annotation.MessageAlertChannelActiveAwareComponent;
import org.xmatthew.spy2servers.annotation.SpyComponent;

/**
 * @author XieMaLin
 *
 */
public class ComponentPostProcessor implements BeanPostProcessor {

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        detectSpyComponent(bean);
        detectAlertComponent(bean);
        detectChannelAwareComponent(bean);
        
        return bean;
    }

    private void detectSpyComponent(Object bean) throws RuntimeException {
        Class<?> beanClass = this.getBeanClass(bean);
        SpyComponent spyComponentAnnotation = AnnotationUtils.findAnnotation(beanClass, SpyComponent.class);
        if (spyComponentAnnotation != null) {
            if (bean instanceof org.xmatthew.spy2servers.core.SpyComponent) {
                org.xmatthew.spy2servers.core.SpyComponent spyComponent;
                spyComponent = (org.xmatthew.spy2servers.core.SpyComponent) bean;
                
                String name = spyComponentAnnotation.name();
                if (StringUtils.isNotBlank(name)) {
                    spyComponent.setName(name);
                }
            } else {
                throw new RuntimeException("@SpyComponent mark class must implement " +
                        "org.xmatthew.spy2servers.core.SpyComponent interface");
            }
        }
    }
    
    private void detectAlertComponent(Object bean) throws RuntimeException {
        Class<?> beanClass = this.getBeanClass(bean);
        AlertComponent alertComponentAnnotation = AnnotationUtils.findAnnotation(beanClass, AlertComponent.class);
        if (alertComponentAnnotation != null) {
            if (bean instanceof org.xmatthew.spy2servers.core.AlertComponent) {
                org.xmatthew.spy2servers.core.AlertComponent alertComponent;
                alertComponent = (org.xmatthew.spy2servers.core.AlertComponent) bean;
                
                String name = alertComponentAnnotation.name();
                if (StringUtils.isNotBlank(name)) {
                    alertComponent.setName(name);
                }
            } else {
                throw new RuntimeException("@AlertComponent mark class must implement " +
                        "org.xmatthew.spy2servers.core.AlertComponent interface");
            }
        }
    }
 
    private void detectChannelAwareComponent(Object bean) throws RuntimeException {
        Class<?> beanClass = this.getBeanClass(bean);
        MessageAlertChannelActiveAwareComponent ChannelAwareComponentAnnotation = AnnotationUtils.findAnnotation(beanClass, MessageAlertChannelActiveAwareComponent.class);
        if (ChannelAwareComponentAnnotation != null) {
            if (bean instanceof org.xmatthew.spy2servers.core.MessageAlertChannelActiveAwareComponent) {
                org.xmatthew.spy2servers.core.MessageAlertChannelActiveAwareComponent channelAwareComponent;
                channelAwareComponent = (org.xmatthew.spy2servers.core.MessageAlertChannelActiveAwareComponent) bean;
                
                String name = ChannelAwareComponentAnnotation.name();
                if (StringUtils.isNotBlank(name)) {
                    channelAwareComponent.setName(name);
                }
            } else {
                throw new RuntimeException("@MessageAlertChannelActiveAwareComponent mark class must implement " +
                        "org.xmatthew.spy2servers.core.MessageAlertChannelActiveAwareComponent interface");
            }
        }
    }
    
    private Class<?> getBeanClass(Object bean) {
        return AopUtils.getTargetClass(bean);
    }

}
