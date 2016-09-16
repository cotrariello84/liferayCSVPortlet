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

public class SorpresarioCategoryImportUtils {

	private static final Log _log = LogFactoryUtil
			.getLog(SorpresarioCategoryImportUtils.class);

	public static boolean ImportCategory(SorpresarioCSVModel f,	JournalArticle ja, ActionRequest actionRequest,	ActionResponse response) {

		final String VOCABULARY_CAMPAGNA = "StyleGuideYearFamily";
		final String VOCABULARY_PRODOTTO = "StyleGuideProductFamily";
		final String VOCABULARY_COUNTRY = "Country";
		ServiceContext serviceContext = null;

		try {
			serviceContext = ServiceContextFactory.getInstance(JournalArticle.class.getName(), actionRequest);
		} catch (PortalException e3) {
			_log.error(e3);
		} catch (SystemException e3) {
			_log.error(e3);
		}
		//	CAMPAGNA
		try {

			AssetVocabulary avc = SorpresarioCategoryUtils.existVocabulary(	actionRequest, response, VOCABULARY_CAMPAGNA);

			if (avc == null)
				avc = AssetVocabularyLocalServiceUtil.addVocabulary(ja.getUserId(), VOCABULARY_CAMPAGNA, serviceContext);

			AssetCategory ac = SorpresarioCategoryUtils.existCategory(actionRequest, response, avc, f.getCampagna());

			if (ac == null)
				ac = AssetCategoryLocalServiceUtil.addCategory(ja.getUserId(),f.getCampagna(), avc.getVocabularyId(), serviceContext);

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

		//  PRODOTTO
		try {
			AssetVocabulary avp = SorpresarioCategoryUtils.existVocabulary(actionRequest, response, VOCABULARY_PRODOTTO);

			if (avp == null)
				avp = AssetVocabularyLocalServiceUtil.addVocabulary(ja.getUserId(), VOCABULARY_PRODOTTO, serviceContext);

			AssetCategory ac = SorpresarioCategoryUtils.existCategory(actionRequest, response, avp, f.getProdotto());

			if (ac == null)
				ac = AssetCategoryLocalServiceUtil.addCategory(ja.getUserId(),f.getProdotto(), avp.getVocabularyId(), serviceContext);

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
			if (listAssetEntry.size() > 0) {
				for (AssetEntry assetEntry : listAssetEntry) {
					if (assetEntry.getEntryId() == entryId)
						c++;
				}
			}
			if (c == 0) {
				try {
					AssetEntryLocalServiceUtil.addAssetCategoryAssetEntry(categoryId, entryId);
				} catch (SystemException e) {
					_log.error(e);
				}
			} else {
			}

		} catch (PortalException e2) {
			_log.error(e2);
		} catch (SystemException e2) {
			_log.error(e2);
		}

		// COUNTRY
		try {
			AssetVocabulary avco = SorpresarioCategoryUtils.existVocabulary(actionRequest, response, VOCABULARY_COUNTRY);

			if (avco == null)
				avco = AssetVocabularyLocalServiceUtil.addVocabulary(ja.getUserId(), VOCABULARY_COUNTRY, serviceContext);

			AssetCategory ac = SorpresarioCategoryUtils.existCategory(actionRequest, response, avco, f.getCountry());

			if (ac == null)
				ac = AssetCategoryLocalServiceUtil.addCategory(ja.getUserId(), f.getCountry(),avco.getVocabularyId(), serviceContext);

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

			} else {
			}

		} catch (PortalException e2) {
			_log.error(e2);
		} catch (SystemException e2) {
			_log.error(e2);
		}

		return false;

	}
}
