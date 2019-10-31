package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.ClickerDao;
import com.revature.util.AuthUtil;
import com.revature.util.ClickerConnectionUtil;

public class PotatoClickerPrompt implements Prompt {
	private static Logger log = Logger.getRootLogger();
	
	public static final PotatoClickerPrompt instance = new PotatoClickerPrompt();

	private ClickerDao clickerDao = ClickerDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
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
		
		int[] potatoUser = clickerDao.extractUser(authUtil.getCurrentUser().getUserId());
		balance = potatoUser[0];
		upgrade = potatoUser[1];
		
		System.out.println("You currently have: " + balance + " potatoes!!!");
		System.out.println("upgrade level: " + upgrade);
		System.out.println();
		
		int upcost = (int) (Math.pow(10, upgrade) * 5);
		System.out.println("Enter 1 to start Clicking!");
		System.out.println("Enter 2 to Upgrade! cost for going " + upgrade + " to " + (upgrade+1) + ": " + upcost);
		System.out.println("Enter 3 to create Potato Farm!");
		System.out.println("Enter 4 to exchange potatoes with $$$");
		System.out.println("or Enter \"back\" to go back.");
		
		String selection = scan.nextLine();
		if (selection.toLowerCase().equals("back")) {
			System.out.println("\nGoing back to Account Menu");
			return AccountPrompt.instance;
		}
		
		switch (selection) {
		case "1":
			return ClickingPrompt.instance;
			
		case "2":
			if (upcost > balance) {
				System.out.println("You don't have enough potatoes, you fooool!");
				return PotatoClickerPrompt.instance;
			}
			clickerDao.upgrade(authUtil.getCurrentUser().getUserId(), upgrade);
			clickerDao.transaction(authUtil.getCurrentUser().getUserId(), (-1 * upcost) );
			balance -= upcost;
			
			System.out.println();
			return PotatoClickerPrompt.instance;
			
		case "3":
			return PotatoFarmPrompt.instance;
			
		case "4":
			System.out.println("NOT YET IMPLEMENTED");
			return AccountPrompt.instance;

		default:
			System.out.println("Invalid Selection, please Enter 1-4.\n");
			return PotatoClickerPrompt.instance;
		}
	}
	
	private PotatoClickerPrompt() {
		super();
	}

}
