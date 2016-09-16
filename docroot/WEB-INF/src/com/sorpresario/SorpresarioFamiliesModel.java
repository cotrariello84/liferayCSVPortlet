package com.sorpresario;

import java.util.ArrayList;

public class SorpresarioFamiliesModel {
	
	private SorpresarioCategoriesModel categories;
	private String nome;
	private ArrayList<String> country;
	private ArrayList<String> assortimenti;
	private String thumbnail;
	private String leafletImage;
	private String leafletImageHigh;
	private String highResolutionLeaflet;
	private String assetZip;
	private String familyDescription;

	public SorpresarioFamiliesModel() {
	}
	
	public SorpresarioFamiliesModel(SorpresarioCategoriesModel categories,
			String nome, ArrayList<String> country,
			ArrayList<String> assortimenti, String thumbnail,
			String leafletImage, String leafletImageHigh,
			String highResolutionLeaflet, String assetZip,
			String familyDescription) {
		super();
		this.categories = categories;
		this.nome = nome;
		this.country = country;
		this.assortimenti = assortimenti;
		this.thumbnail = thumbnail;
		this.leafletImage = leafletImage;
		this.leafletImageHigh = leafletImageHigh;
		this.highResolutionLeaflet = highResolutionLeaflet;
		this.assetZip = assetZip;
		this.familyDescription = familyDescription;
	}
	
	public SorpresarioCategoriesModel getCategories() {
		return categories;
	}

	public void setCategories(SorpresarioCategoriesModel categories) {
		this.categories = categories;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<String> getCountry() {
		return country;
	}

	public void setCountry(ArrayList<String> country) {
		this.country = country;
	}

	public ArrayList<String> getAssortimenti() {
		return assortimenti;
	}

	public void setAssortimenti(ArrayList<String> assortimenti) {
		this.assortimenti = assortimenti;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLeafletImage() {
		return leafletImage;
	}

	public void setLeafletImage(String leafletImage) {
		this.leafletImage = leafletImage;
	}

	public String getLeafletImageHigh() {
		return leafletImageHigh;
	}

	public void setLeafletImageHigh(String leafletImageHigh) {
		this.leafletImageHigh = leafletImageHigh;
	}

	public String getHighResolutionLeaflet() {
		return highResolutionLeaflet;
	}

	public void setHighResolutionLeaflet(String highResolutionLeaflet) {
		this.highResolutionLeaflet = highResolutionLeaflet;
	}

	public String getAssetZip() {
		return assetZip;
	}

	public void setAssetZip(String assetZip) {
		this.assetZip = assetZip;
	}

	public String getFamilyDescription() {
		return familyDescription;
	}

	public void setFamilyDescription(String familyDescription) {
		this.familyDescription = familyDescription;
	}
}
