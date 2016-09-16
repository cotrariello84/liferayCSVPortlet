package com.sorpresario.scheduler.portlet;

import com.liferay.portal.model.Portlet;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.BaseControlPanelEntry;


/**
 * Control panel entry class SocialNetworksImporterPortletControlPanelEntry
 */
public class JobsSchedulerPortletControlPanelEntry extends BaseControlPanelEntry {
       
    /**
     * @see com.liferay.util.bridges.mvc.MVCPortlet#com.liferay.util.bridges.mvc.MVCPortlet()
     */
    public JobsSchedulerPortletControlPanelEntry() {
        super();
    }

    @Override
    public boolean isVisible(PermissionChecker permissionChecker, Portlet portlet)
            throws Exception {
        return false;
    }

}