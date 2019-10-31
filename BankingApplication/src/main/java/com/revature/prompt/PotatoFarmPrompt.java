package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.ClickerDao;
import com.revature.util.AuthUtil;
import com.revature.util.ClickerConnectionUtil;

public class PotatoFarmPrompt implements Prompt {
	private static Logger log = Logger.getRootLogger();

	public static final PotatoFarmPrompt instance = new PotatoFarmPrompt();

	private ClickerDao clickerDao = ClickerDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	private ClickerConnectionUtil ccu = ClickerConnectionUtil.instance;

	private Scanner scan = new Scanner(System.in);

	// current user info
	private int balance;
	private int upgrade;
	/*
	CREATE SEQUENCE potato_users_id_seq;
	CREATE TABLE potato_users (
		user_id INT REFERENCES bank_users(user_id),
		balance NUMBER DEFAULT 0,
		upgrades INT DEFAULT 0
	);
	*/
	
	@Override
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
		
		if (ccu.getActiveNum() == 10) {
			System.out.println("All the Farm Slots are currently working VERY HARD. Come back Later.");
			return PotatoClickerPrompt.instance;
		}
		
		int[] potatoUser = clickerDao.extractUser(authUtil.getCurrentUser().getUserId());
		balance = potatoUser[0];
		upgrade = potatoUser[1];

		System.out.println("You currently have: " + balance + " potatoes!!!");
		System.out.println("upgrade level: " + upgrade);

		System.out.println("Making a POTATO FARMS!!!");
		System.out.println("Produces [Paid Potatoes]/50 * Upgrade per second for 100 secconds.");
		System.out.println("Enter amount you wish to pay!");
		System.out.println("or Enter \"back\" to go back.");

		String selection = scan.nextLine();
		if (selection.toLowerCase().equals("back")) {
			System.out.println("\nGoing back to Main Menu");
			return PotatoClickerPrompt.instance;
		}

		if (selection.length() != 0) {

			int selectedInt = Integer.parseInt(selection);

			if (balance < selectedInt) {
				System.out.println("You don't have enough potatoes, DON'T TRY TO SCAM ME!!!");
				authUtil.logout();
				return HomePrompt.instance;
			}

			log.trace("Selection: " + selectedInt);

			clickerDao.automated(authUtil.getCurrentUser().getUserId(), 
								selectedInt, 
								upgrade).start();
			clickerDao.transaction(authUtil.getCurrentUser().getUserId(), (-1 * selectedInt));
			return PotatoClickerPrompt.instance;
		}

		return PotatoClickerPrompt.instance;
	}

	private PotatoFarmPrompt() {
		super();
	}
}
