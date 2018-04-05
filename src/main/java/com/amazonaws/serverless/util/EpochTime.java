package com.amazonaws.serverless.util;

import java.util.Date;

public class EpochTime {
	
	public static void main(String args[]) {
		System.out.println("Date :: "+new Date().toString());
		System.out.println("Epoch time :: "+new EpochTime().DateTimeToLong(new Date()));
		
	}
	
	public long DateTimeToLong(Date date) {
		return date.getTime();
	}

}
