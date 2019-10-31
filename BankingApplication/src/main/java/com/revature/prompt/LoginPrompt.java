package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.util.AuthUtil;

public class LoginPrompt implements Prompt {
	
	private static Logger log = Logger.getRootLogger();
	
	public static LoginPrompt instance = new LoginPrompt();

	private AuthUtil authUtil = AuthUtil.instance;
	
	private Scanner scan = new Scanner(System.in);
	
	@Override
	public Prompt run() {
		int loginAttempts = 0;
		while (loginAttempts <= 3) {
			System.out.println("\tUser Log-in \n");
			
			System.out.println("Enter the username: ");
			String username = scan.nextLine();
			log.trace("username: " + username);

			System.out.println("Enter the password: ");
			String password = scan.nextLine();
			log.trace("password: " + password);
			
			if (authUtil.login(username, password) != null) {
				log.trace("Credential info match, logging in");
				System.out.println("Succesfully logged into User : " + authUtil.getCurrentUser().getUserName()+"\n");
				log.trace("Permission: "+authUtil.getCurrentUser().getPermission());
				if (authUtil.getCurrentUser().getPermission()) {
					log.info("Logging into Admin User");
					return AdminPrompt.instance;
				} else {
					return MainMenuPrompt.instance;
				}
				
			} else if (loginAttempts < 3){
				log.info("User failed to login, attempt: " + (loginAttempts+1));
				System.out.println("Invalid Credential, please try again.\n");
				loginAttempts++;
				
			} else {
				log.info("User reached maximum login attempts, going back to home prompt");
				System.out.println("Invalid Credential, Failed to log-in. Going back to Home.\n");
				return HomePrompt.instance;
			}
		}
		return HomePrompt.instance;
	}
	
	private LoginPrompt () {
		super();
	}

}
