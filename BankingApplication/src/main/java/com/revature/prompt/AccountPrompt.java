package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class AccountPrompt implements Prompt {
	
	private static Logger log = Logger.getRootLogger();
	
	public static final AccountPrompt instance = new AccountPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AccountDao accountDao = AccountDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
	private Scanner scan = new Scanner(System.in);
	
	@Override
	public Prompt run() {
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Oops, something went wrong.");
			return HomePrompt.instance;
		} else if (authUtil.getCurrentAccount() == null) {
			System.out.println("Oops, something went wrong.");
			return SelectAccountPrompt.instance;
		}
		
		System.out.println("\tAccount Options\n");
		System.out.println("Hello " + authUtil.getCurrentUser().getUserName() + ".");
		System.out.println("Here are the options for your account: " + authUtil.getCurrentAccount());
		System.out.println("Enter 1 to Deposit/Withdraw");
		System.out.println("Enter 2 to View Account transaction History");
		System.out.println("Enter 3 to Remove this account");
		System.out.println("Enter \"back\" to return to Main Menu");
		
		String selection = scan.nextLine();
		
		if (selection.toLowerCase().equals("back")) {
			return MainMenuPrompt.instance;
		}
		
		switch (selection) {
		case "1":
			return DepositWithdrawPrompt.instance;
		case "2":
			System.out.println("Account Transaction History");
			accountDao.viewAccountTransactions(authUtil.getCurrentAccount().getId());
			return AccountPrompt.instance;
		case "3":
			System.out.println("Remaining balance is: " + authUtil.getCurrentAccount().getBalance() + ".");
			System.out.println("which will be returned shortly.");
			return SelectAccountPrompt.instance;

		default:
			System.out.println("Invalid Selection, please Enter 1, 2, or 3.\n");
			return AccountPrompt.instance;
		}
	}
	
	private AccountPrompt() {
		super();
	}

}
