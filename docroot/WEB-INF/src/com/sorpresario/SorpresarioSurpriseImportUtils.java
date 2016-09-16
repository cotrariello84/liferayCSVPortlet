package com.sorpresario;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;

public class SorpresarioSurpriseImportUtils {

	private static Log _log = LogFactoryUtil.getLog(SorpresarioSurpriseImportUtils.class);

	public static boolean ImportJournalArticle(SorpresarioCSVModel f,ActionRequest actionRequest, ActionResponse response) {

		String data = f.getId_surprise();

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();

		/*
		 * CONTROLLO LA PRESENZA DI UN CAMPO CUSTOM_FIELDS NELLA TABELLA
		 * EXPANDOTABLE DI LIFERAY
		 */
		DynamicQuery queryTable = DynamicQueryFactoryUtil.forClass(ExpandoTable.class, PortalClassLoaderUtil.getClassLoader())
				.add(PropertyFactoryUtil.forName("name").eq("CUSTOM_FIELDS"))
				.add(PropertyFactoryUtil.forName("classNameId").eq(GetterUtil.getLong(ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class.getName()))));

		try {
			List<ExpandoTable> queryTableResult = SorpresarioUtils.castList(ExpandoTable.class,	ExpandoTableLocalServiceUtil.dynamicQuery(queryTable));

			if (!queryTableResult.isEmpty()) {
				/*
				 * CONTROLLO LA PRESENZA DI UN CAMPO PLM_IDENTIFY NELLA TABELLA
				 * EXPANDOCOLUMN DI LIFERAY. SI RIFERISCE AL CUSTOM_FIELDS CHE
				 * DEVE ESSERE CREATO DA CMS TRAMITE CONTROL PANEL
				 */
				ExpandoTable et = queryTableResult.get(0);

				DynamicQuery queryColumn = DynamicQueryFactoryUtil.forClass(ExpandoColumn.class,PortalClassLoaderUtil.getClassLoader())
						.add(PropertyFactoryUtil.forName("name").eq("PLM_IDENTIFY"))
						.add(PropertyFactoryUtil.forName("tableId").eq(et.getTableId()));

				List<ExpandoColumn> queryColumnResult = SorpresarioUtils.castList(ExpandoColumn.class,	ExpandoColumnLocalServiceUtil.dynamicQuery(queryColumn));

				if (!queryColumnResult.isEmpty()) {
					/*
					 * IL CUSTOM_FIELDS VIENE UTILIZZATO PER IDENTIFICARE IN
					 * MANIERA UNIVOCA UN JOURNAL ARTICLE. NEL NOSTRO CASO
					 * UTILIZZIAZMO ID_SURPRISE. LA RELAZIONE TRA JOURNAL
					 * ARTICLE E CUSTOM FIELDS VIENE FATTA NELLA TABELLA
					 * EXPANDOVALUE DI LIFERAY. CONTROLLO CHE NELLA TABELLA
					 * EXPANDOVALUE SIA PRESENTE UN RECORD LA CUI COLONNA DATA
					 * CORRISPONDA AL ID_SURPRISE. SE PRESENTE IL RECORD
					 * SIGNIFICA CHE IL JOURNAL ARTICLE è GIA' PRESENTE.
					 */
					ExpandoColumn ec = queryColumnResult.get(0);

					DynamicQuery queryValue = DynamicQueryFactoryUtil.forClass(ExpandoValue.class,	PortalClassLoaderUtil.getClassLoader())
							.add(PropertyFactoryUtil.forName("data").eq(data))
							.add(PropertyFactoryUtil.forName("tableId").eq(et.getTableId()))
							.add(PropertyFactoryUtil.forName("columnId").eq(ec.getColumnId()));

					try {
						List<ExpandoValue> queryValueResult = SorpresarioUtils.castList(ExpandoValue.class,ExpandoValueLocalServiceUtil.dynamicQuery(queryValue));

						ServiceContext serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(),actionRequest);

						String structureID = SorpresarioUtils.getStructureKey("StyleGuide-Surprises");

						String templateID = SorpresarioUtils.getTemplateKey("TPL StyleGuide-Surprises");

						HashMap<Locale, String> titleMap = new HashMap<Locale, String>();
						HashMap<Locale, String> descriptionMap = new HashMap<Locale, String>();

						if (!queryValueResult.isEmpty()) {
							/*
							 * JOURNAL ARTICLE GIA' PRESENTE. AGGIORNO ALCUNI
							 * CAMPI E LE CATEGORIE ASSOCIATE.
							 */
							ExpandoValue ev = queryValueResult.get(queryValueResult.size()-1);

							long classPK = ev.getClassPK();

							JournalArticle ja = JournalArticleLocalServiceUtil.getArticle(classPK);
							SorpresarioUtils.getRelatedAssetsByJournalArticleId(ja);
							titleMap.put(Locale.US, f.getSorpresa());
							descriptionMap.put(Locale.US, f.getSorpresa());
							ja.setTitleMap(titleMap);
							ja.setDescriptionMap(descriptionMap);
							
							if (structureID != null){
								ja.setStructureId(structureID);
							}
							
							if (templateID != null){
								ja.setTemplateId(templateID);
							}

							String[] qrcode = SorpresarioStoreCSV.getInstance().getQrCodeFromSorpresario(f.getId_surprise()).split(",");
							JournalArticle tmpJa = SorpresarioSurpriseContentEdit.contentEdit(ja, f, qrcode);
							ja.setContent(tmpJa.getContent());
														
							ja = JournalArticleLocalServiceUtil.updateArticle(
									ja.getUserId(), groupId,
									ja.getFolderId(), ja.getArticleId(),
									ja.getVersion(), ja.getContent(),
									serviceContext);

							long classNameId = GetterUtil
									.getLong(ClassNameLocalServiceUtil
											.getClassNameId(ExpandoValue.class
													.getName()));
							long tableId = ec.getTableId();
							long columnId = ec.getColumnId();
							String datas = f.getId_surprise();	
							
							ExpandoValueLocalServiceUtil.addValue(classNameId, tableId, columnId,	ja.getId(), datas);
														
							String[] prodotti = SorpresarioStoreCSV.getInstance().getProdottoFromSorpresario(f.getId_surprise()).split(",");
							String[] campagna = SorpresarioStoreCSV.getInstance().getCampagnaFromSorpresario(f.getId_surprise()).split(",");
							String[] country = SorpresarioStoreCSV.getInstance().getCountryFromSorpresario(f.getId_surprise()).split(",");
							String[] famiglie = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getFamilyFromSorpresario(f.getId_surprise()).split(","));
							

							//SEZIONE CATEGORIE SORPRESA					
							for(int i = 0; i< prodotti.length;i++){
								f.setProdotto(prodotti[i]);
								f.setCampagna(campagna[i]);
								try{f.setQr_code(qrcode[i]);}catch(Exception e){}
								try{f.setCountry(country[i]);}catch(Exception e){}
								if(!f.getProdotto().trim().isEmpty() && !f.getCampagna().trim().isEmpty()){
									SorpresarioCategoryImportUtils.ImportCategory(f, ja, actionRequest,	response);
								}
							}
							//SEZIONE FAMIGLIA SORPRESA
							for(String famiglia : famiglie){
								String sorpresa = f.getId_surprise();
								String[] cFam = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getCountryFromFamily(sorpresa+"_"+famiglia).split(","));
								String[] aFam = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getAssortimentoFromFamily(sorpresa+"_"+famiglia).split(","));
								JournalArticle fam = SorpresarioFamiliesUtils.getFamilyByName(actionRequest, famiglia);
								String[] subcat = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getSubCategoryFromFamily(sorpresa+"_"+famiglia).split(","));
								if(fam!=null){
									//la famiglia esiste verifico o aggiungo categorie associate
									for (String ass : aFam) {
										SorpresarioCategoriesFamiliesImport.ImportCategory("StyleGuideAssortimentiFamily", ass, fam, actionRequest, response);										
									}
									for (String con : cFam) {
										SorpresarioCategoriesFamiliesImport.ImportCategory("Country", con, fam, actionRequest, response);										
									}
									SorpresarioFamiliesUtils.joinSurpriseToFamily(actionRequest, ja, fam);
									//controllo subcategory 
									for(String subc : subcat){
										JournalArticle scat = SorpresarioSubcategoriesUtils.getSubcategoryByName(actionRequest, subc);
										if(scat!=null){
											//subcategoria esistente
											SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scat, fam);
										}else{
											JournalArticle scatNew = SorpresarioSubcategoriesUtils.createSubcategory(actionRequest, subc, null);
											SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scatNew, fam);	
										}
									}		
									JournalArticle modFam = SorpresarioFamiliesContentEdit.contentEdit(fam, f, SorpresarioStoreCSV.getInstance().getImageFromFamilies(sorpresa+"_"+famiglia).split(","));
									fam.setContent(modFam.getContent());										
									JournalArticleLocalServiceUtil.updateJournalArticle(fam);							
								}else{
									JournalArticle newFamily = SorpresarioFamiliesUtils.createFamliy(actionRequest, famiglia, null);
									for (String ass : aFam) {
										SorpresarioCategoriesFamiliesImport.ImportCategory("StyleGuideAssortimentiFamily", ass, newFamily, actionRequest, response);										
									}
									for (String con : cFam) {
										SorpresarioCategoriesFamiliesImport.ImportCategory("Country", con, newFamily, actionRequest, response);										
									}
									SorpresarioFamiliesUtils.joinSurpriseToFamily(actionRequest, ja, newFamily);
									//controllo subcategory 
									for(String subc : subcat){
										JournalArticle scat = SorpresarioSubcategoriesUtils.getSubcategoryByName(actionRequest, subc);
										if(scat!=null){
											//subcategoria esistente
											SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scat, fam);
										}else{
											JournalArticle scatNew = SorpresarioSubcategoriesUtils.createSubcategory(actionRequest, subc, null);
											SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scatNew, fam);	
										}
									}
									JournalArticle modFam = SorpresarioFamiliesContentEdit.contentEdit(newFamily, f, SorpresarioStoreCSV.getInstance().getImageFromFamilies(sorpresa+"_"+famiglia).split(","));
									newFamily.setContent(modFam.getContent());	
									JournalArticleLocalServiceUtil.updateJournalArticle(newFamily);
								}
							}							
							//SEZIONE FILE CAD SORPRESA
							
							
						} else {
							/* CREO UN NUOVO JOURNAL ARTICLE */
							try {
								titleMap = new HashMap<Locale, String>();
								descriptionMap = new HashMap<Locale, String>();
								Map<String, byte[]> images = new HashMap<String, byte[]>();
								titleMap.put(Locale.US, f.getSorpresa());
								descriptionMap.put(Locale.US, f.getSorpresa());

								String structureKey = SorpresarioUtils.getStructureKey("StyleGuide-Surprises");

								String templateKey = SorpresarioUtils.getTemplateKey("TPL StyleGuide-Surprises");

								String content = SorpresarioUtils.getXMLStructureContent();

								Date dateImport = new Date();
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(dateImport);

								JournalArticle journalArticle = JournalArticleServiceUtil
										.addArticle(
												groupId,
												0L,
												0L,
												0L,
												"",
												true,
												titleMap,
												descriptionMap,
												content,
												"general",
												structureKey,
												templateKey,
												"",
												0,
												1,
												calendar.get(Calendar.YEAR),
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												calendar.get(Calendar.MONTH),
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.YEAR) + 1,
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												true,
												calendar.get(Calendar.MONTH),
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.YEAR) + 1,
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												true, true, false, "", null,
												images, "", serviceContext);

								long classNameId = GetterUtil.getLong(ClassNameLocalServiceUtil.getClassNameId(ExpandoValue.class.getName()));
								long tableId = ec.getTableId();
								long columnId = ec.getColumnId();
								long classPK = journalArticle.getId();
								String datas = f.getId_surprise();
								
								ExpandoValueLocalServiceUtil.addValue(classNameId, tableId, columnId,	classPK, datas);

								String[] qrcode = SorpresarioStoreCSV.getInstance().getQrCodeFromSorpresario(f.getId_surprise()).split(",");

								JournalArticle tmpJa = SorpresarioSurpriseContentEdit.contentEdit(journalArticle, f, qrcode);

								journalArticle.setContent(tmpJa.getContent());

								JournalArticleLocalServiceUtil.updateJournalArticle(journalArticle);

								String[] prodotti = SorpresarioStoreCSV.getInstance().getProdottoFromSorpresario(f.getId_surprise()).split(",");
								String[] campagna = SorpresarioStoreCSV.getInstance().getCampagnaFromSorpresario(f.getId_surprise()).split(",");
								String[] country = SorpresarioStoreCSV.getInstance().getCountryFromSorpresario(f.getId_surprise()).split(",");

								String[] famiglie = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getFamilyFromSorpresario(f.getId_surprise()).split(","));
								

								//SEZIONE CATEGORIE SORPRESA					
								for(int i = 0; i< prodotti.length;i++){
									f.setProdotto(prodotti[i]);
									f.setCampagna(campagna[i]);
									try{f.setQr_code(qrcode[i]);}catch(Exception e){}
									try{f.setCountry(country[i]);}catch(Exception e){}
									if(!f.getProdotto().trim().isEmpty() && !f.getCampagna().trim().isEmpty()){
										SorpresarioCategoryImportUtils.ImportCategory(f, journalArticle, actionRequest,	response);
									}
								}
								//SEZIONE FAMIGLIA SORPRESA
								for(String famiglia : famiglie){
									String sorpresa = f.getId_surprise();
									String[] cFam = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getCountryFromFamily(sorpresa+"_"+famiglia).split(","));
									String[] aFam = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getAssortimentoFromFamily(sorpresa+"_"+famiglia).split(","));
									JournalArticle fam = SorpresarioFamiliesUtils.getFamilyByName(actionRequest, famiglia);
									String[] subcat = SorpresarioUtils.removeDuplicates(SorpresarioStoreCSV.getInstance().getSubCategoryFromFamily(sorpresa+"_"+famiglia).split(","));
									if(fam!=null){
										//la famiglia esiste verifico o aggiungo categorie associate e immagini
										for (String ass : aFam) {
											SorpresarioCategoriesFamiliesImport.ImportCategory("StyleGuideAssortimentiFamily", ass, fam, actionRequest, response);										
										}
										for (String con : cFam) {
											SorpresarioCategoriesFamiliesImport.ImportCategory("Country", con, fam, actionRequest, response);										
										}
										SorpresarioFamiliesUtils.joinSurpriseToFamily(actionRequest, journalArticle, fam);
										//controllo subcategory 
										for(String subc : subcat){
											JournalArticle scat = SorpresarioSubcategoriesUtils.getSubcategoryByName(actionRequest, subc);
											if(scat!=null){
												//subcategoria esistente
												SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scat, fam);
											}else{
												JournalArticle scatNew = SorpresarioSubcategoriesUtils.createSubcategory(actionRequest, subc, null);
												SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scatNew, fam);	
											}
										}	
										JournalArticle modFam = SorpresarioFamiliesContentEdit.contentEdit(fam, f, SorpresarioStoreCSV.getInstance().getImageFromFamilies(sorpresa+"_"+famiglia).split(","));
										fam.setContent(modFam.getContent());										
										JournalArticleLocalServiceUtil.updateJournalArticle(fam);
									}else{
										JournalArticle newFamily = SorpresarioFamiliesUtils.createFamliy(actionRequest, famiglia, null);
										for (String ass : aFam) {
											SorpresarioCategoriesFamiliesImport.ImportCategory("StyleGuideAssortimentiFamily", ass, newFamily, actionRequest, response);										
										}
										for (String con : cFam) {
											SorpresarioCategoriesFamiliesImport.ImportCategory("Country", con, newFamily, actionRequest, response);										
										}
										SorpresarioFamiliesUtils.joinSurpriseToFamily(actionRequest, journalArticle, newFamily);
										//controllo subcategory 
										for(String subc : subcat){
											JournalArticle scat = SorpresarioSubcategoriesUtils.getSubcategoryByName(actionRequest, subc);
											if(scat!=null){
												//subcategoria esistente
												SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scat, newFamily);
											}else{
												JournalArticle scatNew = SorpresarioSubcategoriesUtils.createSubcategory(actionRequest, subc, null);
												SorpresarioSubcategoriesUtils.joinSubcategoryToFamily(actionRequest, scatNew, newFamily);	
											}
										}
										JournalArticle modFam = SorpresarioFamiliesContentEdit.contentEdit(newFamily, f, SorpresarioStoreCSV.getInstance().getImageFromFamilies(sorpresa+"_"+famiglia).split(","));
										newFamily.setContent(modFam.getContent());	
										JournalArticleLocalServiceUtil.updateJournalArticle(newFamily);
									}
								}
								//SEZIONE FILE CAD SORPRESA								

							} catch (SystemException e1) {
								_log.debug("Problema nella creazione del journal article. \n"+ e1.getMessage());
								_log.error(e1);
							} catch (PortalException e) {
								_log.debug("Problema nella creazione del journal article. \n"+ e.getMessage());
								_log.error(e);
							}

						}
					} catch (SystemException e) {
						e.printStackTrace();
						_log.error(e);
					} catch (PortalException e) {
						e.printStackTrace();
						_log.error(e);
					}
				} else {
					_log.error("Problema nella ricerca del tag PLM_IDENTIFY");
				}

			} else {
				_log.error("Problema nella ricerca del tag CUSTOM_FIELDS");
			}
		} catch (SystemException e1) {
			_log.error(e1);
		}
		return false;
	}
}
