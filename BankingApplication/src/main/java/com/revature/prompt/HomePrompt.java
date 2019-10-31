package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

public class HomePrompt implements Prompt{
	
	private static Logger log = Logger.getRootLogger();
	
	public static final HomePrompt instance = new HomePrompt();
	
	private Scanner scan = new Scanner(System.in);
	
	public Prompt run() {
		
		System.out.println("Enter 1 to log in");
		System.out.println("Enter 2 to register new account\n");
		
		String selection = scan.nextLine();
		log.trace("Selected: " + selection);
		
		switch (selection) {
		case "1":
			log.trace("Switch: 1");
			return LoginPrompt.instance;
		case "2":
			log.trace("Switch: 2");
			return RegisterPrompt.instance;

		default:
			System.out.println("Invalid option, please enter 1 or 2.\n");
			return HomePrompt.instance;
		}
	}
	
	private HomePrompt () {
		super();
		
	}
}
