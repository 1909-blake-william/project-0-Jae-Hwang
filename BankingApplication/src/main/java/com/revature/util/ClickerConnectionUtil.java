package com.revature.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ClickerConnectionUtil {

	public static ClickerConnectionUtil instance;
	public static boolean isStarted;

	public static ConnectionUtil connectionUtil = ConnectionUtil.instance;

	private List<Connection> clickConnections;
	private Connection currentConnection;
	private List<Boolean> currentlyActive;
	private Logger log = Logger.getRootLogger();
	private int activeNum;

	public static boolean start() {
		if (instance == null) {
			instance = new ClickerConnectionUtil();
			return true;
		}
		System.out.println("Fake Logger: Instance may already exists");
		return false;
	}

	public int getConnection() {
		if (activeNum < 10) {
			for (int i = 0; i < clickConnections.size(); i++) {
				if (!currentlyActive.get(i)) {
					log.trace("Connection " + i + ": currently free.");
					try {
						if (clickConnections.get(i).isValid(100)) {
							log.info("Using Connection " + i + " for the this Thread");
							activeNum++;
							currentlyActive.set(i, true);
							currentConnection = clickConnections.get(i);
							return i;
						} else {
							log.debug("Connection " + i + " Disconnected, attempting to reconnect.");
							clickConnections.set(i, ConnectionUtil.connect());
						}
					} catch (SQLException e) {
						log.trace("Connection failed.");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					log.trace("Connection " + i + ": currently busy.");
				}
			}
		}
		log.info("All Connections are busy");
		return -2;
	}

	public Connection getCurConnection() {
		return currentConnection;
	}
	
	public int getActiveNum () {
		return activeNum;
	}

	public void returnConnection(int index) {
		if (index >= 0) {
			currentlyActive.set(index, false);
			activeNum--;
			log.info("Connection " + index + " returned.");
		}
	}

	private ClickerConnectionUtil() {
		try {
			log.trace("Creating 10 connections to ready");

			clickConnections = new ArrayList<Connection>();
			currentlyActive = new ArrayList<Boolean>();
			activeNum = 0;
			isStarted = true;

			clickConnections.add(ConnectionUtil.connect()); // 0
			currentlyActive.add(false);
			log.trace("Connection 0 created.");

			clickConnections.add(ConnectionUtil.connect()); // 1
			currentlyActive.add(false);
			log.trace("Connection 1 created.");

			clickConnections.add(ConnectionUtil.connect()); // 2
			currentlyActive.add(false);
			log.trace("Connection 2 created.");

			clickConnections.add(ConnectionUtil.connect()); // 3
			currentlyActive.add(false);
			log.trace("Connection 3 created.");

			clickConnections.add(ConnectionUtil.connect()); // 4
			currentlyActive.add(false);
			log.trace("Connection 4 created.");

			clickConnections.add(ConnectionUtil.connect()); // 5
			currentlyActive.add(false);
			log.trace("Connection 5 created.");

			clickConnections.add(ConnectionUtil.connect()); // 6
			currentlyActive.add(false);
			log.trace("Connection 6 created.");

			clickConnections.add(ConnectionUtil.connect()); // 7
			currentlyActive.add(false);
			log.trace("Connection 7 created.");

			clickConnections.add(ConnectionUtil.connect()); // 8
			currentlyActive.add(false);
			log.trace("Connection 8 created.");

			clickConnections.add(ConnectionUtil.connect()); // 9
			currentlyActive.add(false);
			log.trace("Connection 9 created.");

		} catch (SQLException e) {
			log.debug("Failed to establish the connections.");
		}
	}
}
