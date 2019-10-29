package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.models.Account;
import com.revature.util.AuthUtil;

public class DepositWithdrawPrompt implements Prompt {
	
	private static Logger log = Logger.getRootLogger();
	
	public static final DepositWithdrawPrompt instance = new DepositWithdrawPrompt();

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
		
		System.out.println("Not Yet Implemented.\n");
		
		System.out.println("\tDeposit or Withdraw\n");
		System.out.println("Enter 1 to Deposit");
		System.out.println("Enter 2 to Withdraw");
		
		String selection = scan.nextLine();
		log.trace("Selection: " + selection);
		boolean isDeposit; // true for deposit, false for withdraw
		switch (selection) {
		case "1":
			isDeposit = true;
			break;
			
		case "2":
			isDeposit = false;
			break;
			
		default:
			System.out.println("Invalid option, please enter 1 or 2.\n");
			return AccountPrompt.instance;
		}
		
		if (isDeposit) {
			System.out.println("Enter the amount you would like to Deposit.");
		} else {
			System.out.println("Enter the amount you would like to Withdraw.");
		}
		selection = scan.nextLine();
		selection = selection.replaceAll("[^0-9]", "");
		log.trace("Selection: " + selection);
		int selectedInt = Integer.parseInt(selection);
		
		if (isDeposit) {
			accountDao.transaction(authUtil.getCurrentAccount().getId(), selectedInt);
			authUtil.getCurrentAccount().setBalance(selectedInt + authUtil.getCurrentAccount().getBalance());
		} else {
			accountDao.transaction(authUtil.getCurrentAccount().getId(), selectedInt * (-1));
			authUtil.getCurrentAccount().setBalance(selectedInt * (-1) + authUtil.getCurrentAccount().getBalance());
		}
		
		return AccountPrompt.instance;
	}
	
	private DepositWithdrawPrompt () {
		super();
	}
}
