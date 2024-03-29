package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.models.Account;
import com.revature.util.AuthUtil;

public class MainMenuPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();
	
	public static MainMenuPrompt instance = new MainMenuPrompt();

	private AccountDao accountDao = AccountDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	private Scanner scan = new Scanner(System.in);

	public Prompt run() {
		if (authUtil.getCurrentUser() == null) {
			log.debug("Lost current user, going back to home.");
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
			Account newAccount = new Account(authUtil.getCurrentUser().getUserId());
			newAccount.setId(accountDao.save(newAccount));
			authUtil.setCurrentAccount(newAccount);
			System.out.println("Successfully created new account.");
			return AccountPrompt.instance;
		case "3":
			authUtil.logout();
			return HomePrompt.instance;

		default:
			System.out.println("Invalid Selection, please enter 1, 2, or 3");
			return HomePrompt.instance;
		}
	}

	private MainMenuPrompt() {
		super();
	}

}
