package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.openejb.resource.jdbc.dbcp.BasicManagedDataSource;

public class ThreadsController extends Thread implements EventsListener {
	private final int initialNumberOfThreads=1;
	private boolean active=true;
	// Emulator[] emulators = new Emulator[initialNumberOfThreads];
	List<Emulator> emulators = Collections.synchronizedList(new ArrayList<Emulator>());
	private int connectionsObtained=0;
	private double averageConnectionTime=0;
	private long quickestConnectionTime=100000;
	private long slowestConnectionTime=-1;
	private int connectionFailures=0;

	long myId = -1;
	
	public ThreadsController(Object monitor, long id) {
		myId=id;
	}
	
	@Override
	public void run() {
		for (int i=0; i<initialNumberOfThreads; i++) {
			Emulator emulator = new Emulator(this);
			emulators.add(emulator);
			emulator.start();
		}
		while (active) {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// do nothing
			}
			printStatistics();
		}
	}

	private void printStatistics() {
		System.out.println("Number of threads:"+emulators.size()+" # Conn. obtained:"+connectionsObtained+" # Conn. failures:"+connectionFailures+" # Average connection time:"+String.format("%.2f", averageConnectionTime)+" # Quickest conn. time:"+quickestConnectionTime+" # Slowest conn. time:"+slowestConnectionTime);
	}

	public long getMyId() {
		return myId;
	}
	
	public void stopAsap() {
		for (Emulator el: emulators) {
			el.stopAsap();
		}
		active=false;
	}

	@Override
	public void failedToObtainConnection(Exception e) {
		connectionFailures++;
	}

	@Override
	synchronized public void connectionObtainedIn(long connectionTime) {		
		averageConnectionTime = ((averageConnectionTime*connectionsObtained)+connectionTime)/(connectionsObtained+1);				
		connectionsObtained++;
		if (slowestConnectionTime < connectionTime) slowestConnectionTime=connectionTime;
		if (quickestConnectionTime > connectionTime) quickestConnectionTime=connectionTime;
		
	}

	@Override
	public void addThreads(int nThreads) {
		for (int i=0; i<nThreads; i++) {
			Emulator emulator = new Emulator(this);
			emulators.add(emulator);
			emulator.start();
		}
		
	}

	@Override
	public int removeThreads(int nThreads) {
		int succesfullyRemoved=0;
		for (int i=0; i<nThreads; i++) {
			if (!emulators.isEmpty()) {
				Emulator el = emulators.remove(0);
				el.stopAsap();
				succesfullyRemoved++;
			}
		}
		return succesfullyRemoved;
	}

	@Override
	public int getTotNumberOfThreads() {
		return emulators.size();
		
	}


}
