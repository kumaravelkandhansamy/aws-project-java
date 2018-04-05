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


import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;


@DynamoDBTable(tableName = "ENGINEERS")
public class Event implements Serializable {

    private static final long serialVersionUID = -8243145429438016232L;
//    public static final String CITY_INDEX = "city-index";
 //   public static final String AWAY_TEAM_INDEX = "awayTeam-index";

    @DynamoDBHashKey
    private Long engrId;

//    @DynamoDBRangeKey
//    private Long eventDate;

    @DynamoDBAttribute
    private String address;

    @DynamoDBAttribute
    private String engrName;

//    @DynamoDBIndexHashKey(globalSecondaryIndexName = AWAY_TEAM_INDEX)
//    private String awayTeam;
//
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = CITY_INDEX)
//    private String city;
//
//    @DynamoDBAttribute
//    private String country;

    public Event() { }

    public Event(Long engrId, String engrName) {
        this.engrId = engrId;
        this.engrName = engrName;
    }

    public Event(Long engrId, String engrName, String address) {
        this.engrId = engrId;
        this.engrName = engrName;
        this.address = address;
        
    }
    

	public Long getEngrId() {
		return engrId;
	}

	public void setEngrId(Long engrId) {
		this.engrId = engrId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEngrName() {
		return engrName;
	}

	public void setEngrName(String engrName) {
		this.engrName = engrName;
	}

}
