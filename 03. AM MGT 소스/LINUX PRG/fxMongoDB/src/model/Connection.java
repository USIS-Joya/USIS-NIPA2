package model;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class Connection {
	public String MongoDB_IP = "127.0.0.1";
	public int MongoDB_PORT = 27017;
	public DB MongoDB_DB = null;
	public String MongoDB_USR = "user";
	public String MongoDB_PWD = "usis";

	public void setIP(String IP) {
		MongoDB_IP = IP;
	}

	public String getIP() {
		return MongoDB_IP;
	}

	public void setPort(int Port) {
		MongoDB_PORT = Port;
	}

	public int getPort() {
		return MongoDB_PORT;
	}

	public void setDB(DB db) {
		MongoDB_DB = db;
	}

	public DB getDB() {
		return MongoDB_DB;
	}
	public void setUSR(String USR) {
		MongoDB_USR = USR;
	}

	public String getUSR() {
		return MongoDB_USR;
	}
	public void setPWD(String PWD) {
		MongoDB_PWD = PWD;
	}

	public String getPWD() {
		return MongoDB_PWD;
	}

	@SuppressWarnings("deprecation")
	public void DBConn() {
		try {
			Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
			mongoLogger.setLevel(Level.SEVERE);
			@SuppressWarnings("resource")
			MongoClient mgClient = new MongoClient(new ServerAddress(getIP(), getPort()));
			MongoDB_DB = mgClient.getDB("test");
			System.out.println("mongoDB Connect");
			
		} catch (Exception e) {
			
			System.out.println("mongoDB Disconnect");
			e.printStackTrace();
		}
	}
}
