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

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMunite() {
		return munite;
	}

	public void setMunite(int munite) {
		this.munite = munite;
	}

	public FSTimeObject toTimeObject(Date date,Date time) {
		FSTimeObject fsTimeObject = new FSTimeObject();
		Calendar timeInstance = Calendar.getInstance();
		timeInstance.setTimeInMillis(time.getTime());
		fsTimeObject.setHour(timeInstance.get(Calendar.HOUR_OF_DAY));
		fsTimeObject.setMunite(timeInstance.get(Calendar.MINUTE));
		
		Calendar dateInstance = Calendar.getInstance();
		dateInstance.setTime(date);
		fsTimeObject.setYear(dateInstance.get(Calendar.YEAR));
		fsTimeObject.setMonth(dateInstance.get(Calendar.MONTH));
		fsTimeObject.setDay(dateInstance.get(Calendar.DAY_OF_MONTH));
		return fsTimeObject;
	}

	public boolean after(FSTimeObject thareTime) {
		if(getYear() == thareTime.getYear() || thareTime.getYear() > getYear()){
			System.out.println("after:: Year true");
			if(thareTime.getMonth() < getMonth()){
				return true;
			}
			if(getMonth() == thareTime.getMonth()){
				System.out.println("after:: Month true");
				if(thareTime.getDay() < getDay()){
					System.out.println("after:: Day true");
					return true;
				}
				if(getDay() == thareTime.getDay()){
					if(getHour() == thareTime.getHour()){
						if(thareTime.getMunite()<getMunite()){
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

	public boolean before(FSTimeObject thareTime) {
		if(getYear() == thareTime.getYear() || thareTime.getYear() > getYear()){
			if(getMonth() == thareTime.getMonth() || thareTime.getMonth() > getMonth()){
				
				if(thareTime.getMonth() > getMonth()){
						return true;
				}
				if(thareTime.getDay() > getDay()){
					return true;
				}
				if(getDay() == thareTime.getDay()){
					if(getHour() == thareTime.getHour()){
						if(getMunite()<thareTime.getMunite()){
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
