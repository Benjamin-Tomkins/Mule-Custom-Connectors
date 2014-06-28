package com.sandeep.couchdb.util;

import java.util.List;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;

public class CouchDBTest {

	/*These are the keys of student document in couch db*/
	public static final String STUDENT_KEY_NAME ="name";
	
	public static final String STUDENT_KEY_MARKS ="marks";
	
	public static final String STUDENT_KEY_ROLL="roll";
	
	public static final String STUDENT_KEY_CONTACT ="contact";
	
	public static void main(String[] args){
		
		/*Creating a session with couch db running in 5984 port*/
		Session studentDbSession = new Session("localhost",5984);
		
		/*Selecting the student database from list of couch database*/
		Database studentCouchDb = studentDbSession.getDatabase("student");
		
		/*Fetching all Student Document to ViewResult object*/
		ViewResults couchViewResults = studentCouchDb.getAllDocuments();
		
		/*Retieving all document as result to a List*/
		List<Document> studentDocuments = couchViewResults.getResults();
		
		
		for(Document couchDocument: studentDocuments){
			
			String id = couchDocument.getJSONObject().getString("id");
			
			Document studentRow = studentCouchDb.getDocument(id);
			
			System.out.println("__________START OF DOCUMENT("+studentRow.get("_id")+")_________");
			
			if(studentRow.containsKey(STUDENT_KEY_NAME)){
				
				System.out.println("NAME : "+studentRow.get(STUDENT_KEY_NAME));
				
			}
			if(studentRow.containsKey(STUDENT_KEY_MARKS)){
				
				System.out.println("MARK : "+studentRow.get(STUDENT_KEY_MARKS));
				
			}
			
			if(studentRow.containsKey(STUDENT_KEY_ROLL)){
				
				System.out.println("ROLL : "+studentRow.get(STUDENT_KEY_ROLL));
				
			}
			if(studentRow.containsKey(STUDENT_KEY_CONTACT)){
				
				System.out.println("CONTACT : "+studentRow.get(STUDENT_KEY_CONTACT));
				
			}
			
		}
	}
}
