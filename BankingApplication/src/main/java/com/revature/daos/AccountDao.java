package com.revature.daos;

import java.util.List;

import com.revature.models.Account;

public interface AccountDao {
	AccountDao currentImplementation = new AccountDaoSQL();
	
	int getBalanceById(int account_id);
	
	int save(Account acc);
	
	int save(int acc_id);
	
	Account findById(int acc_id);

	List<Account> findAll();
	
	List<Account> findByUserId(int user_id);
	
	void findAllWithNames();
	
	int transaction(int acc_id, int amount);
	
	void viewAccountTransactions(int acc_id);
	
	void viewAllTransactions();
}
