package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.daos.ClickerDao;
import com.revature.util.AuthUtil;
import com.revature.util.ClickerConnectionUtil;

public class StartPotatoClickerPrompt implements Prompt {
	private static Logger log = Logger.getRootLogger();
	
	public static final StartPotatoClickerPrompt instance = new StartPotatoClickerPrompt();

	private AccountDao accountDao = AccountDao.currentImplementation;
	private ClickerDao clickerDao = ClickerDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
	private Scanner scan = new Scanner(System.in);
	
	public Prompt run() {
		if (!ClickerConnectionUtil.isStarted) {
			log.debug("How did you get here?!");
			return HomePrompt.instance;
		}
		
		if (authUtil.getCurrentUser() == null) {
			log.debug("Lost current user, going back to home.");
			return HomePrompt.instance;
		} else if (authUtil.getCurrentAccount() == null) {
			System.out.println("Lost current account, going back to selecting account.");
			return SelectAccountPrompt.instance;
		}
		
		System.out.println("Welcome to the VERY SUPER REALLY SECRET PotAtO cLICkeR.");
		
		if (clickerDao.getUserById(authUtil.getCurrentUser().getUserId())) {
			System.out.println("\nPotato User already exists! Let's Go!");
			return PotatoClickerPrompt.instance;
		}
		
		System.out.println("Now pay money to get some potatoes.\n");
		
		System.out.println("Current running rate for buying Potato:$ = 1:1");
		System.out.println("How many would you like to buy?: ");
		System.out.println("or Enter \"back\" to back out.");
		
		String selection = scan.nextLine();
		if (selection.toLowerCase().equals("back")) {
			System.out.println("\nGoing back to Main Menu");
			return MainMenuPrompt.instance;
		}

		// if it contains non digit, removes all none digits and tries to match it to id
		selection = selection.replaceAll("[^0-9]", "");

		if (selection.length() != 0) {
			
			int selectedInt = Integer.parseInt(selection);
			
			if (authUtil.getCurrentAccount().getBalance() < selectedInt) {
				System.out.println("You don't have that much money, YOU SCAMMER. GO AWAY.");
				authUtil.logout();
				return HomePrompt.instance;
			}
			
			log.trace("Selection: " + selectedInt);
			
			clickerDao.createUser(authUtil.getCurrentUser().getUserId(), selectedInt);
			accountDao.transaction(authUtil.getCurrentAccount().getId(), (-1 * selectedInt) );
			authUtil.getCurrentAccount().setBalance( (-1 * selectedInt) + authUtil.getCurrentAccount().getBalance());
			log.trace("Potato User created.");
			return PotatoClickerPrompt.instance;
		}
		
		System.out.println("Not a valid input, you just go back to home. :( ");
		authUtil.logout();
		return HomePrompt.instance;
	}
	
	private StartPotatoClickerPrompt() {
		super();
	}
}
