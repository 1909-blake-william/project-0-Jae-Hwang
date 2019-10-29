package com.revature.driver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.revature.util.ConnectionUtil;

import sun.jvm.hotspot.utilities.soql.Callable;

public class TestDriver {

	private static Logger log = Logger.getRootLogger();

	public static void main(String[] args) {

		ConnectionUtil connectionUtil = ConnectionUtil.instance;
		connectionUtil.setConnection();

		try {
			Connection c = connectionUtil.getConnection();
			String sql = "SELECT * FROM bank_users ORDER BY user_id";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			System.out.println("\n- Users");
			System.out.println("user id\t| username\t| password\t| permission");
			System.out.println("-----------------------------------------------------");

			while (rs.next()) {
				System.out.print(rs.getString("user_id") + "   \t| ");
				System.out.print(rs.getString("username") + "   \t| ");
				System.out.print(rs.getString("password") + "   \t| ");
				System.out.print(rs.getString("permis") + "   \t ");
				System.out.println();
			}
			System.out.println("-----------------------------------------------------");
			
			
			sql = "CALL deactivate_account(?, ?)";
			CallableStatement cs = c.prepareCall(sql);
			cs.setInt(1, 2);
			cs.registerOutParameter(2, Types.INTEGER);
			
			cs.execute();
			
			System.out.println(cs.getInt(2));
			
			
		} catch (Exception e) {
			log.debug("connection failed");
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}