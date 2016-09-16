package com.sorpresario;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;

public class SorpresarioCategoriesFamiliesImport {

	private static final Log _log = LogFactoryUtil
			.getLog(SorpresarioCategoriesFamiliesImport.class);

	public static boolean ImportCategory(String vocabolario, String categoria, JournalArticle ja, ActionRequest actionRequest,	ActionResponse response) {

		ServiceContext serviceContext = null;

		try {
			serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), actionRequest);
		} catch (PortalException e3) {
			_log.error(e3);
		} catch (SystemException e3) {
			_log.error(e3);
		}
		
		try {

			AssetVocabulary avc = SorpresarioCategoryUtils.existVocabulary(	actionRequest, response, vocabolario);

			if (avc == null)
				avc = AssetVocabularyLocalServiceUtil.addVocabulary(ja.getUserId(), vocabolario, serviceContext);

			AssetCategory ac = SorpresarioCategoryUtils.existCategory(actionRequest, response, avc, categoria);

			if (ac == null)
				ac = AssetCategoryLocalServiceUtil.addCategory(ja.getUserId(),categoria, avc.getVocabularyId(), serviceContext);

			AssetEntry ae = null;
			try {
				ae = AssetEntryLocalServiceUtil.getEntry(JournalArticle.class.getName(),ja.getResourcePrimKey());

			} catch (PortalException e1) {
				_log.error(e1);
			} catch (SystemException e1) {
				_log.error(e1);
			}

			long categoryId = ac.getCategoryId();
			long entryId = ae.getEntryId();
			List<AssetEntry> listAssetEntry = null;

			try {
				listAssetEntry = SorpresarioUtils.castList(AssetEntry.class,AssetEntryLocalServiceUtil.getAssetCategoryAssetEntries(categoryId));
			} catch (SystemException e1) {
				_log.error(e1);
			}
			int c = 0;
			for (AssetEntry assetEntry : listAssetEntry) {
				if (assetEntry.getEntryId() == entryId)
					c++;
			}
			if (c == 0) {
				try {
					AssetEntryLocalServiceUtil.addAssetCategoryAssetEntry(categoryId, entryId);
				} catch (SystemException e) {
					_log.error(e);
				}
			}
		} catch (PortalException e2) {
			_log.error(e2);
		} catch (SystemException e2) {
			_log.error(e2);
		}
		
		return false;

	}
}
