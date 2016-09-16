package com.sorpresario;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.opencsv.CSVReader;

public class SorpresarioAnalyzeCSV {

	public static List<SorpresarioCSVModel> listaFoo = new ArrayList<SorpresarioCSVModel>();

	public static ArrayList<SorpresarioCSVModel> listaOK = new ArrayList<SorpresarioCSVModel>();
	public static ArrayList<SorpresarioCSVModel> listaKO = new ArrayList<SorpresarioCSVModel>();

	private static Log _log = LogFactoryUtil.getLog(SorpresarioAnalyzeCSV.class);

	public static String CSVRead(String path, ActionRequest actionRequest,	ActionResponse response) {

		String pathFile = path;
		System.out.println(path);
		File file = new File(pathFile);
		System.out.println(file.getName());
		final int ID_SURPRISE = 0, BUCKET = 1, SHELL_COLOR = 2, LANGUAGE_CODE = 3, LANGUAGE = 4, MARKET_UNIT = 5, COUNTRY_CODE = 6, COUNTRY = 7, SORPRESA = 8, FAMIGLIA = 9, MPG_CODE = 10, QR_CODE = 11, CRE_LIC = 12, CAMPAGNA = 13, PRODOTTO = 14, FORMATO = 15, ASSORTIMENTO = 16, MS_HL = 17, TIPO = 18, SESSIONE = 19, SUBCATEGOR = 20, TEC_TYPE = 21, DOC_LEAFLET_FRONT = 22, DOC_THUMBNAIL = 23, DOC_LEAFLET_BACK = 24, DOC_LEAFLET_FRONT_LOW = 25, DOC_LEAFLET_BACK_LOW = 26;
		if (file != null && file.canRead() && file.isFile() && file.exists()) {
			try {
				/*
				 * Utilizzo ArrayList per tenere traccia delle righe ok e ko. 
				 * per righe ok si intendono quelle righe le cui celle sono tutte valorizzate; 
				 * ler righe ko sono quelle righe che presentano delle celle vuote.
				 * */
				listaOK = new ArrayList<SorpresarioCSVModel>();
				listaKO = new ArrayList<SorpresarioCSVModel>();
				FileReader fr = new FileReader(file.getAbsolutePath());
				CSVReader reader = new CSVReader(fr, ';', '\'', 2);
				String[] nextLine;
				int counter = 0;
				//Utilizzo un oggetto per memorizzare i dati relativi a nome file e righe ok e ko
				SorpresarioFileModel ss = new SorpresarioFileModel();
				ss.setNomeFile(file.getName());
				while ((nextLine = reader.readNext()) != null) {
					counter++;
					SorpresarioCSVModel f = new SorpresarioCSVModel();
					f.setId_surprise(nextLine[ID_SURPRISE]);
					f.setBucket(nextLine[BUCKET]);
					f.setShell_color(nextLine[SHELL_COLOR]);
					f.setLanguage_code(nextLine[LANGUAGE_CODE]);
					f.setLanguage(nextLine[LANGUAGE]);
					f.setMarket_unit(nextLine[MARKET_UNIT]);
					f.setCountry_code(nextLine[COUNTRY_CODE]);
					f.setCountry(nextLine[COUNTRY]);
					f.setSorpresa(nextLine[SORPRESA]);
					f.setFamiglia(nextLine[FAMIGLIA]);
					f.setMpg_code(nextLine[MPG_CODE]);
					f.setQr_code(nextLine[QR_CODE]);
					f.setCre_lic(nextLine[CRE_LIC]);
					f.setCampagna(nextLine[CAMPAGNA]);
					f.setFormato(nextLine[FORMATO]);
					f.setProdotto(nextLine[PRODOTTO]);
					f.setAssortimento(nextLine[ASSORTIMENTO]);
					f.setMs_hl(nextLine[MS_HL]);
					f.setTipo(nextLine[TIPO]);
					f.setSessione(nextLine[SESSIONE]);
					f.setSubcategor(nextLine[SUBCATEGOR]);
					f.setTec_type(nextLine[TEC_TYPE]);
					f.setDoc_leaflet_front_low(nextLine[DOC_LEAFLET_FRONT]);
					f.setDoc_thumbnail(nextLine[DOC_THUMBNAIL]);
					f.setDoc_leaflet_back(nextLine[DOC_LEAFLET_BACK]);
					f.setDoc_leaflet_front_low(nextLine[DOC_LEAFLET_FRONT_LOW]);
					f.setDoc_leaflet_back_low(nextLine[DOC_LEAFLET_BACK_LOW]);

					String campiMancanti = "";
					if (f.getId_surprise().trim().isEmpty()) {
						campiMancanti += "ID_SURPRISE,";
					}
					if (f.getProdotto().trim().isEmpty()) {
						campiMancanti += " Prodotto,";
					}
					if (f.getCampagna().trim().isEmpty()) {
						campiMancanti += " Campagna,";
					}
					if (f.getSorpresa().trim().isEmpty()) {
						campiMancanti += " Sorpresa,";
					}
					if (f.getFamiglia().trim().isEmpty()) {
						campiMancanti += " Famiglia,";
					}
					if (f.getCountry().trim().isEmpty()) {
						campiMancanti += " Country,";
					}
					if (f.getAssortimento().trim().isEmpty()) {
						campiMancanti += " Assortimento,";
					}
					if (f.getMs_hl().trim().isEmpty()) {
						campiMancanti += " MsHl,";
					}
					if (f.getSubcategor().trim().isEmpty()) {
						campiMancanti += " Subcategory,";
					}
					if (campiMancanti == "") {
						/*
						 * Utilizzo delle strutture dati per la memorizzazione delle informazioni.
						 * viene elaborata ogni singola riga del file csv.
						 * utilizzo delel hasmap per la memorizzazione dei dati.
						 * premesso che più righe possono appartenere alla stessa sorpresa
						 * ogni sorpresa è composta da una famiglia, da un elenco di nazioni, 
						 * un elenco di qrcode, un elenco di prodotti, un elenco di anni
						 * ho preferito creare delle hashmap per ognugno dei suddetti elenchi, 
						 * dando come chiave primaria della hashmap idsurprise
						 * identificativo univoco della sopresa
						 * */
						SorpresarioStoreCSV.getInstance().addCampagnaToSurprise(f.getId_surprise(),f.getCampagna());						
						SorpresarioStoreCSV.getInstance().addProdottoToSurprise(f.getId_surprise(),f.getProdotto());
						SorpresarioStoreCSV.getInstance().addQrCodeToSurprise(f.getId_surprise(), f.getQr_code());
						SorpresarioStoreCSV.getInstance().addSurprise(f.getId_surprise(), f);
						SorpresarioStoreCSV.getInstance().addCountryToSurprise(f.getId_surprise(), f.getCountry());
						String key = f.getId_surprise() + "_" + f.getFamiglia();
						SorpresarioStoreCSV.getInstance().addFamilyToSurprise(f.getId_surprise(), f.getFamiglia());
						SorpresarioStoreCSV.getInstance().addAssortimentoToFamily(key, f.getMs_hl());
						SorpresarioStoreCSV.getInstance().addCountryToFamily(key, f.getAssortimento());
						SorpresarioStoreCSV.getInstance().addSubCategoryToFamily(key, f.getSubcategor());
						SorpresarioStoreCSV.getInstance().addImageToFamilies(key, f.getDoc_thumbnail()+";"+f.getDoc_leaflet_back()+";"+f.getDoc_leaflet_back_low()+";"+f.getDoc_leaflet_front()+";"+f.getDoc_leaflet_front_low());
						SorpresarioStoreCSV.getInstance().addImageToSurprise(key, f.getDoc_thumbnail()+";"+f.getDoc_leaflet_back()+";"+f.getDoc_leaflet_back_low()+";"+f.getDoc_leaflet_front()+";"+f.getDoc_leaflet_front_low());
						String msg = "Record (" + counter+ ") della sorpresa: " + f.getId_surprise()+ " elaborata";
						f.setMsg(msg);
						listaFoo.add(f);
						listaOK.add(f);
					} else {
						String msg = "Record ("+ counter+ ") della sorpresa: "+ f.getId_surprise()+ " non può essere elaborata; manca il valore di: "+ campiMancanti.substring(0,campiMancanti.length() - 1);
						_log.debug(msg);
						f.setMsg(msg);
						listaKO.add(f);
					}
				}
				ss.setRigheOK(listaOK);
				ss.setRigheKO(listaKO);
				ss.setRigheTot(counter);
				SorpresarioStoreData.getInstance().addStatistica(ss);
				reader.close();
			} catch (IOException ioe) {
				_log.debug("Errore nella lettura del file: " + file.getName());
				_log.error(ioe);
				return "Errore nella lettura del file: " + file.getName();
			}
		} else {
			_log.debug("Errore nella lettura del file: " + file.getName());
			return "Errore nella lettura del file: " + file.getName();
		}
		if(listaKO.size()> 0 )
			return "error";
		return "success";
	}
}
