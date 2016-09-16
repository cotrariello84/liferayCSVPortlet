<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<script src="https://code.jquery.com/jquery-3.1.0.js"></script>
<portlet:defineObjects />
<portlet:actionURL name="upload" var="uploadFileURLImport"></portlet:actionURL>

<div id="boxMigrationLoading">
	<img id="imgAjaxLoader" class="ajaxLoader"	src="/SorpresaPortlet-portlet/images/loading.gif" />
</div>

<c:if test="${not empty messageError}">
	<strong>${messageError}</strong>
</c:if>

<c:choose>
	<c:when test="${not empty messageSuccess}">
		<strong>${messageSuccess}</strong>
		<br />
		<br />
		<c:out value="Tot Record CSV: ${totrecordcsv}"></c:out>
		<br />
		<br />
		<br />
	</c:when>
	<c:otherwise>
		<form action="<%=uploadFileURLImport%>" enctype="multipart/form-data" method="post" id="<portlet:namespace/>mForm" name="<portlet:namespace/>mForm" >
			<c:choose>
				<c:when test="${showFile}">
					<label>File valido, è possibile caricare i dati nel	sistema.</label>
					<input name="<portlet:namespace/>upload" type="hidden"	value="upload"></input>
					<input name="<portlet:namespace/>uploads" value="Process" type="submit" class="btn btn-success"/>
				</c:when>
				<c:otherwise>
					<div class="input-group">
				    	<label class="input-group-btn">        					
							<span class="btn btn-primary">
								Browse&hellip;<input type="file" name="<portlet:namespace/>fileName" id="<portlet:namespace/>fileName" style="display: none;" />
							</span>
    					</label>
    					<input type="text" class="form-control" readonly>
					</div>
					<input name="<portlet:namespace/>analyze" type="hidden"	value="analyze"></input>
					<input name="<portlet:namespace/>analyzes" value="Analize" type="submit" id="<portlet:namespace/>analyzes" class="btn btn-primary"/>
				</c:otherwise>
			</c:choose>
		</form>
	</c:otherwise>
</c:choose>
<br />
<h3>Report</h3>
<h4>File elaborati <b>( ${fn:length(statistiche)} )</b></h4>
<c:forEach items="${statistiche}" var="statistica">

<h5>File ${statistica.nomeFile} </h5> 
<p>Righe totali ( ${statistica.righeTot} )</p>
<p>Righe OK ( ${fn:length(statistica.righeOK)} )</p>
<c:if test="${fn:length(statistica.righeKO) > 0 }">
	<p>File corrotto.</p>
	<p>Righe KO ( ${fn:length(statistica.righeKO)} )</p>
	<c:forEach var="totaliko" items="${statistica.righeKO}">
		${totaliko.msg }
	</c:forEach>
</c:if>


</c:forEach>