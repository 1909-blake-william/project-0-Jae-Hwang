package com.revature.util;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.models.Account;
import com.revature.models.User;

public class AuthUtil {
	
	public static final AuthUtil instance = new AuthUtil();
	
	private UserDao userDao = UserDao.currentImplementation;
	private AccountDao accountDao = AccountDao.currentImplementation;
	private User currentUser = null;
	private Account currentAccount = null;
	
	
	private AuthUtil () {
		super();
	}
	
	public User login(String username, String password) {
		User u = userDao.findByUsernameAndPassword(username, password);
		currentUser = u;
		return u;
	}
	
	public void login(User u) {
		currentUser = u;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentAccount(Account account) {
		currentAccount = account;
	}
	
	public void setCurrentAccount(int id) {
		currentAccount = accountDao.findById(id);
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	public void logout() {
		currentUser = null;
		currentAccount = null;
		System.out.println("Logout successful");
	}
}
