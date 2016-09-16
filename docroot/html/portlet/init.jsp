<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<%@ page import="com.liferay.portal.kernel.json.JSONObject" %>
<%@ page import="com.liferay.portal.kernel.json.JSONFactoryUtil" %>

<%@ page import="javax.portlet.PortletPreferences" %>

<%@ page import="java.util.Calendar" %>

<portlet:defineObjects />

<liferay-theme:defineObjects/>

<% PortletPreferences pp = renderRequest.getPreferences(); %>

<%

String schedulerTab = LanguageUtil.get( pageContext, "scheduler" );

String socialTabNames = schedulerTab;

String currentTab = ParamUtil.getString( request, "currentTab", schedulerTab );

String currentURL = PortalUtil.getCurrentURL( request );
String redirect = ParamUtil.getString( request, "redirect" );
String backURL = ParamUtil.getString( request, "backURL" );

long groupId = themeDisplay.getScopeGroupId();
%>
