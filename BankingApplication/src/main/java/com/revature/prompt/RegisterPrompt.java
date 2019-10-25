package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class RegisterPrompt implements Prompt{

	private static Logger log = Logger.getRootLogger();
	
	public static RegisterPrompt instance = new RegisterPrompt();

	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	
	private Scanner scan = new Scanner(System.in);
	
	@Override
	public Prompt run() {
		System.out.println("\t Register\n");
		
		boolean isValidName = false;
		String newUsername = "";
		while (!isValidName) {
			System.out.println("Enter the new Username: ");
			System.out.println("or enter \"back\" to go back to Home.");
			
			newUsername = scan.nextLine();
			
			if (newUsername.toLowerCase().equals("back")) {
				System.out.println("\ngoing back to Home\n");
				return HomePrompt.instance;
			}
			
			if (newUsername.length() < 5 || newUsername.length() > 20) {
				System.out.println("please Enter at least 5 and at most 15 characters for Username");
			} else if (userDao.findByUsername(newUsername) == null) {
				isValidName = true;
			} else {
				System.out.println("Entered username already exists, please try again.");
			}
		}
		
		System.out.println("\nEnter the password: ");
		
		String password = scan.nextLine();
		User newUser = new User(newUsername, password);
		int userId = userDao.save(newUser);
		if (userId == 0) {
			log.debug("INSERT failed.");
			System.out.println("Network Error, returning to the Home.");
			return HomePrompt.instance;
		}
		newUser.setUserId(userId);
		authUtil.login(newUser);
		
		return MainMenuPrompt.instance;
	}
	
	private RegisterPrompt() {
		super();
	}

}
