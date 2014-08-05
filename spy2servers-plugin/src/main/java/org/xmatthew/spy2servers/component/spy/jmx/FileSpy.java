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

import java.util.Date;

import org.xmatthew.spy2servers.component.util.StorageUnitUtils;

/**
 * @author Matthew Xie
 *
 */
public class FileSpy {
	
	public final static int FILE_COUNT_GOOD = 0;
	public final static int FILE_COUNTWILL_OUT_OF_MAX = 1;
	public final static int FILE_COUNT_LOWER_TO_GOOD = 2;

	private long filesOpenedToAlert;
	
	private float filesOpenedPercentToAlert;
	
	private long alertAfterKeepTimeLive;
	
	private Date keepLiveDate;
	
	private int status;

	/**
	 * @return the filesOpened
	 */
	public long getFilesOpenedToAlert() {
		return filesOpenedToAlert;
	}

	/**
	 * @param filesOpened the filesOpened to set
	 */
	public void setFilesOpenedToAlert(long filesOpened) {
		this.filesOpenedToAlert = filesOpened;
	}

	/**
	 * @return the alertAfterKeepTimeLive
	 */
	public long getAlertAfterKeepTimeLive() {
		return alertAfterKeepTimeLive;
	}

	/**
	 * @param alertAfterKeepTimeLive the alertAfterKeepTimeLive to set
	 */
	public void setAlertAfterKeepTimeLive(long alertAfterKeepTimeLive) {
		this.alertAfterKeepTimeLive = alertAfterKeepTimeLive;
	}

	/**
	 * @return the usedPercent
	 */
	public float getFilesOpenedPercentToAlert() {
		return filesOpenedPercentToAlert;
	}

	/**
	 * @param usedPercent the usedPercent to set
	 */
	public void setFilesOpenedPercentToAlert(float usedPercent) {
		this.filesOpenedPercentToAlert = usedPercent;
	}

	/**
	 * 
	 */
	public FileSpy() {
		this.filesOpenedPercentToAlert = 0;
		this.alertAfterKeepTimeLive = -1;
		this.filesOpenedToAlert = -1;
		init();
	}
	
	public void init() {
		this.keepLiveDate = null;
		this.status = FILE_COUNT_GOOD;
	}

	public int spyFiles(long fileCount, long maxFileCount) {
		if ((filesOpenedToAlert == -1 && filesOpenedPercentToAlert <=0) || alertAfterKeepTimeLive == -1) {
			return FILE_COUNT_GOOD;
		}
		
		boolean isSpyed = false;
		if (filesOpenedPercentToAlert > 0) {
			if (filesOpenedPercentToAlert <= (fileCount / Double.valueOf(maxFileCount) * 100)) {
				isSpyed = true;
			}
		} else {
			if (StorageUnitUtils.asMbyteFromByte(fileCount) >= this.filesOpenedToAlert) {
				isSpyed = true;
			}
		}
		
		if (isSpyed) {
			if (keepLiveDate == null) {
				keepLiveDate = new Date();
				return FILE_COUNT_GOOD;
			}
			if (status != FILE_COUNTWILL_OUT_OF_MAX) {
				long timeKeepLived = (System.currentTimeMillis() - keepLiveDate.getTime()) / 1000;
				if (timeKeepLived >= alertAfterKeepTimeLive) {
					status = FILE_COUNTWILL_OUT_OF_MAX;
					return FILE_COUNTWILL_OUT_OF_MAX;
				}
			}
		} else {
			if (status == FILE_COUNTWILL_OUT_OF_MAX) {
				status = FILE_COUNT_GOOD;
				return FILE_COUNT_LOWER_TO_GOOD;
			}
		}
		return FILE_COUNT_GOOD;
	}

}
