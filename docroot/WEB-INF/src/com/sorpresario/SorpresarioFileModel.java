package com.sorpresario;

import java.util.ArrayList;

public class SorpresarioFileModel {
	
	private String nomeFile;
	private ArrayList<SorpresarioCSVModel> righeOK;
	private ArrayList<SorpresarioCSVModel> righeKO;
	private int righeTot;
	
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public ArrayList<SorpresarioCSVModel> getRigheOK() {
		return righeOK;
	}
	public void setRigheOK(ArrayList<SorpresarioCSVModel> righeOK) {
		this.righeOK = righeOK;
	}
	public ArrayList<SorpresarioCSVModel> getRigheKO() {
		return righeKO;
	}
	public void setRigheKO(ArrayList<SorpresarioCSVModel> righeKO) {
		this.righeKO = righeKO;
	}
	public int getRigheTot() {
		return righeTot;
	}
	public void setRigheTot(int righeTot) {
		this.righeTot = righeTot;
	}

}
