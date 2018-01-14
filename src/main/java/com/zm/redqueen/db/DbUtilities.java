package com.zm.redqueen.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class DbUtilities {
	static ApplicationContext ctxUser = new AnnotationConfigApplicationContext(UserAndAuthCfg.class);
	static DB dbUser = (DB) ctxUser.getBean("db");

	public static DBCollection getDBUserCollection(String collectionName) {
		return dbUser.getCollection(collectionName);
	}

}
