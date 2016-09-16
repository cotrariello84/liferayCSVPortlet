package com.sorpresario;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetLinkLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;

public class SorpresarioFamiliesUtils {

	private static Log _log = LogFactoryUtil.getLog(SorpresarioCategoryImportUtils.class);

	public static JournalArticle getFamilyByName(ActionRequest actionRequest,String nome) {
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long groupId = themeDisplay.getScopeGroupId();
		JournalArticle journalArticle = null;
		try {
			journalArticle = JournalArticleLocalServiceUtil.getArticleByUrlTitle(groupId, nome.replace(' ',	'-'));
		} catch (SystemException e) {
			_log.error(e);
			return null;
		} catch (PortalException e) {
			_log.error(e);
			return null;
		}
		return journalArticle;
	}

	public static boolean existLink(long entryId1, long entryId2) {
		DynamicQuery query = DynamicQueryFactoryUtil.forClass(AssetLink.class,PortalClassLoaderUtil.getClassLoader())
				.add(PropertyFactoryUtil.forName("entryId1").eq(entryId1))
				.add(PropertyFactoryUtil.forName("entryId2").eq(entryId2));

		try {
			List<AssetLink> queryResult = SorpresarioUtils.castList(AssetLink.class,AssetLinkLocalServiceUtil.dynamicQuery(query));
			if (!queryResult.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			_log.error(e);
			return false;
		}
		return false;
	}

	public static boolean joinSurpriseToFamily(ActionRequest actionRequest,	JournalArticle surprise, JournalArticle family) {

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		try {
			AssetEntry assetEntrySurprise = AssetEntryLocalServiceUtil.fetchEntry(JournalArticle.class.getName(),surprise.getResourcePrimKey());
			AssetEntry assetEntryFamily = AssetEntryLocalServiceUtil.fetchEntry(JournalArticle.class.getName(),	family.getResourcePrimKey());

			// verifico che non siano già correlati i due article
			if (existLink(assetEntrySurprise.getEntryId(),assetEntryFamily.getEntryId())) {
			} else {
				AssetLinkLocalServiceUtil.addLink(themeDisplay.getUserId(),assetEntrySurprise.getEntryId(),assetEntryFamily.getEntryId(), 0, 0);
			}
		} catch (SystemException e) {
			_log.error(e);
			return false;
		} catch (PortalException e) {
			_log.error(e);
			return false;
		}
		return true;
	}

	public static JournalArticle createFamliy(ActionRequest actionRequest,String title, byte[] image) {

		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(	JournalArticle.class.getName(), actionRequest);
		} catch (PortalException e2) {
			_log.error(e2);
		} catch (SystemException e2) {
			_log.error(e2);
		}

		HashMap<Locale, String> titleMap = new HashMap<Locale, String>();
		HashMap<Locale, String> descriptionMap = new HashMap<Locale, String>();
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();
		JournalArticle journalArticle = null;

		try {
			titleMap = new HashMap<Locale, String>();
			descriptionMap = new HashMap<Locale, String>();
			Map<String, byte[]> images = new HashMap<String, byte[]>();
			images.put(title, image);
			titleMap.put(Locale.US, title);
			descriptionMap.put(Locale.US, title);

			String structureKey = SorpresarioUtils.getStructureKey("StyleGuide-Families");
			String templateKey = SorpresarioUtils.getTemplateKey("TPL StyleGuide-Families");

			String content = SorpresarioUtils.getXMLStructureContentFamily();

			Date dateImport = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateImport);

			journalArticle = JournalArticleServiceUtil.addArticle(groupId, 0L,
					0L, 0L, "", true, titleMap, descriptionMap, content,
					"general", structureKey, templateKey, "", 0, 1,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR),
					calendar.get(Calendar.MINUTE),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.YEAR) + 1,
					calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
					true, calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH),
					calendar.get(Calendar.YEAR) + 1,
					calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
					true, true, false, "", null, images, "", serviceContext);

		} catch (SystemException e1) {
			_log.debug("Problema nella creazione del journal article. \n"+ e1.getMessage());
			_log.error(e1);
		} catch (PortalException e) {
			_log.debug("Problema nella creazione del journal article. \n"+ e.getMessage());
			_log.error(e);
		}
		return journalArticle;
	}
}
