package com.sorpresario;

import java.util.ArrayList;

public class SorpresarioStoreData {

	private static final Object lock = new Object();
	private static volatile SorpresarioStoreData instance;
	private ArrayList<SorpresarioFileModel> statistiche;

	public static SorpresarioStoreData getInstance() {
		SorpresarioStoreData r = instance;
		if (r == null) {
			synchronized (lock) { // While we were waiting for the lock, another
				r = instance; // thread may have instantiated the object.
				if (r == null) {
					r = new SorpresarioStoreData();
					instance = r;
				}
			}
		}
		return r;
	}

	public SorpresarioStoreData() {
		statistiche = new ArrayList<SorpresarioFileModel>();
	}
	
	public void addStatistica(SorpresarioFileModel e){
		statistiche.add(e);
	}
	
	public ArrayList<SorpresarioFileModel> getStatistiche(){
		return statistiche;
	}
	
	public void resetStatistiche(){
		statistiche.clear();
	}
	
}
