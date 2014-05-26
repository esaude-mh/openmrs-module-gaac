<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Gaac" otherwise="/login.htm" redirect="/module/gaac/gaacList.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>


<br />
<a href="addNewGaac.form"><spring:message code="gaac.addNew"/></a> 



<br />
<c:if test="${not empty openmrs_msg}">
<span class="retiredMessage">
	<spring:message code="${openmrs_msg}" text="${openmrs_msg}" />
</span>	
</c:if>
<br />

<h2><spring:message code="gaac.list"/></h2>	
<b class="boxHeader"><spring:message code="gaac.list.title"/></b>
<form method="post" class="box">
	<table>
		<tr>
			<th> <spring:message code="general.name" /> </th>
			<th> <spring:message code="gaac.manage.identifier" /> </th>			
			<th> <spring:message code="gaac.manage.startDate" /> </th>
			<th> <spring:message code="gaac.manage.location" /> </th>			
			<th> <spring:message code="gaac.manage.affinity" /> </th>
			<th> <spring:message code="gaac.manage.focal" /> </th>
			<th> <spring:message code="gaac.manage.DateCrumbled" /> </th>
			
		</tr>
		<c:forEach var="gaac" items="${gaacList}">
			<tr>
				<td valign="top">
					<a href="addNewGaac.form?gaacId=${gaac.gaacId}">
						<c:choose>
							<c:when test="${gaac.crumbled == true}">
								<del>${gaac.name}</del>
							</c:when>
							<c:otherwise>
								${gaac.name}
							</c:otherwise>
						</c:choose>
					</a>
				</td>
				<td valign="top">${gaac.gaacIdentifier}</td>
				<td valign="top"><openmrs:formatDate date="${gaac.startDate}" type="medium" /></td>
				<td valign="top">${gaac.location.name}</td>
				<td valign="top">${gaac.affinity.name}</td>
				<td valign="top">${gaac.focalPatient.personName}</td>
				<td valign="top"><openmrs:formatDate date="${gaac.dateCrumbled}" type="medium" /></td>
			</tr>
		</c:forEach>
	</table>	
</form>
<%@ include file="/WEB-INF/template/footer.jsp" %>