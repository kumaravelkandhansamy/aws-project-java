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


package com.amazonaws.serverless.manager;


import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


public class DynamoDBManager {

    private static volatile DynamoDBManager instance;
    private static final Logger log = Logger.getLogger(DynamoDBManager.class);


    private static DynamoDBMapper mapper;

    private DynamoDBManager() {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        //DynamoDBMapper dynamoDB = new DynamoDBMapper(client);

        

        log.info("client :::::::::::: "+client.toString());
        mapper = new DynamoDBMapper(client);
    }

    public static DynamoDBManager instance() {
    	
        if (instance == null) {
            synchronized(DynamoDBManager.class) {
                if (instance == null)
                    instance = new DynamoDBManager();
            }
            
        }
        log.info("instance ::::"+instance);

        return instance;
    }

    public static DynamoDBMapper mapper() {
log.info("mapper ::::"+mapper);
        DynamoDBManager manager = instance();
        log.info("manager ::::"+manager);
        return manager.mapper;
    }

}
