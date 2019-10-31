package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.revature.util.ClickerConnectionUtil;
import com.revature.util.ConnectionUtil;

public class ClickerDaoSQL implements ClickerDao {

	private Logger log = Logger.getRootLogger();
	ConnectionUtil connectionUtil = ConnectionUtil.instance;
	ClickerConnectionUtil ccu = ClickerConnectionUtil.instance;

	@Override
	public int createUser(int userId, int amount) {
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "INSERT INTO potato_users (user_id, balance) VALUES (?, ?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, amount);

			int result = ps.executeUpdate();
			log.trace("Insert result: " + result);
			c.commit();
			return result;

		} catch (SQLException e) {
			log.debug("Request Failed");
			e.printStackTrace();
			try {
				log.debug("Attempting to rollback");
				connectionUtil.getConnection().rollback();
				return 0;
			} catch (SQLException e1) {
				log.debug("Connection failed");
				e1.printStackTrace();
				return 0;
			}
		}
	}

	@Override
	public boolean getUserById(int userId) {
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM potato_users WHERE user_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.debug("connection failed");
		}

		return false;
	}

	@Override
	public int[] extractUser(int userId) {
		int[] returnArray = new int[2];
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM potato_users WHERE user_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				returnArray[0] = rs.getInt("balance");
				returnArray[1] = rs.getInt("upgrades");
			} else {
				log.debug("User does not exist?");
			}

		} catch (Exception e) {
			log.debug("connection failed");
		}

		return returnArray;
	}

	@Override
	public void transaction(int userId, int amount) {
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "INSERT INTO potato_transactions (pot_tr_id, user_id, amount, type) "
					+ "VALUES (potato_transactions_id_seq.NEXTVAL, ?, ?, 1)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, amount);

			int result = ps.executeUpdate();
			log.trace("Insert result: " + result);
			c.commit();

		} catch (SQLException e) {
			log.debug("Request Failed");
			e.printStackTrace();
			try {
				log.debug("Attempting to rollback");
				connectionUtil.getConnection().rollback();
			} catch (SQLException e1) {
				log.debug("Connection failed");
				e1.printStackTrace();
			}
		}
	}

	@Override
	public Thread automated(int userId, int amount, int upgrade) {
		Thread potatoThread = new Thread(() -> {
			log.info("Thread starts");
			int conIndex = -1;
			try {
				conIndex = ccu.getConnection();
				if (conIndex < 0) {
					log.trace("All connections are currently occupied.");
				} else {
					Connection c = ccu.getCurConnection();
					
					int i = 0;
					while (i < 100) {
						try {
							String sql = "INSERT INTO potato_transactions (pot_tr_id, user_id, amount, type) "
									+ "VALUES (potato_transactions_id_seq.NEXTVAL, ?, ?, 2)";
	
							PreparedStatement ps = c.prepareStatement(sql);
							ps.setInt(1, userId);
							ps.setInt(2, (int) Math.floor(amount/50*upgrade));
	
							int result = ps.executeUpdate();
							log.trace("Insert result: " + result);
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (SQLException e2) {
							log.debug("Connection failed");
							e2.printStackTrace();
						}
						i++;
					}
				}
			} finally {
				ccu.returnConnection(conIndex);
			}

			log.info("Thread ended");
		});
		return potatoThread;
	}

	@Override
	public void upgrade(int userId, int upgrade) {
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "UPDATE potato_users SET upgrades = ? WHERE user_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, (upgrade + 1));
			ps.setInt(2, userId);

			int result = ps.executeUpdate();
			log.trace("Insert result: " + result);
			c.commit();

		} catch (SQLException e) {
			log.debug("Request Failed");
			e.printStackTrace();
			try {
				log.debug("Attempting to rollback");
				connectionUtil.getConnection().rollback();
			} catch (SQLException e1) {
				log.debug("Connection failed");
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void viewFarmTransaction(int userId) {

	}

	@Override
	public void viewClickTransaction(int userId) {

	}
}
