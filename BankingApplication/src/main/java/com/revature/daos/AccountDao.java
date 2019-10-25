package com.revature.daos;

import com.revature.models.Account;
import com.revature.models.User;

public interface AccountDao {
	AccountDao currentImplementation = null;
	
	int getBalance();
	
	int save(Account acc);
	
	User findById();
	
	User findByAccountname(String accountname);
	
	int Transaction(int amount);
}
