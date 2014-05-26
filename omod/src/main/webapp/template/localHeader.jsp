<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/gaac/gaacList.form"><spring:message
				code="gaac.manage" /></a>
	</li>
	
	<li>
		<a
		href="${pageContext.request.contextPath}/module/gaac/affinityTypeList.form"><spring:message
				code="gaac.affinityType.manage" /></a>
	</li>
	
	<li>
		<a
		href="${pageContext.request.contextPath}/module/gaac/reasonList.form"><spring:message
				code="gaac.reasonLeaving.manage" /></a>
	</li>
	
	<!-- Add further links here -->
</ul>
<!--  <h2>
	<spring:message code="gaac.title" />
</h2>-->
