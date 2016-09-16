package com.sorpresario;

import java.util.ArrayList;

public class SorpresarioSurpriseModel {

	private SorpresarioFamiliesModel families;
	private String nome;
	private String idSurprise;
	private ArrayList<String> prodotti;
	private ArrayList<String> anni;
	private ArrayList<String> qrcode;
	private ArrayList<String> country;
	private String thumbnail;
	private String highFotoritocco;
	private String lowFotoritoccoJpg;
	
	public SorpresarioSurpriseModel() {
		// TODO Auto-generated constructor stub
	}	
	
	public SorpresarioSurpriseModel(SorpresarioFamiliesModel families,
			String nome, String idSurprise, ArrayList<String> prodotti,
			ArrayList<String> anni, ArrayList<String> qrcode,
			ArrayList<String> country, String thumbnail,
			String highFotoritocco, String lowFotoritoccoJpg) {
		super();
		this.families = families;
		this.nome = nome;
		this.idSurprise = idSurprise;
		this.prodotti = prodotti;
		this.anni = anni;
		this.qrcode = qrcode;
		this.country = country;
		this.thumbnail = thumbnail;
		this.highFotoritocco = highFotoritocco;
		this.lowFotoritoccoJpg = lowFotoritoccoJpg;
	}
	public SorpresarioFamiliesModel getFamilies() {
		return families;
	}
	public void setFamilies(SorpresarioFamiliesModel families) {
		this.families = families;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIdSurprise() {
		return idSurprise;
	}
	public void setIdSurprise(String idSurprise) {
		this.idSurprise = idSurprise;
	}
	public ArrayList<String> getProdotti() {
		return prodotti;
	}
	public void setProdotti(ArrayList<String> prodotti) {
		this.prodotti = prodotti;
	}
	public ArrayList<String> getAnni() {
		return anni;
	}
	public void setAnni(ArrayList<String> anni) {
		this.anni = anni;
	}
	public ArrayList<String> getQrcode() {
		return qrcode;
	}
	public void setQrcode(ArrayList<String> qrcode) {
		this.qrcode = qrcode;
	}
	public ArrayList<String> getCountry() {
		return country;
	}
	public void setCountry(ArrayList<String> country) {
		this.country = country;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getHighFotoritocco() {
		return highFotoritocco;
	}
	public void setHighFotoritocco(String highFotoritocco) {
		this.highFotoritocco = highFotoritocco;
	}
	public String getLowFotoritoccoJpg() {
		return lowFotoritoccoJpg;
	}
	public void setLowFotoritoccoJpg(String lowFotoritoccoJpg) {
		this.lowFotoritoccoJpg = lowFotoritoccoJpg;
	}
	
	
}
