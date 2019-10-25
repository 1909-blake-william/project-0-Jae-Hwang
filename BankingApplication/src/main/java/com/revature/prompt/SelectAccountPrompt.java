package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class SelectAccountPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();
	
	public static SelectAccountPrompt instance = new SelectAccountPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
	private Scanner scan = new Scanner(System.in);
	
	@Override
	public Prompt run() {
		//get view account then prompt user to select from that list.
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Oops, something went wrong.");
			return HomePrompt.instance;
		}
		
		System.out.println("\tView and Select Account\n");
		authUtil.getCurrentUser().showAccounts();
		
		System.out.println("For Account options, select the account by entering Account ID: ");
		System.out.println("To go back to Main Menu enter \"back\"");
		
		String selection = scan.nextLine();
		
		if (selection.toLowerCase().equals("back")) {
			System.out.println("\nGoing back to Main Menu");
			return MainMenuPrompt.instance;
		}
		
		// if it contains non digit, removes all none digits and tries to match it to id
		selection = selection.replaceAll("[^0-9]", "");
		if (authUtil.getCurrentUser().getUserAccounts().containsKey(selection)) {
			authUtil.setCurrentAccount(Integer.parseInt(selection));
			return AccountPrompt.instance;
		}
		
		System.out.println("Invalid Input, please enter valid information.");
		return AccountPrompt.instance;
	}
	
	private SelectAccountPrompt() {
		super();
	}

}
