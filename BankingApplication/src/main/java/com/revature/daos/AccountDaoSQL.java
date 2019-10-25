package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AccountDaoSQL implements AccountDao {

	private Logger log = Logger.getRootLogger();

	Account extractUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("account_id");
		return null;
	}

	@Override
	public int getBalance() {
		
		return 0;
	}

	@Override
	public int save(Account u) {
		return 0;
	}

	@Override
	public User findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByAccountname(String accountname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int Transaction(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

}
