package com.revature.driver;

import org.apache.log4j.Logger;

import com.revature.prompt.HomePrompt;
import com.revature.prompt.Prompt;
import com.revature.util.ConnectionUtil;

public class BankAppDriver {
	
	private static Logger log = Logger.getRootLogger();
	
	private static Prompt p = HomePrompt.instance;
	private static ConnectionUtil connectionUtil = ConnectionUtil.instance;
	
	public static void main(String[] args) {
		connectionUtil.setConnection();
		while (true) {
			log.trace("Running Prompt: " + p.getClass()+"\n");
			p = p.run();
			log.trace("Next Prompt: " + p.getClass()+"\n");
		}
	}
}
