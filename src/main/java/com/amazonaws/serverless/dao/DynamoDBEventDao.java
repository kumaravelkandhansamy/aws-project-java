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

package com.amazonaws.serverless.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.domain.EngineersLog;
import com.amazonaws.serverless.domain.Event;
import com.amazonaws.serverless.manager.DynamoDBManager;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DynamoDBEventDao implements EventDao {

	private static final Logger log = Logger.getLogger(DynamoDBEventDao.class);

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

	private static volatile DynamoDBEventDao instance;

	private DynamoDBEventDao() {
	}

	public static DynamoDBEventDao instance() {

		if (instance == null) {
			synchronized (DynamoDBEventDao.class) {
				if (instance == null)
					instance = new DynamoDBEventDao();
			}
		}
		log.info("instance" + instance);
		return instance;
	}

	@Override
	public List<EngineersLog> findAllEvents() {
		log.info("getting all details :::: ");
		return mapper.scan(EngineersLog.class, new DynamoDBScanExpression());
	}

	@Override
	public List<Event> findEventsById(Long engrId) {

		log.info("engrId name " + engrId);

		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":v1", new AttributeValue().withN(engrId.toString()));

		DynamoDBQueryExpression<Event> query = new DynamoDBQueryExpression<Event>().withConsistentRead(false)
				.withKeyConditionExpression("engrId = :v1").withExpressionAttributeValues(eav);

		return mapper.query(Event.class, query);

		// NOTE: without an index, this query would require a full table scan with a
		// filter:
		/*
		 * DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		 * .withFilterExpression("city = :val1") .withExpressionAttributeValues(eav);
		 * 
		 * return mapper.scan(Event.class, scanExpression);
		 */

	}

	public List<EngineersLog> findEventsById(Long engrId, Long date) {

		log.info("engrId name " + engrId);

		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":v1", new AttributeValue().withN(engrId.toString()));
		eav.put(":v1", new AttributeValue().withN(date.toString()));

		DynamoDBQueryExpression<EngineersLog> query = new DynamoDBQueryExpression<EngineersLog>()
				.withConsistentRead(false).withKeyConditionExpression("engrId = :v1")
				.withKeyConditionExpression("shiftDate = :v1").withExpressionAttributeValues(eav);

		log.info("query in dao :::::: " + query);
		EngineersLog elog = mapper.load(EngineersLog.class, engrId, date);
		// log.info("query in dao :::::: "+ list.size());
		// return list;
		EngineersLog list = new EngineersLog();

		list.setEngrId(elog.getEngrId());
		list.setShiftDate(elog.getShiftDate());
		list.setShiftType(elog.getShiftType());

		List<EngineersLog> elogs = new ArrayList<EngineersLog>();

		elogs.add(list);

		// return mapper.query(EngineersLog.class, query);
		return elogs;

		// NOTE: without an index, this query would require a full table scan with a
		// filter:
		/*
		 * DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		 * .withFilterExpression("city = :val1") .withExpressionAttributeValues(eav);
		 * 
		 * return mapper.scan(Event.class, scanExpression);
		 */

	}
	
	
	public List<EngineersLog> findEventsByDate(Long date) {

		

		Map<String, AttributeValue> eav = new HashMap<>();
		//eav.put(":v1", new AttributeValue().withN(engrId.toString()));
		eav.put(":v1", new AttributeValue().withN(date.toString()));

		DynamoDBQueryExpression<EngineersLog> query = new DynamoDBQueryExpression<EngineersLog>()
				.withConsistentRead(false)
				.withKeyConditionExpression("shiftDate").withExpressionAttributeValues(eav);
		
		 
		

		log.info("query in dao :::::: " + query);
		EngineersLog elog = mapper.load(EngineersLog.class, date);
		// log.info("query in dao :::::: "+ list.size());
		// return list;
		EngineersLog list = new EngineersLog();	
		

		list.setEngrId(elog.getEngrId());
		list.setShiftDate(elog.getShiftDate());
		list.setShiftType(elog.getShiftType());

		List<EngineersLog> elogs = new ArrayList<EngineersLog>();

		elogs.add(list);
		
		//List<EngineersLog> list1 = mapper.query(EngineersLog.class, query);

		// return mapper.query(EngineersLog.class, query);
		return mapper.query(EngineersLog.class, query);

		// NOTE: without an index, this query would require a full table scan with a
		// filter:
		/*
		 * DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
		 * .withFilterExpression("city = :val1") .withExpressionAttributeValues(eav);
		 * 
		 * return mapper.scan(Event.class, scanExpression);
		 */

	}

	/*
	 * @Override public List<Event> findEventsByTeam(String team) {
	 * 
	 * DynamoDBQueryExpression<Event> homeQuery = new DynamoDBQueryExpression<>();
	 * Event eventKey = new Event(); eventKey.setHomeTeam(team);
	 * homeQuery.setHashKeyValues(eventKey); List<Event> homeEvents =
	 * mapper.query(Event.class, homeQuery);
	 * 
	 * Map<String, AttributeValue> eav = new HashMap<>(); eav.put(":v1", new
	 * AttributeValue().withS(team)); DynamoDBQueryExpression<Event> awayQuery = new
	 * DynamoDBQueryExpression<Event>() .withIndexName(Event.AWAY_TEAM_INDEX)
	 * .withConsistentRead(false) .withKeyConditionExpression("awayTeam = :v1")
	 * .withExpressionAttributeValues(eav);
	 * 
	 * List<Event> awayEvents = mapper.query(Event.class, awayQuery);
	 * 
	 * // need to create a new list because PaginatedList from query is immutable
	 * List<Event> allEvents = new LinkedList<>(); allEvents.addAll(homeEvents);
	 * allEvents.addAll(awayEvents); allEvents.sort( (e1, e2) -> e1.getEventDate()
	 * <= e2.getEventDate() ? -1 : 1 );
	 * 
	 * return allEvents; }
	 */

	/*
	 * @Override public Optional<Event> findEventByTeamAndDate(String team, Long
	 * eventDate) {
	 * 
	 * Event event = mapper.load(Event.class, team, eventDate);
	 * 
	 * return Optional.ofNullable(event); }
	 */

	@Override
	public boolean saveOrUpdateEvent(Event event) {

		log.info("mapper" + mapper);

		mapper.save(event);

		return true;
	}

	@Override
	public boolean saveEngineersLog(EngineersLog engrLog) {

		log.info("mapper" + mapper);
		log.info("engr ID ::: " + engrLog.getEngrId());

		mapper.save(engrLog);

		return true;
	}

	/*
	 * @Override public void deleteEvent(String team, Long eventDate) {
	 * 
	 * Optional<Event> oEvent = findEventByTeamAndDate(team, eventDate); if
	 * (oEvent.isPresent()) { mapper.delete(oEvent.get()); } else {
	 * log.error("Could not delete event, no such team and date combination"); throw
	 * new IllegalArgumentException("Delete failed for nonexistent event"); } }
	 */
}
