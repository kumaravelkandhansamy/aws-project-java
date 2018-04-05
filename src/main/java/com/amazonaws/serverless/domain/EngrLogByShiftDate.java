package com.amazonaws.serverless.domain;

import java.util.Comparator;

public class EngrLogByShiftDate implements Comparator<EngineersLog> {

	public int compare(EngineersLog el1,EngineersLog el2) {
		int flag = (int) (el1.getShiftDate() - el2.getShiftDate());
		if(flag == 0) flag = el1.getEngrId().compareTo(el2.getEngrId());
		return flag;
	}
}
