package com.revature.daos;

public interface ClickerDao {
	
	ClickerDao currentImplementation = new ClickerDaoSQL();
	
	int createUser (int userId, int amount);
	
	boolean getUserById (int userId);
	
	int[] extractUser(int userId);
	
	void transaction (int userId, int amount);
	
	Thread automated(int userId, int amount, int upgrade);
	
	void upgrade (int userId, int upgrade);
	
	void viewFarmTransaction(int userId);
	
	void viewClickTransaction(int userId);
}
