package com.sorpresario;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

public class SorpresarioCategoryUtils {

	private static Log _log = LogFactoryUtil.getLog(SorpresarioCategoryImportUtils.class);

	public static AssetVocabulary existVocabulary(ActionRequest actionRequest,	ActionResponse response, String vocabulary) {

		DynamicQuery queryVocabulary = DynamicQueryFactoryUtil.forClass(AssetVocabulary.class, PortalClassLoaderUtil.getClassLoader());
		queryVocabulary.add(PropertyFactoryUtil.forName("name").eq(vocabulary));

		try {
			List<AssetVocabulary> queryVocabularyResult = SorpresarioUtils.castList(AssetVocabulary.class,AssetVocabularyLocalServiceUtil.dynamicQuery(queryVocabulary));
			if (!queryVocabularyResult.isEmpty()) {
				return queryVocabularyResult.get(0);
			}
		} catch (Exception e) {
			_log.error(e);
			return null;
		}

		return null;
	}

	public static AssetCategory existCategory(ActionRequest actionRequest,ActionResponse response, AssetVocabulary avc, String category) {

		DynamicQuery queryCategory = DynamicQueryFactoryUtil.forClass(AssetCategory.class, PortalClassLoaderUtil.getClassLoader())
				.add(PropertyFactoryUtil.forName("name").eq(category))
				.add(PropertyFactoryUtil.forName("vocabularyId").eq(avc.getVocabularyId()));

		List<AssetCategory> queryCategoryResult = null;
		try {
			queryCategoryResult = SorpresarioUtils.castList(AssetCategory.class,AssetCategoryLocalServiceUtil.dynamicQuery(queryCategory));
			if (!queryCategoryResult.isEmpty()) {
				return queryCategoryResult.get(0);
			}
		} catch (SystemException e) {
			_log.error(e);
			return null;
		}

		return null;
	}
}
