/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.util;

import java.util.Calendar;
import java.util.Date;

public class FSTimeObject {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int munite;

	public FSTimeObject() {

	}

	public boolean after(final FSTimeObject thareTime) {
		if (getYear() == thareTime.getYear() || thareTime.getYear() > getYear()) {
			System.out.println("after:: Year true");
			if (thareTime.getMonth() < getMonth()) {
				return true;
			}
			if (getMonth() == thareTime.getMonth()) {
				System.out.println("after:: Month true");
				if (thareTime.getDay() < getDay()) {
					System.out.println("after:: Day true");
					return true;
				}
				if (getDay() == thareTime.getDay()) {
					if (getHour() == thareTime.getHour()) {
						if (thareTime.getMunite() < getMunite()) {
							return true;
						}
					}
					if (getHour() > thareTime.getHour()) {
						return true;
					}
				}
			}
		}
		return false;

	}

	public boolean before(final FSTimeObject thareTime) {
		if (getYear() == thareTime.getYear() || thareTime.getYear() > getYear()) {
			if (getMonth() == thareTime.getMonth() || thareTime.getMonth() > getMonth()) {

				if (thareTime.getMonth() > getMonth()) {
					return true;
				}
				if (thareTime.getDay() > getDay()) {
					return true;
				}
				if (getDay() == thareTime.getDay()) {
					if (getHour() == thareTime.getHour()) {
						if (getMunite() < thareTime.getMunite()) {
							return true;
						}
					}

					if (getHour() < thareTime.getHour()) {
						return true;
					}

				}
			}
		}

		return false;
	}

	public int getDay() {
		return this.day;
	}

	public int getHour() {
		return this.hour;
	}

	public int getMonth() {
		return this.month;
	}

	public int getMunite() {
		return this.munite;
	}

	public int getYear() {
		return this.year;
	}

	public void setDay(final int day) {
		this.day = day;
	}

	public void setHour(final int hour) {
		this.hour = hour;
	}

	public void setMonth(final int month) {
		this.month = month;
	}

	public void setMunite(final int munite) {
		this.munite = munite;
	}

	public void setYear(final int year) {
		this.year = year;
	}

	public FSTimeObject toTimeObject(final Date date, final Date time) {
		final FSTimeObject fsTimeObject = new FSTimeObject();
		final Calendar timeInstance = Calendar.getInstance();
		timeInstance.setTimeInMillis(time.getTime());
		fsTimeObject.setHour(timeInstance.get(Calendar.HOUR_OF_DAY));
		fsTimeObject.setMunite(timeInstance.get(Calendar.MINUTE));

		final Calendar dateInstance = Calendar.getInstance();
		dateInstance.setTime(date);
		fsTimeObject.setYear(dateInstance.get(Calendar.YEAR));
		fsTimeObject.setMonth(dateInstance.get(Calendar.MONTH));
		fsTimeObject.setDay(dateInstance.get(Calendar.DAY_OF_MONTH));
		return fsTimeObject;
	}
}
