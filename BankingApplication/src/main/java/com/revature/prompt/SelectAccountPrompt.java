package com.revature.prompt;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.models.Account;
import com.revature.util.AuthUtil;

public class SelectAccountPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();

	public static SelectAccountPrompt instance = new SelectAccountPrompt();

	private AccountDao accountDao = AccountDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	private Scanner scan = new Scanner(System.in);

	@Override
	public Prompt run() {
		// get view account then prompt user to select from that list.
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Oops, something went wrong.");
			return HomePrompt.instance;
		}

		System.out.println("\tView and Select Account\n");

		System.out.println("Accounts under " + authUtil.getCurrentUser().getUserName() + ":");

		List<Account> accounts = accountDao.findByUserId(authUtil.getCurrentUser().getUserId());
		if (accounts.size() == 0) {
			System.out.println("\tThere are currently no account open.\t");
			System.out.println("Going back to Main Menu");
			return MainMenuPrompt.instance;
		}
		for (int i = 0; i < accounts.size(); i++) {
			System.out.println((i + 1) + ". : " + accounts.get(i));
		}

		System.out.println("\nFor Account options, select the account by entering shown number: ");
		System.out.println("To go back to Main Menu enter \"back\"");

		String selection = scan.nextLine();

		if (selection.toLowerCase().equals("back")) {
			System.out.println("\nGoing back to Main Menu");
			return MainMenuPrompt.instance;
		}

		// if it contains non digit, removes all none digits and tries to match it to id
		selection = selection.replaceAll("[^0-9]", "");

		if (selection.length() != 0) {
			log.trace("Selection: " + selection);
			int selectedInt = Integer.parseInt(selection) - 1;
			
			if (selectedInt < accounts.size()) {
				authUtil.setCurrentAccount(accounts.get(selectedInt));
			}
		}

		if (!(authUtil.getCurrentAccount() == null)) {
			log.trace("Selected account: " + authUtil.getCurrentAccount());
			return AccountPrompt.instance;
		}
		
		System.out.println("Invalid Input, please enter valid information.");
		return SelectAccountPrompt.instance;

	}

	private SelectAccountPrompt() {
		super();
	}

}
