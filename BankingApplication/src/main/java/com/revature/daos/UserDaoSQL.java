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

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDaoSQL implements UserDao {

	private Logger log = Logger.getRootLogger();
	ConnectionUtil connectionUtil = ConnectionUtil.instance;

	User extractUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("user_id");
		String rsUsername = rs.getString("username");
		String rsPassword = rs.getString("password");
		return new User(id, rsUsername, rsPassword);
	}

	@Override
	public int save(User u) {

		try {
			Connection c = connectionUtil.getConnection();

			CallableStatement cs = c.prepareCall("CALL regist_user(?, ?, ?)");
			cs.setString(1, u.getUserName());
			cs.setString(2, u.getPassword());
			
			cs.registerOutParameter(3, Types.INTEGER);
			cs.execute();
			
			int result = cs.getInt(3);
			log.trace("\n\nGenerated Id for UserName is: " + result);
			return result;
			
			/*	Changed to Stored Procedure
			
			String sql = "INSERT INTO bank_users (user_id, username, password)"
					+ "VALUES (BANK_USERS_ID_SEQ.NEXTVAL, ?, ?)";
			
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, u.getUserName());
			ps.setString(2, u.getPassword());

			int result = ps.executeUpdate();
			log.trace("Insert result: " + result);

			log.trace("Getting user_id back from the DB");
			sql = "SELECT user_id FROM bank_users WHERE username = ?";
			ps = c.prepareStatement(sql);
			ps.setString(1, u.getUserName());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getInt("user_id");
				log.trace("User found: " + result);
				return result;
			} else {
				log.debug("User not found");
				return 0;
			}
			*/

		} catch (SQLException e) {
			log.debug("connection failed");
			// e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<User> findAll() {
		log.debug("attempting to find all users");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_users ORDER BY user_id";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}

			return users;

		} catch (SQLException e) {
			log.debug("connection failed");
			// e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findById() {
		return null;
	}

	@Override
	public User findByUsername(String username) {
		log.trace("find a user by just username, not to be used for login");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_users " + "WHERE username = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				User newUser = extractUser(rs);
				if (rs.getString("permis").equals("Admin")) {
					newUser.setPermission(true);
				}
				return newUser;
			} else {
				return null;
			}

		} catch (Exception e) {

			log.debug("connection failed");
		}
		return null;
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		log.trace("attempting to find user credentials");
		try {
			Connection c = connectionUtil.getConnection();

			String sql = "SELECT * FROM bank_users " + "WHERE username = ? AND password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				User newUser = extractUser(rs);
				log.trace("Permission: " + rs.getString("permis"));
				if (rs.getString("permis").equals("Admin")) {
					newUser.setPermission(true);
				}
				return newUser;
			} else {
				return null;
			}

		} catch (Exception e) {

			log.debug("connection failed");

		}
		return null;
	}
}
