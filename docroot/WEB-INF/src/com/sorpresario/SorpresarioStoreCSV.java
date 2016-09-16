package com.sorpresario;

import java.util.HashMap;

public class SorpresarioStoreCSV {

	private static final Object lock = new Object();
	private static volatile SorpresarioStoreCSV instance;
	private HashMap<String, SorpresarioCSVModel> sorpresarioSurprise;
	private HashMap<String, String> sorpresarioSurpriseProdotti;
	private HashMap<String, String> sorpresarioSurpriseCampagne;
	private HashMap<String, String> sorpresarioSurpriseQrCode;
	private HashMap<String, String> sorpresarioSurpriseCountry;
	private HashMap<String, String> sorpresarioSurpriseFamilies;
	private HashMap<String, String> sorpresarioFamiliesAssortimento;
	private HashMap<String, String> sorpresarioFamiliesCountry;
	private HashMap<String, String> sorpresarioSubCategory;
	private HashMap<String, String> sorpresarioFamiliesImage;
	private HashMap<String, String> sorpresarioSurpriseImage;

	public static SorpresarioStoreCSV getInstance() {
		SorpresarioStoreCSV r = instance;
		if (r == null) {
			synchronized (lock) { // While we were waiting for the lock, another
				r = instance; // thread may have instantiated the object.
				if (r == null) {
					r = new SorpresarioStoreCSV();
					instance = r;
				}
			}
		}
		return r;
	}

	public SorpresarioStoreCSV() {
		sorpresarioSurprise = new HashMap<String, SorpresarioCSVModel>();
		sorpresarioSurpriseProdotti = new HashMap<String, String>();
		sorpresarioSurpriseCampagne = new HashMap<String, String>();
		sorpresarioSurpriseQrCode = new HashMap<String, String>();
		sorpresarioSurpriseCountry = new HashMap<String, String>();
		sorpresarioSurpriseFamilies = new HashMap<String, String>();
		sorpresarioFamiliesAssortimento = new HashMap<String, String>();
		sorpresarioFamiliesCountry = new HashMap<String, String>();
		sorpresarioSubCategory = new HashMap<String, String>();
		sorpresarioFamiliesImage = new HashMap<String, String>();
		sorpresarioSurpriseImage = new HashMap<String, String>();
	}

	public void addSurprise(String key, SorpresarioCSVModel value) {
		sorpresarioSurprise.put(key, value);
	}

	public SorpresarioCSVModel getSurprise(String key) {
		return sorpresarioSurprise.get(key);
	}
	
	public HashMap<String, SorpresarioCSVModel> getSurprises(){
		return sorpresarioSurprise;
	}

	public int sizeSurprise() {
		return sorpresarioSurprise.size();
	}

	public void addProdottoToSurprise(String key, String value) {
		if (getProdottoFromSorpresario(key).trim().isEmpty()) {
			sorpresarioSurpriseProdotti.put(key, value);
		} else {
			sorpresarioSurpriseProdotti.put(key,getProdottoFromSorpresario(key) + "," + value);
		}
	}

	public String getProdottoFromSorpresario(String key) {
		return (sorpresarioSurpriseProdotti.get(key) != null) ? sorpresarioSurpriseProdotti.get(key) : "";
	}

	public int sizeProdottiFromSorpresario() {
		return sorpresarioSurpriseProdotti.size();
	}

	public void addCampagnaToSurprise(String key, String value) {
		if (getCampagnaFromSorpresario(key).trim().isEmpty()) {
			sorpresarioSurpriseCampagne.put(key, value);
		} else {
			sorpresarioSurpriseCampagne.put(key,getCampagnaFromSorpresario(key) + "," + value);
		}
	}

	public String getCampagnaFromSorpresario(String key) {
		return (sorpresarioSurpriseCampagne.get(key) != null) ? sorpresarioSurpriseCampagne.get(key) : "";
	}

	public int sizeCampagnaFromSorpresario() {
		return sorpresarioSurpriseCampagne.size();
	}

	public void addQrCodeToSurprise(String key, String value) {
		if (getQrCodeFromSorpresario(key).trim().isEmpty()) {
			sorpresarioSurpriseQrCode.put(key, value);
		} else {
			sorpresarioSurpriseQrCode.put(key,getQrCodeFromSorpresario(key) + "," + value);
		}
	}
	
	public String getQrCodeFromSorpresario(String key) {
		return (sorpresarioSurpriseQrCode.get(key) != null) ? sorpresarioSurpriseQrCode.get(key) : "";
	}

	public int sizeQrCodeFromSorpresario() {
		return sorpresarioSurpriseQrCode.size();
	}
	
