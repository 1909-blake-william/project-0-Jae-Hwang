package com.revature.driver;

import java.sql.Connection;
import java.sql.SQLException;

import com.revature.util.ConnectionUtil;

public class ConnectionTest {

	public static void main(String[] args) {
		Connection c = null;
		while (true) {
			for (int i = 0; i < 15; ++i)
				System.out.println();
			try {
				if (c == null || !c.isValid(1000)) {
					c = ConnectionUtil.connect();
				}
				System.out.println("Connected");
			} catch (SQLException e1) {
				System.out.println("Not connected");
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
