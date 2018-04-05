package com.amazonaws.serverless.util;

import java.text.DateFormat;
import java.util.*;

public class ConvertDate {
	public static void main(String args[]) {
		ConvertDate f = new ConvertDate();

		Long str = new Long(1522699544038L);
		
		System.out.println(f.convertDate(str));
	}

	public String convertDate(Long str) {
		Date epoch = new Date(str);
		DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String s2 = df2.format(epoch);

		return s2;

	}
}
