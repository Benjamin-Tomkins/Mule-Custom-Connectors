package com.kss.uClassify;

import org.mule.api.ConnectionException;

public class test {
	
	public static void main(String[] args){
		
		uClassifyConnector uc=new uClassifyConnector();
		
		uc.setAccessKey("TmqFnkCBXtOwDBkhuLMRiDOKI0");
		uc.setText("very happy");
		try {
			uc.connect("TmqFnkCBXtOwDBkhuLMRiDOKI0");
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(uc.myProcessor("ANALYZE_MOOD"));
	}

}
