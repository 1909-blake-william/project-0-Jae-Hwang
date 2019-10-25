package com.revature.prompt;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class DepositWithdrawPrompt implements Prompt {
	
	private static Logger log = Logger.getRootLogger();
	
	public static final DepositWithdrawPrompt instance = new DepositWithdrawPrompt();

	private UserDao userDao = UserDao.currentImplementation;
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
		
		
		
		return null;
	}
}
