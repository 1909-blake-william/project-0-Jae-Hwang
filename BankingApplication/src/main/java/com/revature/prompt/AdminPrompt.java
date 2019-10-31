package com.revature.prompt;

import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.AuthUtil;
import com.revature.util.ClickerConnectionUtil;

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
			log.debug("Lost current user, going back to home.");
			return HomePrompt.instance;
		} else if (!authUtil.getCurrentUser().getPermission()) {
			log.debug("Wrong user, going back to home.");
			authUtil.logout();
			return HomePrompt.instance;
		}

		System.out.println("\n\tAdmin Menu\n");
		System.out.println("Enter 1 to view All Users");
		System.out.println("Enter 2 to view All Accounts");
		System.out.println("Enter 3 to view All Transactions");
		System.out.println("Enter \"back\" to log out and return to Home");
		System.out.println("Enter \"TRACE\" to change log level to trace");
		System.out.println("Enter \"DEBUG\" to change log level to debug");

		String selection = scan.nextLine();
		log.trace("Selection: " + selection);

		if (selection.toLowerCase().equals("back")) {
			authUtil.logout();
			return HomePrompt.instance;
		}
		
		if (selection.toLowerCase().equals("trace")) {
			Logger.getRootLogger().setLevel(Level.TRACE);
			return AdminPrompt.instance;
		}
		
		if (selection.toLowerCase().equals("debug")) {
			Logger.getRootLogger().setLevel(Level.DEBUG);
			return AdminPrompt.instance;
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
			
		case "4":
			System.out.println("   *  o                                      *");
			System.out.println("*   )  *  o                            o  * (     o");
			System.out.println(" \\ (   )  -~*                         *~ (   ) o ");
			System.out.println("o ) \\ ( /                               ) \\ ( /");
			System.out.println("   _____                                _____");
			System.out.println("   \\* *|                                |* */");
			System.out.println("    \\* |                                | */");
			System.out.println("     \\*|    Potato Clicker ENABLED.     |*/");
			System.out.println("      \\|                                |/\n");

			ClickerConnectionUtil.start();
			return HomePrompt.instance;
			
		default:
			System.out.println("Invalid Selection, please Enter 1, 2, 3, or back.\n");
			return AdminPrompt.instance;
		}

	}

	private AdminPrompt() {
		super();
	}

}
