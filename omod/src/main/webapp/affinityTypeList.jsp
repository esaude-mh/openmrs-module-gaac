<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Affinity Types" otherwise="/login.htm" redirect="/module/gaac/affinityTypeList.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>

<h2><spring:message code="gaac.affinityType.management"/></h2>	

<a href="affinityTypeForm.form"><spring:message code="gaac.affinityType.add"/></a> 

<br />
<br />
<c:if test="${not empty openmrs_msg}">
<span class="retiredMessage">
	<spring:message code="${openmrs_msg}" text="${openmrs_msg}" />
</span>	
</c:if>
<br />
<br />
<b class="boxHeader"><spring:message code="gaac.affinityType.manage"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> <spring:message code="general.name" /> </th>
			<th> <spring:message code="general.description" /> </th>
		</tr>
		<c:forEach var="affinityType" items="${affinityTypeList}">
			<tr>
				<td valign="top">
					<a href="affinityTypeForm.form?affinityTypeId=${affinityType.affinityTypeId}">
						<c:choose>
							<c:when test="${affinityType.retired == true}">
								<del>${affinityType.name}</del>
							</c:when>
							<c:otherwise>
								${affinityType.name}
							</c:otherwise>
						</c:choose>
					</a>
				</td>
				<td valign="top">${affinityType.description}</td>
			</tr>
		</c:forEach>
	</table>
	
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>