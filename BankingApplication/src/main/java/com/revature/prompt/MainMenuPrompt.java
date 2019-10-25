package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class MainMenuPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();
	
	public static MainMenuPrompt instance = new MainMenuPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	private Scanner scan = new Scanner(System.in);

	public Prompt run() {
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Oops, something went wrong.");
			return HomePrompt.instance;
		}
		System.out.println("Welcome, " + authUtil.getCurrentUser().getUserName() + ".\n");

		System.out.println("Enter 1 to View accounts and select an account");
		System.out.println("Enter 2 to Add a new account");
		System.out.println("Enter 3 to Log-out");

		String selection = scan.nextLine();

		switch (selection) {
		case "1":
			return SelectAccountPrompt.instance;
		case "2":
			System.out.println("Not yet implemented.");
			return this;
		case "3":
			authUtil.logout();
			return HomePrompt.instance;

		default:
			System.out.println("Invalid Selection, please enter 1, 2, or 3");
			break;
		}

		return null;
	}

	private MainMenuPrompt() {
		super();
	}

}
