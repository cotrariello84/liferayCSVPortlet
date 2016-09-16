package com.sorpresario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetLinkLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.PwdGenerator;

public class SorpresarioUtils {
	
	private static Log _log = LogFactoryUtil.getLog(SorpresarioUtils.class);
	
	public static Locale getDefaultLanguageSite(long groupId) {
		Locale localeDefault = null;

		try {

			localeDefault = LocaleUtil.getSiteDefault();

		} catch (Exception e) {
			_log.error(e);
		}
		return localeDefault;
	}

	public static String generateInstanceId() {
		StringBuilder instanceId = new StringBuilder(8);
		String key = PwdGenerator.KEY1 + PwdGenerator.KEY2 + PwdGenerator.KEY3;

		for (int i = 0; i < 8; i++) {
			int pos = (int) Math.floor(Math.random() * key.length());
			instanceId.append(key.charAt(pos));
		}
		return instanceId.toString();
	}

	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c)
			r.add(clazz.cast(o));
		return r;
	}

	public static String getStructureKey(String name) {
		DynamicQuery queryStructure = DynamicQueryFactoryUtil.forClass(	DDMStructure.class, PortalClassLoaderUtil.getClassLoader());
		try {
			List<DDMStructure> queryStructureResult = SorpresarioUtils.castList(DDMStructure.class, DDMStructureLocalServiceUtil.dynamicQuery(queryStructure));
			if (!queryStructureResult.isEmpty()) {
				for (DDMStructure ddmStructure : queryStructureResult) {
					if (ddmStructure.getName().contains(name)) {
						return ddmStructure.getStructureKey();
					}
				}
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		return null;

	}

	public static String getTemplateKey(String name) {
		DynamicQuery queryTemplate = DynamicQueryFactoryUtil.forClass(DDMTemplate.class, PortalClassLoaderUtil.getClassLoader());
		try {
			List<DDMTemplate> queryTemplateResult = SorpresarioUtils.castList(DDMTemplate.class,DDMTemplateLocalServiceUtil.dynamicQuery(queryTemplate));
			if (!queryTemplateResult.isEmpty()) {
				for (DDMTemplate ddmTemplate : queryTemplateResult) {
					if (ddmTemplate.getName().contains(name)) {
						return ddmTemplate.getTemplateKey();
					}
				}
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			_log.error(e);
		}
		return null;

	}

	public static long getStructureId(String name) {
		DynamicQuery queryStructure = DynamicQueryFactoryUtil.forClass(DDMStructure.class, PortalClassLoaderUtil.getClassLoader());
		try {
			List<DDMStructure> queryStructureResult = SorpresarioUtils.castList(DDMStructure.class, DDMStructureLocalServiceUtil.dynamicQuery(queryStructure));
			if (!queryStructureResult.isEmpty()) {
				for (DDMStructure ddmStructure : queryStructureResult) {
					if (ddmStructure.getName().contains(name)) {
						return ddmStructure.getStructureId();
					}
				}
			}
		} catch (SystemException e) {
			_log.error(e);			
		}
		return 0;

	}

	public static long getTemplateId(String name) {
		DynamicQuery queryTemplate = DynamicQueryFactoryUtil.forClass(DDMTemplate.class, PortalClassLoaderUtil.getClassLoader());
		try {
			List<DDMTemplate> queryTemplateResult = SorpresarioUtils.castList(DDMTemplate.class,DDMTemplateLocalServiceUtil.dynamicQuery(queryTemplate));
			if (!queryTemplateResult.isEmpty()) {
				for (DDMTemplate ddmTemplate : queryTemplateResult) {
					if (ddmTemplate.getName().contains(name)) {
						return ddmTemplate.getTemplateId();
					}
				}
			}
		} catch (SystemException e) {
			_log.error(e);
		}
		return 0;

	}

	public static String getXMLStructureContent() {
		String content = "<?xml version=\"1.0\"?>"
				+ "<root available-locales=\"en_US\" default-locale=\"en_US\">"
				+ "<dynamic-element name=\"Thumbnail\" index=\"0\" type=\"image\" index-type=\"keyword\">"
				+ "<dynamic-content language-id=\"en_US\"></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"High_Fotoritocco\" index=\"0\" type=\"document_library\" index-type=\"keyword\">"
				+ "		<dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "	</dynamic-element>"
				+ "	<dynamic-element name=\"Low_Fotoritocco_JPG\" index=\"0\" type=\"image\" index-type=\"keyword\">"
				+ "		<dynamic-content language-id=\"en_US\"></dynamic-content>"
				+ "	</dynamic-element>"
				+ "	<dynamic-element name=\"QR_Code\" index=\"0\" type=\"text\" index-type=\"keyword\">"
				+ "		<dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "	</dynamic-element>" + "</root>";
		return content;
	}

	public static String getXMLStructureContentFamily(){
		String content = "<?xml version=\"1.0\"?>"
				+ "<root available-locales=\"en_US\" default-locale=\"en_US\">"
				+ "<dynamic-element name=\"Thumbnail\" index=\"0\" type=\"image\" index-type=\"\">"
				+ "    <dynamic-content language-id=\"en_US\"></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"Family_Description\" index=\"0\" type=\"document_library\" index-type=\"\">"
				+ "    <dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"Leaflet_Image\" index=\"0\" type=\"image\" index-type=\"\">"
				+ "    <dynamic-element name=\"Leaflet_Image_high\" index=\"0\" type=\"image\" index-type=\"\">"
				+ "        <dynamic-content language-id=\"en_US\" id=\"30338\"><![CDATA[]]></dynamic-content>"
				+ "    </dynamic-element>"
				+ "    <dynamic-content language-id=\"en_US\" id=\"30337\"><![CDATA[]]></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"High_Resolution_Leaflet\" index=\"0\" type=\"document_library\" index-type=\"\">"
				+ "    <dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"Asset_ZIP\" index=\"0\" type=\"document_library\" index-type=\"\">"
				+ "    <dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "</dynamic-element></root>";		
		return content;
	}
	
	public static String getXMLStructureContentCategory(){
		String content = "<?xml version=\"1.0\"?>"
				+ "<root available-locales=\"en_US\" default-locale=\"en_US\">"
				+ "<dynamic-element name=\"Thumbnail\" index=\"0\" type=\"image\" index-type=\"keyword\">"
				+ "    <dynamic-content language-id=\"en_US\"></dynamic-content>"
				+ "</dynamic-element>"
				+ "<dynamic-element name=\"Color\" index=\"0\" type=\"text\" index-type=\"keyword\">"
				+ "    <dynamic-content language-id=\"en_US\"><![CDATA[]]></dynamic-content>"
				+ "</dynamic-element>"
				+ "</root>";		
		return content;
	}

	public static List<JournalArticle> getRelatedAssetsByJournalArticleId(JournalArticle journalArticle){
		ArrayList<JournalArticle> journalArticles = new ArrayList<JournalArticle>();
		try {
			AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(JournalArticle.class.getName(), journalArticle.getResourcePrimKey());
			List<AssetLink> assetLink = AssetLinkLocalServiceUtil.getLinks(assetEntry.getEntryId());
			for (AssetLink assetLink2 : assetLink) {
				if(assetEntry.getEntryId()==assetLink2.getEntryId1()){
					AssetEntry ae = AssetEntryLocalServiceUtil.getEntry(assetLink2.getEntryId2());
					journalArticles.add(JournalArticleLocalServiceUtil.fetchLatestIndexableArticle(ae.getClassPK()));
				}
			}
		} catch (SystemException e) {

		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return journalArticles;
	}
	public List<JournalArticle> getRelatedAssetsByJournalArticleId(long articleId){return null;}
	
	public static String[] removeDuplicates(String[] d){
		List<String> al = new ArrayList<String>();
		for(String s : d){
			al.add(s);
		}
		Set<String> hs = new HashSet<String>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		String[] res = new String[al.size()];
		al.toArray(res);
		return res;
	}
}
