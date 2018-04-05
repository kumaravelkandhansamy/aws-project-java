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

package com.amazonaws.serverless.function;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.regions.Regions;
import com.amazonaws.serverless.dao.DynamoDBEventDao;
import com.amazonaws.serverless.domain.EngineersLog;
import com.amazonaws.serverless.domain.Event;
import com.amazonaws.serverless.pojo.EngineerId;
import com.amazonaws.serverless.util.Consts;
import com.amazonaws.serverless.util.ConvertDate;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class EventFunctions {

	private static final Logger log = Logger.getLogger(EventFunctions.class);

	private static final DynamoDBEventDao eventDao = DynamoDBEventDao.instance();
	// private DynamoDB dynamoDB;

	// static AmazonDynamoDB client =
	// AmazonDynamoDBClientBuilder.standard().build();
	// static DynamoDB dynamoDB = new DynamoDB(client);

	private Regions REGION = Regions.AP_SOUTHEAST_1;
	static String tableName = "ENGINEERS";
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);

	// private void initDynamoDbClient() {
	// AmazonDynamoDBClient client = new AmazonDynamoDBClient();
	// client.setRegion(Region.getRegion(REGION));
	// this.dynamoDB = new DynamoDB(client);
	// }

	public List<EngineersLog> getAllEventsHandler() {

		String randomNumbers = "";
		String split[] = null;
		String fstNo = "";
		String sndNo = "";
		randomNumbers = getWheelOfFate();
		split = randomNumbers.split(",");
		EngineersLog saveEngrLog = new EngineersLog();
		EngineersLog list = new EngineersLog();
		EngineersLog list1 = new EngineersLog();
		List<EngineersLog> elogs = new ArrayList<EngineersLog>();

		fstNo = split[0];
		sndNo = split[1];

		// 1st engineer
		List<Event> engrDetails = eventDao.findEventsById(Long.parseLong(fstNo));

		log.info("engr log " + engrDetails.size() + " total events");

		Long fstShiftEngrId = 0L;

		String fstShiftEngrName = engrDetails.get(0).getEngrName();
		fstShiftEngrId = engrDetails.get(0).getEngrId().longValue();

		engrDetails = null;

		// 2nd engineer
		engrDetails = eventDao.findEventsById(Long.parseLong(sndNo));
		String sndShiftEngrName = engrDetails.get(0).getEngrName();
		Long sndShiftEngrId = engrDetails.get(0).getEngrId();

		log.info("GetAllEvents invoked to scan table for ALL events");
		List<EngineersLog> engrLogList = eventDao.findAllEvents();
		
		log.info("Found *******	 " + engrLogList.size() + " total events.");

		long[] lng = new long[engrLogList.size()];
		long temp = 0L;

		if (!engrLogList.isEmpty()) { // record already there.
			log.info("records already there :::::::::::::::::");
			for (int i = 0; i < engrLogList.size(); i++) {
				log.info("events" + i + engrLogList.get(i));
				lng[i] = engrLogList.get(i).getShiftDate();
			}

			for (int i = 0; i < engrLogList.size(); i++) {
				for (int j = i + 1; j < engrLogList.size(); j++) {
					if (lng[i] < lng[j]) {
						temp = lng[i];
						lng[i] = lng[j];
						lng[j] = temp;
					}
				}
			}

			log.info("Decending order of shiftdate ::: ");

			long lastDate1 = 0L;
			long lastDate2 = 0L;

			for (int i = 0; i < engrLogList.size() - 1; i++) {
				log.info(lng[i] + " , ");
				lastDate1 = lng[0];
				lastDate1 = lng[1];
			}
			System.out.print(lng[engrLogList.size() - 1]);

			String lastDte1 = new ConvertDate().convertDate(lastDate1);
			String lastDte2 = new ConvertDate().convertDate(lastDate2);
			Long lastEngrId1 = 0L, lastEngrId2 = 0l;
			String lastShift1 = "", lastShift2 = "";

			log.info("lastest shiftdate : " + lastDte1);

			for (int i = 0; i < engrLogList.size(); i++) {
				// log.info("events" + i + engrLogList.get(i));
				// lng[i]=engrLogList.get(i).getShiftDate();
				if (engrLogList.get(i).getShiftDate().equals(lastDate1)) {
					lastEngrId1 = engrLogList.get(i).getEngrId();
					lastShift1 = engrLogList.get(i).getShiftType();
				}

				if (engrLogList.get(i).getShiftDate().equals(lastDate2)) {
					lastEngrId2 = engrLogList.get(i).getEngrId();
					lastShift2 = engrLogList.get(i).getShiftType();
				}

			}

			boolean test = false;
			if (fstShiftEngrId.equals(lastEngrId2) || fstShiftEngrId.equals(lastEngrId1)) {
				test = false;
			}

			if (sndShiftEngrId.equals(lastEngrId2) || sndShiftEngrId.equals(lastEngrId1)) {
				test = false;
			}

			while (!test) {
				randomNumbers = getWheelOfFate();
				split = randomNumbers.split(",");
				fstNo = split[0];
				sndNo = split[1];
				engrDetails = eventDao.findEventsById(Long.parseLong(fstNo));
				fstShiftEngrId = engrDetails.get(0).getEngrId();
				engrDetails = eventDao.findEventsById(Long.parseLong(sndNo));
				sndShiftEngrId = engrDetails.get(0).getEngrId();

				if (fstShiftEngrId.equals(lastEngrId2) || fstShiftEngrId.equals(lastEngrId1)) {
					test = false;
				} else {
					test = true;
				}

				if (sndShiftEngrId.equals(lastEngrId2) || sndShiftEngrId.equals(lastEngrId1)) {
					test = false;
				} else {
					test = true;
				}
			}

			if (test) {
				Long date = new Date().getTime();
				saveEngrLog.setEngrId(fstShiftEngrId);				
				saveEngrLog.setShiftDate(date);
				saveEngrLog.setShiftType(Consts.FIRSTSHIFT);
				
				boolean status = false;

				status = eventDao.saveEngineersLog(saveEngrLog);
				
				log.info("fstShiftEngrId $$$$$$$$$$$ "+fstShiftEngrId);
				log.info("sndShiftEngrId ########### "+sndShiftEngrId);
				
				if(status) {
					engrLogList = eventDao.findEventsById(fstShiftEngrId,date);
						
					
					
					for(EngineersLog lg:engrLogList) {
						list.setEngrId(lg.getEngrId());
						list.setShiftDate(lg.getShiftDate());
						list.setShiftType(lg.getShiftType());
						
					}

					
					
					elogs.add(list);
					
					for(EngineersLog l:elogs) {
						log.info(l.getEngrId()+" "+l.getShiftType()+" "+l.getShiftDate());
						
						
					}
				}

				log.info("first time status first shift " + status);

				saveEngrLog.setEngrId(sndShiftEngrId);
				saveEngrLog.setShiftDate(date);
				saveEngrLog.setShiftType(Consts.SECONDSHIFT);

				status = eventDao.saveEngineersLog(saveEngrLog);

				log.info("first time status second shift " + status);
				
				if(status) {
					engrLogList = eventDao.findEventsById(sndShiftEngrId,date);
					for(EngineersLog lg:engrLogList) {
						list1.setEngrId(lg.getEngrId());
						list1.setShiftDate(lg.getShiftDate());
						list1.setShiftType(lg.getShiftType());
						
					}
					
					elogs.add(list1);
					
					for(EngineersLog l:elogs) {
						log.info(l.getEngrId()+" "+l.getShiftType()+" "+l.getShiftDate());
						
						
					}
				}
				
				
				
			}

		} else { // empty records
			
			log.info("empty records :::::::::::::::::");
			Long date = new Date().getTime();
			saveEngrLog.setEngrId(fstShiftEngrId);
			saveEngrLog.setShiftDate(date);
			saveEngrLog.setShiftType(Consts.FIRSTSHIFT);

			boolean status = false;

			status = eventDao.saveEngineersLog(saveEngrLog);

			log.info("first time status first shift " + status);

			saveEngrLog.setEngrId(sndShiftEngrId);
			saveEngrLog.setShiftDate(date);
			saveEngrLog.setShiftType(Consts.SECONDSHIFT);

			status = eventDao.saveEngineersLog(saveEngrLog);

			log.info("first time status second shift " + status);
			
			if(status) {
				engrLogList = eventDao.findEventsById(fstShiftEngrId,date);
				
				list.setEngrId(engrLogList.get(0).getEngrId());
				list.setShiftDate(engrLogList.get(0).getShiftDate());
				list.setShiftType(engrLogList.get(0).getShiftType());
				
				elogs.add(list);
			}
			
			if(status) {
				engrLogList = eventDao.findEventsById(sndShiftEngrId,date);
				
				list.setEngrId(engrLogList.get(0).getEngrId());
				list.setShiftDate(engrLogList.get(0).getShiftDate());
				list.setShiftType(engrLogList.get(0).getShiftType());
				
				elogs.add(list);
			}

		}
		return elogs;
	}

	public static String getWheelOfFate() {
		boolean check = false;
		int fstRandomNumber = 0;
		int secondRandomNumber = 0;

		while (!check) {
			fstRandomNumber = getRandomInteger(10, 1);
			secondRandomNumber = getRandomInteger(10, 1);

			if (fstRandomNumber == secondRandomNumber) {
				check = false;
				fstRandomNumber = getRandomInteger(10, 1);
				secondRandomNumber = getRandomInteger(10, 1);
			}

			if (fstRandomNumber != secondRandomNumber) {
				check = true;
			}

		}

		return fstRandomNumber + "," + secondRandomNumber;
	}

	// public List<Event> getEventsForTeam(Team team) throws
	// UnsupportedEncodingException {
	//
	// if (null == team || team.getTeamName().isEmpty() ||
	// team.getTeamName().equals(Consts.UNDEFINED)) {
	// log.error("GetEventsForTeam received null or empty team name");
	// throw new IllegalArgumentException("Team name cannot be null or empty");
	// }
	//
	// String name = URLDecoder.decode(team.getTeamName(), "UTF-8");
	// log.info("GetEventsForTeam invoked for team with name = " + name);
	// List<Event> events = eventDao.findEventsByTeam(name);
	// log.info("Found " + events.size() + " events for team = " + name);
	//
	// return events;
	// }

	public List<Event> getEventsForCity(EngineerId engrId) throws UnsupportedEncodingException {

		log.error("engrId " + engrId.getEngrId());

		if (null == engrId || engrId.getEngrId().toString().isEmpty()
				|| engrId.getEngrId().toString().equals(Consts.UNDEFINED)) {
			log.error("engr Id is null or empty");
			throw new IllegalArgumentException("City name cannot be null or empty");
		}

		String name = URLDecoder.decode(engrId.getEngrId().toString(), "UTF-8");
		log.info("Get engr Id = " + name);
		List<Event> events = eventDao.findEventsById(engrId.getEngrId());
		log.info("Found " + events.size() + " events for city = " + name);

		return events;
	}

	public String saveOrUpdateEvent(Event event) {

		log.info("event ********* object" + eventDao.toString());

		if (null == event) {
			log.error("SaveEvent received null input");
			throw new IllegalArgumentException("Cannot save null object");
		}

		log.info("Engineer Name :: " + event.getEngrName());

		// log.info("Away Team :: " + event.getAwayTeam());
		// log.info("City :: " + event.getCity());
		// log.info("Country :: " + event.getCountry());
		// log.info("Home Team :: " + event.getHomeTeam());
		// log.info("Sport :: " + event.getSport());
		// log.info("Event Date :: " + event.getEventDate());
		// log.info("Event ID :: " + event.getEventId());

		//
		boolean status = eventDao.saveOrUpdateEvent(event);
		String saveStatus = "";
		if (status) {
			log.info("Successfully saved/updated event");
			saveStatus = "success";
		} else {
			log.info("Failed to save/udpate event");
			saveStatus = "failure";
		}
		return saveStatus;
	}

	// public void deleteEvent(Event event) {
	//
	// if (null == event) {
	// log.error("DeleteEvent received null input");
	// throw new IllegalArgumentException("Cannot delete null object");
	// }
	//
	// log.info("Deleting event for team = " + event.getHomeTeam() + " , date = " +
	// event.getEventDate());
	// eventDao.deleteEvent(event.getHomeTeam(), event.getEventDate());
	// log.info("Successfully deleted event");
	// }

	public static int getRandomInteger(int maximum, int minimum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

}
