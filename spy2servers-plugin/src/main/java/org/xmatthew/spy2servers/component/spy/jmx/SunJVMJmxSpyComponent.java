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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.openmbean.CompositeData;

import org.xmatthew.spy2servers.core.IVisitor;
import org.xmatthew.spy2servers.core.Message;
import org.xmatthew.spy2servers.util.CollectionUtils;

/**
 * @author Matthew Xie
 * 
 */
public class SunJVMJmxSpyComponent extends JmxSpySupportComponent {


	private MemorySpy heapMemorySpy;
	private MemorySpy noneHeapMemorySpy;

	private FileSpy fileSpy;

	public final static String MEMORYIMPL_CLASS = "sun.management.MemoryImpl";
	public final static String HEAP_MEORY_USAGE_NAME = "HeapMemoryUsage";
	public final static String NON_HEAP_MEMORY_USAGE_NAME = "NonHeapMemoryUsage";
	
	public final static String UNIT_OS_CLASS="com.sun.management.UnixOperatingSystem";
	public final static String MAX_FILE_COUNT = "MaxFileDescriptorCount";
	public final static String OPENfILE_COUNT = "OpenFileDescriptorCount";
	
	public final static String MEMORY_USED = "used";
	public final static String MAX_MEMORY = "max";
	public final static String MEMORY_INIT = "init";
	public final static String MAMORY_COMMITTED = "committed";
	
	public final static String MEMORY_STATUS = "MemoryStatus";
	public final static String FILES_STATUS = "FilesOpenStatus";
	public final static String WEBMODULE_STATUS = "WebModuleStatus";

	/**
	 * 
	 */
	public SunJVMJmxSpyComponent() {
		// TODO Auto-generated constructor stub
	}

	protected void memoryMBeanSpy(Map<String, Object> beansMap) {
		if (CollectionUtils.isBlankMap(beansMap)) { return; }
		
		if (heapMemorySpy != null) { 
			spyMemory(beansMap, HEAP_MEORY_USAGE_NAME, heapMemorySpy);
		}
		
		if (noneHeapMemorySpy != null) {
			spyMemory(beansMap, NON_HEAP_MEMORY_USAGE_NAME, noneHeapMemorySpy);
		}
	}
	
	private void spyMemory(Map<String, Object> beansMap, String memoryTypeName, MemorySpy memorySpy) {
		Object obj = beansMap.get(HEAP_MEORY_USAGE_NAME);
		if (obj == null) { return; }
		
		if (obj instanceof CompositeData) {
			CompositeData data = (CompositeData) obj;
			
			long heapMemoryUsed = (Long) data.get(MEMORY_USED);
			long maxMemory = (Long) data.get(MAX_MEMORY);
			
			int spyedResult = memorySpy.spyMemory(heapMemoryUsed, maxMemory);
			if (MemorySpy.MEMORY_LOWER_TO_GOOD == spyedResult || 
					MemorySpy.MEMORY_WILL_OUT_OF_MAX == spyedResult) {
				Map<String, Object> memoryValues = new HashMap<String, Object>(4);
				memoryValues.put(MEMORY_USED, heapMemoryUsed);
				memoryValues.put(MEMORY_INIT, data.get(MEMORY_INIT));
				memoryValues.put(MAX_MEMORY, data.get(MAX_MEMORY));
				memoryValues.put(MAMORY_COMMITTED, data.get(MAMORY_COMMITTED));
				onSpy(createMessage(memoryTypeName, String.valueOf(spyedResult), 
                        Message.LV_ERROR, MEMORY_STATUS, memoryValues));
			}
		}
	}
	
	private void osMBeanSpy(Map<String, Object> beansMap) {
		if (CollectionUtils.isBlankMap(beansMap)) { return; }
		
		if (fileSpy == null) {
			return;
		}
		
		long maxFileCount = (Long) beansMap.get(MAX_FILE_COUNT);
		long fileCount = (Long) beansMap.get(OPENfILE_COUNT);
		
		int spyedResult = fileSpy.spyFiles(fileCount, maxFileCount);
		if (FileSpy.FILE_COUNTWILL_OUT_OF_MAX == spyedResult || 
				FileSpy.FILE_COUNT_LOWER_TO_GOOD == spyedResult) {
			onSpy(createMessage(OPENfILE_COUNT, 
                    String.valueOf(spyedResult), Message.LV_ERROR, FILES_STATUS, beansMap));
		}
	}
	

	protected void inspectMBean(ObjectInstance objectInstance,
			MBeanServerConnection mbsc) throws Exception {
		
		String className = objectInstance.getClassName();
		
		if (MEMORYIMPL_CLASS.equals(className)) {
			memoryMBeanSpy(getAttributesAsMap(objectInstance.getObjectName().toString(), mbsc));
		}
		
		if (UNIT_OS_CLASS.equals(className)) {
			osMBeanSpy(getAttributesAsMap(objectInstance.getObjectName().toString(), mbsc));
		}
	}

	@SuppressWarnings("unchecked")
	protected Message createMessage(String body, String description, int level,
			String type, Map<String, Object> beansMap) {
		Message message = new Message();
		message.setBody(body);
		message.setLevel(level);
		message.setId(UUID.randomUUID().toString());
		message.setDescription(description);
        message.setType(type);
		if (beansMap != null) {
			message.getProperties().putAll(beansMap);
		}
		return message;
	}
	
	/**
	 * @param heapMemorySpy the heapMemorySpy to set
	 */
	public void setHeapMemorySpy(MemorySpy heapMemorySpy) {
		this.heapMemorySpy = heapMemorySpy;
	}


	/**
	 * @param noneHeapMemorySpy the noneHeapMemorySpy to set
	 */
	public void setNoneHeapMemorySpy(MemorySpy noneHeapMemorySpy) {
		this.noneHeapMemorySpy = noneHeapMemorySpy;
	}

	/**
	 * @param fileSpy the fileSpy to set
	 */
	public void setFileSpy(FileSpy fileSpy) {
		this.fileSpy = fileSpy;
	}

	
	public final static String MEMORY_OBJECTNAME = "Memory";
	public final static String OS_OBJECTNAME = "OperatingSystem";
	protected Set<String> getObjectNamesPrefix() {
		Set<String> objectNamesPrefix = new HashSet<String>(2); 
		objectNamesPrefix.add(MEMORY_OBJECTNAME);
		objectNamesPrefix.add(OS_OBJECTNAME);
		
		return objectNamesPrefix;
	}

	@Override
	public void mscOnInterval(MBeanServerConnection mbsc) throws Exception {
		// ingnore
	}

	/* (non-Javadoc)
	 * @see org.xmatthew.spy2servers.component.spy.jmx.JmxSpySupportComponent#reStartJmxConnection()
	 */
	@Override
	protected void reStartJmxConnection() {
		if (heapMemorySpy != null) heapMemorySpy.init();
		if (noneHeapMemorySpy != null) noneHeapMemorySpy.init();
		if (fileSpy != null) fileSpy.init();
		super.reStartJmxConnection();
	}

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
