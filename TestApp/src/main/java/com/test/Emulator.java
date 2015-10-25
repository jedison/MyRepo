package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public class Emulator extends Thread {

	private boolean active=true;
	DataSource ds = null;
	// TransactionManager tm = null;
	private EventsListener eventsListener=null;
	
	public Emulator(EventsListener eventsListener) {
		this.eventsListener=eventsListener;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource)envCtx.lookup("myDBXAPooled");
			// System.out.println("DS:"+ds.getClass());
			// tm = (TransactionManager)initCtx.lookup("java:comp/TransactionManager");
			
		} catch (Exception e) {
			
			System.err.println("Error getting datasource");
			e.printStackTrace();
			
		}
	}

	@Override
	public void run() {
		while (active && ds!=null) {
			doAction();
			try {
				Thread.sleep(Helper.showRandomInteger(3000, 4500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doAction() {
		long start = System.currentTimeMillis();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = -1;
		long endGetConnection = -1;

		try {
			try {
				conn = ds.getConnection();
			} catch (Exception e) {
				eventsListener.failedToObtainConnection(e);
				// e.printStackTrace();
			}
			if (conn!=null) {
				endGetConnection = System.currentTimeMillis();
				ps = conn.prepareStatement("INSERT INTO MY_TABLE (name) VALUES(?)");
				ps.setString(1, (new Long(System.currentTimeMillis())).toString());
				ps.execute();

				eventsListener.connectionObtainedIn(System.currentTimeMillis()-endGetConnection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, ps, rs);
			
		}

	}

	private void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs!=null) rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (ps!=null) ps.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (conn!=null) conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void stopAsap() {
		active=false;
	}

}
