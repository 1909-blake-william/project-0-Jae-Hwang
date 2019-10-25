package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class NewAccountPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();
	
	public static NewAccountPrompt instance = new NewAccountPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	private Scanner scan = new Scanner(System.in);

	public Prompt run() {
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Oops, something went wrong.");
			return HomePrompt.instance;
		}
		
		
		
		return null;
	}
	
	private NewAccountPrompt() {
		super();
	}
}
