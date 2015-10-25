package com.test;

public interface EventsListener {
	public void failedToObtainConnection(Exception e);
	public void connectionObtainedIn(long milliseconds);
	public void addThreads(int nThreads);
	public int removeThreads(int nThreads);
	public int getTotNumberOfThreads();

}
