package com.revature.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AccountDaoSQL implements AccountDao {

	private Logger log = Logger.getRootLogger();
	ConnectionUtil connectionUtil = ConnectionUtil.instance;

	Account extractAccount(ResultSet rs) throws SQLException {
		int id = rs.getInt("account_id");
		int owner = rs.getInt("user_id");
		int balance = rs.getInt("balance");
		return new Account(id, owner, balance);
	}

	@Override
	public int getBalanceById(int account_id) {

		return 0;
	}

	@Override
	public int save(Account acc) {

		try {
			Connection c = connectionUtil.getConnection();

			CallableStatement cs = c.prepareCall("CALL regist_account(?, ?)");
			cs.setInt(1, acc.getUserId());

			cs.registerOutParameter(2, Types.INTEGER);
			cs.execute();

			int result = cs.getInt(2);
			System.out.println("\n\nGenerated Id for account is: " + result);
			return result;

		} catch (SQLException e) {
			log.debug("connection failed");
			// e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int save(int user_id) {

		try {
			Connection c = connectionUtil.getConnection();

			CallableStatement cs = c.prepareCall("CALL regist_account(?, ?)");
			cs.setInt(1, user_id);

			cs.registerOutParameter(2, Types.INTEGER);
			cs.execute();

			int result = cs.getInt(2);
			System.out.println("\n\nGenerated Id for account is: " + result);
			return result;

		} catch (SQLException e) {
			log.debug("connection failed");
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Account findById(int acc_id) {

		return null;
	}

	public List<Account> findAll() {
		log.trace("attempting to find all accounts");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_accounts ORDER BY account_id";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<Account> accounts = new ArrayList<>();
			while (rs.next()) {
				accounts.add(extractAccount(rs));
			}

			return accounts;

		} catch (SQLException e) {
			log.debug("connection failed");
			// e.printStackTrace();
			return null;
		}
	}

	public List<Account> findByUserId(int user_id) {
		log.trace("attempting to find accounts under user id = " + user_id);
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_accounts" + " WHERE user_id = ?" + " ORDER BY account_id";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user_id);

			ResultSet rs = ps.executeQuery();
			List<Account> accounts = new ArrayList<>();
			while (rs.next()) {
				log.trace("adding account: " + rs.getInt("account_id"));
				accounts.add(extractAccount(rs));
			}

			return accounts;

		} catch (SQLException e) {
			log.debug("connection failed");
			// e.printStackTrace();
			return null;
		}
	}

	public void findAllWithNames() {
		log.debug("attempting to find all accounts");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_accounts " + "LEFT JOIN bank_users USING (user_id) "
					+ "ORDER BY account_id";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			System.out.println("[ Account ID | Owner | Balance ]");
			while (rs.next()) {
				System.out.print("[ " + rs.getInt("account_id") + " | ");
				System.out.print(rs.getString("username") + " | ");
				System.out.println(rs.getInt("balance") + " ]");
			}

		} catch (SQLException e) {
			log.debug("connection failed");
			e.printStackTrace();
		}
	}

	@Override
	public int transaction(int acc_id, int amount) {

		try {
			Connection c = connectionUtil.getConnection();

			CallableStatement cs = c.prepareCall("CALL regist_transaction(?, ?, ?)");
			cs.setInt(1, acc_id);
			cs.setInt(2, amount);

			cs.registerOutParameter(3, Types.INTEGER);
			cs.execute();

			int result = cs.getInt(3);
			System.out.println("\n\nGenerated Id for transcation is: " + result);
			return result;

		} catch (SQLException e) {
			log.debug("connection failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public void viewAccountTransactions (int acc_id) {
		log.trace("attempting to find all transaction on this account");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_accounts "
					+ "LEFT JOIN transactions USING (account_id) "
					+ "WHERE account_id = ?"
					+ "ORDER BY transaction_id DESC";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, acc_id);

			ResultSet rs = ps.executeQuery();
			System.out.println("[ Transaction ID | Amount | Running Balance ]");
			while (rs.next()) {
				System.out.print("[ " + rs.getInt("transaction_id") + " | ");
				System.out.print(rs.getInt("amount") + " | ");
				System.out.println(rs.getInt("runningbalance") + " ]");
			}

		} catch (SQLException e) {
			log.debug("connection failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public void viewAllTransactions () {
		log.trace("attempting to find all transaction");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_accounts " + "LEFT JOIN bank_users USING (user_id) "
					+ "LEFT JOIN transactions USING (account_id) "
					+ "ORDER BY transaction_id DESC";
			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			System.out.println("[ Account ID | Owner | Amount | Running Balance ]");
			while (rs.next()) {
				System.out.print("[ " + rs.getInt("account_id") + " | ");
				System.out.print(rs.getString("username") + " | ");
				System.out.print(rs.getInt("amount") + " | ");
				System.out.println(rs.getInt("runningbalance") + " ]");
			}

		} catch (SQLException e) {
			log.debug("connection failed");
			e.printStackTrace();
		}
	}

}
