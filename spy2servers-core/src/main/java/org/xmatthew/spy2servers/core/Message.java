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
package org.xmatthew.spy2servers.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Matthew Xie
 *
 */
public class Message implements Comparable<Message> {
    
    public static final int LV_ERROR = 1;
    
    public static final int LV_WARN = 2;
    
    public static final int LV_FATAL = 3;
    
    public static final int LV_INFO = 4;
    
    private String id;
    
    private String body;
    
    private int level;
    
    @SuppressWarnings("unchecked")
    private Map properties;
    
    private Date createDate;
    
    private String description;
    
    private String type;
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Message other) {
        return new CompareToBuilder().append(createDate, other.createDate).toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("body", body).append("level", level).append(
                "properties", properties).append("createDate", createDate).append("description", description).append(
                "type", type).toString();
    }

    public static List<String> getKeys() {
        List<String> keys = new ArrayList<String>(7);
        keys.add("id");
        keys.add("body");
        keys.add("level");
        keys.add("createDate");
        keys.add("properties");
        keys.add("description");
        keys.add("type");
        return keys;
    }
    
    @SuppressWarnings("unchecked")
    public Map getFileds() {
        Map map = new HashMap(7);
        map.put("id", StringUtils.defaultString(getId()));
        map.put("body",  StringUtils.defaultString(getBody()));
        map.put("level", String.valueOf(getLevel()));
        if (getCreateDate() != null) {
            map.put("createDate", getCreateDate().toString());
        } else {
            map.put("createDate", "");
        }
        if (getProperties() != null) {
            map.put("properties", getProperties().toString());
        } else {
            map.put("properties", "{}");
        }
        map.put("description",  StringUtils.defaultString(getDescription()));
        map.put("type", getType());
        return map;
    }
    

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the properties
     */
    @SuppressWarnings("unchecked")
    public Map getProperties() {
        return properties;
    }

   
    public Object getProperty(String name) {
        if (properties == null) {
            return null;
        }
        return properties.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public void setProperty(String name, Object value) {
        if (properties == null) {
            properties = new LinkedHashMap();
        }
        properties.put(name, value);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public Message() {
        properties = new LinkedHashMap();
        createDate = new Date();
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Message clone() throws CloneNotSupportedException {
        Message message = new Message();
        message.setId(id);
        message.setBody(body);
        message.setCreateDate(createDate);
        message.setDescription(description);
        message.setLevel(level);
        message.setType(type);
        message.properties = new LinkedHashMap(properties);
        
        return message;
    }
    
    
}
