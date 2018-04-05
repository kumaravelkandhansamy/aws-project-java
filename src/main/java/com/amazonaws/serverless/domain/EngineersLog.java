// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"). You may
// not use this file except in compliance with the License. A copy of the
// License is located at
//
//	  http://aws.amazon.com/apache2.0/
//
// or in the "license" file accompanying this file. This file is distributed
// on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language governing
// permissions and limitations under the License.

package com.amazonaws.serverless.domain;

import java.io.Serializable;
import java.util.Comparator;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ENGINEERS_LOG")
public class EngineersLog implements Serializable, Comparator<EngineersLog> {

	private static final long serialVersionUID = -8243145429438016232L;
	// public static final String CITY_INDEX = "city-index";
	// public static final String AWAY_TEAM_INDEX = "awayTeam-index";

	@DynamoDBHashKey
	private Long engrId;

	// @DynamoDBRangeKey
	// private Long eventDate;

	@DynamoDBAttribute
	private String shiftType; // fst shift or second shift

	@DynamoDBRangeKey
	private Long shiftDate;

	// @DynamoDBIndexHashKey(globalSecondaryIndexName = AWAY_TEAM_INDEX)
	// private String awayTeam;
	//
	// @DynamoDBIndexHashKey(globalSecondaryIndexName = CITY_INDEX)
	// private String city;
	//
	// @DynamoDBAttribute
	// private String country;

	public EngineersLog() {
		// TODO Auto-generated constructor stub
	}

	public EngineersLog(Long engrId) {
		this.engrId = engrId;

	}

	public EngineersLog(Long engrId, String shiftType, Long shiftDate) {
		this.engrId = engrId;
		this.shiftType = shiftType;
		this.shiftDate = shiftDate;

	}

	public Long getEngrId() {
		return engrId;
	}

	public void setEngrId(Long engrId) {
		this.engrId = engrId;
	}

	public String getShiftType() {
		return shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	public Long getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Long shiftDate) {
		this.shiftDate = shiftDate;
	}

	/*public int compareTo(EngineersLog engrLog) {	
		
		return (int) (this.engrId - engrLog.engrId);
	}*/
	
//	public static Comparator<> NameComparator = new Comparator<Employee>() {
//
//        @Override
//        public int compare(Employee e1, Employee e2) {
//            return e1.getName().compareTo(e2.getName());
//        }
//    };

	@Override
	public String toString() {
		return "EngineersLog [engrId=" + engrId + ", shiftType=" + shiftType + ", shiftDate=" + shiftDate + "]";
	}

	@Override
	public int compare(EngineersLog o1, EngineersLog o2) {
		// TODO Auto-generated method stub
		return (int) (o1.getShiftDate() - o2.getShiftDate());
	}

}