	public void addCountryToSurprise(String key, String value) {
		if (getCountryFromSorpresario(key).trim().isEmpty()) {
			sorpresarioSurpriseCountry.put(key, value);
		} else {
			sorpresarioSurpriseCountry.put(key,getCountryFromSorpresario(key) + "," + value);
		}
	}
	
	public String getCountryFromSorpresario(String key) {
		return (sorpresarioSurpriseCountry.get(key) != null) ? sorpresarioSurpriseCountry.get(key) : "";
	}

	public int sizeCountryFromSorpresario() {
		return sorpresarioSurpriseCountry.size();
	}
	
	public void addFamilyToSurprise(String key, String value) {
		if (getFamilyFromSorpresario(key).trim().isEmpty()) {
			sorpresarioSurpriseFamilies.put(key, value);
		} else {
			sorpresarioSurpriseFamilies.put(key,getFamilyFromSorpresario(key) + "," + value);
		}
	}
	
	public String getFamilyFromSorpresario(String key) {
		return (sorpresarioSurpriseFamilies.get(key) != null) ? sorpresarioSurpriseFamilies.get(key) : "";
	}

	public int sizeFamilyFromSorpresario() {
		return sorpresarioSurpriseFamilies.size();
	}
	
	public void addAssortimentoToFamily(String key, String value) {
		if (getAssortimentoFromFamily(key).trim().isEmpty()) {
			sorpresarioFamiliesAssortimento.put(key, value);
		} else {
			sorpresarioFamiliesAssortimento.put(key,getAssortimentoFromFamily(key) + "," + value);
		}
	}
	
	public String getAssortimentoFromFamily(String key) {
		return (sorpresarioFamiliesAssortimento.get(key) != null) ? sorpresarioFamiliesAssortimento.get(key) : "";
	}

	public int sizeAssortimentoFromFamily() {
		return sorpresarioFamiliesAssortimento.size();
	}
	
	public void addCountryToFamily(String key, String value) {
		if (getCountryFromFamily(key).trim().isEmpty()) {
			sorpresarioFamiliesCountry.put(key, value);
		} else {
			sorpresarioFamiliesCountry.put(key,getCountryFromFamily(key) + "," + value);
		}
	}
	
	public String getCountryFromFamily(String key) {
		return (sorpresarioFamiliesCountry.get(key) != null) ? sorpresarioFamiliesCountry.get(key) : "";
	}

	public int sizeCountryFromFamily() {
		return sorpresarioFamiliesCountry.size();
	}
	
	public void addSubCategoryToFamily(String key, String value) {
		if (getSubCategoryFromFamily(key).trim().isEmpty()) {
			sorpresarioSubCategory.put(key, value);
		} else {
			sorpresarioSubCategory.put(key,getSubCategoryFromFamily(key) + "," + value);
		}
	}
	
	public String getSubCategoryFromFamily(String key) {
		return (sorpresarioSubCategory.get(key) != null) ? sorpresarioSubCategory.get(key) : "";
	}

	public int sizeSubCategoryFromFamily() {
		return sorpresarioSubCategory.size();
	}
	
	//immagini
	public void addImageToSurprise(String key, String value) {
		if (getImageFromSurprise(key).trim().isEmpty()) {
			sorpresarioSurpriseImage.put(key, value);
		} else {
			sorpresarioSurpriseImage.put(key,getImageFromSurprise(key) + "," + value);
		}
	}
	
	public String getImageFromSurprise(String key) {
		return (sorpresarioSurpriseImage.get(key) != null) ? sorpresarioSurpriseImage.get(key) : "";
	}

	public int sizeImageFromSurprise() {
		return sorpresarioSurpriseImage.size();
	}
	

	public void addImageToFamilies(String key, String value) {
		if (getImageFromFamilies(key).trim().isEmpty()) {
			sorpresarioFamiliesImage.put(key, value);
		} else {
			sorpresarioFamiliesImage.put(key,getImageFromFamilies(key) + "," + value);
		}
	}
	
	public String getImageFromFamilies(String key) {
		return (sorpresarioFamiliesImage.get(key) != null) ? sorpresarioFamiliesImage.get(key) : "";
	}

	public int sizeImageFromFamilies() {
		return sorpresarioFamiliesImage.size();
	}
	
	public void reset(){
		sorpresarioSurprise = new HashMap<String, SorpresarioCSVModel>();
		sorpresarioSurpriseProdotti = new HashMap<String, String>();
		sorpresarioSurpriseCampagne = new HashMap<String, String>();
		sorpresarioSurpriseQrCode = new HashMap<String, String>();
		sorpresarioSurpriseCountry = new HashMap<String, String>();
		sorpresarioSurpriseFamilies = new HashMap<String, String>();
		sorpresarioFamiliesImage = new HashMap<String, String>();
		sorpresarioSurpriseImage = new HashMap<String, String>();
	}
}
