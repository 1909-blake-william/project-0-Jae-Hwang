package com.revature.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class User implements Serializable {
	private int userId;
	private String username;
	private String password;
	private HashMap<Integer, Account> userAccounts = new HashMap<>();
	private Account currentAccount;
	private boolean permis = false;

	public void showAccounts() {
		Set<Integer> accountIdSet = userAccounts.keySet();
		for (int id : accountIdSet) {
			System.out.println(id + ": " + userAccounts.get(id));
		}
	}

	// generated methods below
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public User(int userId, String username, String password) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public HashMap<Integer, Account> getUserAccounts() {
		return userAccounts;
	}

	public void setUserAccounts(HashMap<Integer, Account> userAccounts) {
		this.userAccounts = userAccounts;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}
	
	public boolean getPermission() {
		return permis;
	}
	
	public void setPermission(boolean permis) {
		this.permis = permis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userAccounts == null) ? 0 : userAccounts.hashCode());
		result = prime * result + userId;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userAccounts == null) {
			if (other.userAccounts != null)
				return false;
		} else if (!userAccounts.equals(other.userAccounts))
			return false;
		if (userId != other.userId)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", userAccounts="
				+ userAccounts + ", currentAccount=" + currentAccount + "]";
	}

}
