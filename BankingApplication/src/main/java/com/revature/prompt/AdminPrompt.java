package com.revature.prompt;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class AdminPrompt implements Prompt {

	private static Logger log = Logger.getRootLogger();

	public static AdminPrompt instance = new AdminPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AccountDao accountDao = AccountDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	private Scanner scan = new Scanner(System.in);

	@Override
	public Prompt run() {
		if (authUtil.getCurrentUser() == null) {
			System.out.println("Network Error.");
			return HomePrompt.instance;
		} else if (!authUtil.getCurrentUser().getPermission()) {
			System.out.println("Network Error.");
			authUtil.logout();
			return HomePrompt.instance;
		}

		System.out.println("\tAdmin Menu\n");
		System.out.println("Enter 1 to view All Users");
		System.out.println("Enter 2 to view All Accounts");
		System.out.println("Enter 2 to view All Transactions");
		System.out.println("Enter back to log out and return to Home");

		String selection = scan.nextLine();
		log.trace("Selection: " + selection);

		if (selection.toLowerCase().equals("back")) {
			authUtil.logout();
			return HomePrompt.instance;
		}

		switch (selection) {
		case "1":
			List<User> users = userDao.findAll();
			System.out.println("[ User ID | Username | Password ]");
			users.forEach( user -> {
				System.out.print("[ " + user.getUserId() + "  | ");
				System.out.print(user.getUserName() + "  | ");
				System.out.println(user.getPassword() + " ]");
			});
			return AdminPrompt.instance;
		case "2":
			accountDao.findAllWithNames();
			
			/*  Changed to findAllWithNames
			
			List<Account> accounts = accountDao.findAll();
			System.out.println("Account ID\t| Owner ID\t| Balance");
			accounts.forEach( account -> {
				System.out.print(account.getId() + "  \t| ");
				System.out.print(account.getUserId() + "  \t| ");
				System.out.println(account.getBalance() );
			});
			*/
			
			return AdminPrompt.instance;
			
		case "3":
			accountDao.viewAllTransactions();
			
			/*  Changed to findAllWithNames
			
			List<Account> accounts = accountDao.findAll();
			System.out.println("Account ID\t| Owner ID\t| Balance");
			accounts.forEach( account -> {
				System.out.print(account.getId() + "  \t| ");
				System.out.print(account.getUserId() + "  \t| ");
				System.out.println(account.getBalance() );
			});
			*/
			
			return AdminPrompt.instance;

		default:
			System.out.println("Invalid Selection, please Enter 1, 2, 3, or back.\n");
			return AdminPrompt.instance;
		}

	}

	private AdminPrompt() {
		super();
	}

}
