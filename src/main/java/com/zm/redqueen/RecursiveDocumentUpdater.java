package com.zm.redqueen;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.zm.redqueen.config.Config;
import com.zm.redqueen.constants.AMDBConstants;
import com.zm.redqueen.db.DbUtilities;

public class RecursiveDocumentUpdater {
	private static Logger log = Logger.getLogger(RecursiveDocumentUpdater.class.getName());

	public static void main(String[] args) throws JSONException {
		Config.context = new ClassPathXmlApplicationContext("ConfigProperties.xml");
		String docId = "userId";

		DBCollection collection = DbUtilities.getDBUserCollection(AMDBConstants.USERS_COLLECTION);
		BasicDBObject findObj = new BasicDBObject("_id", new ObjectId("5a544febe4b0b0f870437f40"));
		String user = "{\"timeUpdated\":1.515819366E9,\"authType\":\"Zeemart\",\"outlet\":[{\"outletId\":\"OAAAA\",\"outletName\":\"KFC, Boon Keng\"},{\"outletId\":\"OAAAB\",\"outletName\":\"KFC, Outram Park\"}],\"ZeemartAuth\":{\"verificationCode\":\"$31$16$ypWyl-B1_7U2Na_l43pITgsgeezjvYyD6LQx4HqzjQk\",\"ZeemartId\":\"summa@zeemart.asia\",\"authToken\":\"ca779b68-f3a4-49e3-8d61-6620ab8f2c82\",\"password\":\"$31$16$guhAHnSSkQK3gNYCXZcSQ6KBaYTqUu0_D2NclUPPhWA\",\"expiryTime\":1.515862566E9,\"systemGenerted\":false,\"codeExpiryTime\":1.51546559E9},\"firstName\":\"Parul\",\"lastName\":\"Bhandari\",\"status\":\"A\",\"title\":\"Mobile Developer\",\"timeCreated\":1.506931336E9,\"phone\":6.583205554E9,\"lastLoggedIn\":1.515819366E9,\"imageURL\":\"https:\\/\\/www.clinicaledge.co\\/img\\/blank_user.jpg\"}";
		BasicDBObject insertDocDB = BasicDBObject.parse(user);

		WriteResult writeResult = collection.update(findObj, insertDocDB);

		log.info("Hello");
	}

	private static String getUserId() {
		DBCollection collection = DbUtilities.getDBUserCollection(AMDBConstants.USERS_COLLECTION);
		long noOfUsers = collection.count();
		String sequenceData = "";
		for (long i = noOfUsers; i >= 1;) {
			char s = (char) ('A' + (i % 26));
			sequenceData = s + sequenceData;
			i = (long) (i / 26);
		}
		sequenceData = "AAAAA".substring(sequenceData.length()) + sequenceData;
		return "U" + sequenceData + "";
	}

}
