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
package org.xmatthew.spy2servers.component.spy.jmx;

import java.util.Set;

import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.QueryExp;

import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 *
 */
public class NameFilterQueryExp implements QueryExp {
	
	private MBeanServer mbs;
	
	private Set<String> objectNamesPrefix;

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -6272170476479883430L;

	/**
	 * 
	 */
	public NameFilterQueryExp(Set<String> objectNamesPrefix) {
		this.objectNamesPrefix = objectNamesPrefix;
		
	}

	/* (non-Javadoc)
	 * @see javax.management.QueryExp#apply(javax.management.ObjectName)
	 */
	public boolean apply(ObjectName name) throws BadStringOperationException,
			BadBinaryOpValueExpException, BadAttributeValueExpException,
			InvalidApplicationException {
		if (CollectionUtils.isBlankCollection(objectNamesPrefix)) {
			return false;
		}
		for (String prefix : objectNamesPrefix) {
			if (name.toString().startsWith(prefix)
					|| prefix.equals(name.getKeyProperty(JmxSpySupportComponent.TYPE))) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.management.QueryExp#setMBeanServer(javax.management.MBeanServer)
	 */
	public void setMBeanServer(MBeanServer s) {
		this.mbs = s;
	}


	/**
	 * @return the mbs
	 */
	public MBeanServer getMbs() {
		return mbs;
	}

	/**
	 * @return the objectNamesPrefix
	 */
	public Set<String> getObjectNamesPrefix() {
		return objectNamesPrefix;
	}

	/**
	 * @param objectNamesPrefix the objectNamesPrefix to set
	 */
	public void setObjectNamesPrefix(Set<String> objectNamesPrefix) {
		this.objectNamesPrefix = objectNamesPrefix;
	}

}
