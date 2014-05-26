<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Reason Leaving Gaac Types" otherwise="/login.htm" redirect="/module/gaac/reasonList.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<h2><spring:message code="gaac.reasonLeaving.title"/></h2>	

<a href="reasonLeavingTypeForm.form"><spring:message code="gaac.reasonLeaving.add"/></a> 

<br />
<br />
<c:if test="${not empty openmrs_msg}">
<span class="retiredMessage">
	<spring:message code="${openmrs_msg}" text="${openmrs_msg}" />
</span>	
</c:if>
<br />
<br />

<b class="boxHeader"><spring:message code="gaac.reasonLeaving.manage"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> <spring:message code="general.name" /> </th>
			<th> <spring:message code="general.description" /> </th>
		</tr>
		<c:forEach var="reasonLeavingType" items="${reasonLeavingTypeList}">
			<tr>
				<td valign="top">
					<a href="reasonLeavingTypeForm.form?reasonLeavingTypeId=${reasonLeavingType.reasonLeavingTypeId}">
						<c:choose>
							<c:when test="${reasonLeavingType.retired == true}">
								<del>${reasonLeavingType.name}</del>
							</c:when>
							<c:otherwise>
								${reasonLeavingType.name}
							</c:otherwise>
						</c:choose>
					</a>
				</td>
				<td valign="top">${reasonLeavingType.description}</td>
			</tr>
		</c:forEach>
	</table>	
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>