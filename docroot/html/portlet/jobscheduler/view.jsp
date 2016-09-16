
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<%@ include file="../init.jsp" %>

<liferay-portlet:actionURL var="actionURL" />

<aui:form method="post" action="<%= actionURL.toString() %>" >

	<aui:input type="hidden" name="groupId" id="groupId" label="" value="<%= groupId %>" />
	
	<style>
	ul.nav.nav-tabs li:nth-child(8) {
		float: right;
	}
	ul.nav.nav-tabs li:nth-child(9) {
		float: right;
	}
	</style>
	
	<liferay-ui:tabs
	   names="<%= socialTabNames %>"
	   value="<%= currentTab %>"
	   refresh="<%= false %>"
	>
			
		<liferay-ui:section>
			<jsp:include page="/html/portlet/jobscheduler/view_scheduler.jsp" />
		</liferay-ui:section>
	</liferay-ui:tabs>
	
	<aui:button-row>
	
		<aui:column columnWidth="50" >
			<aui:button type="submit" value="Save" />
		</aui:column>
			
	</aui:button-row>
	
</aui:form>


