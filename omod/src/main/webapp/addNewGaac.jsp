<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Gaac" otherwise="/login.htm"
	redirect="/module/gaac/addNewGaac.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>



<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/scripts/dojoConfig.js" />
<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />

<script type="text/javascript">
	dojo.addOnLoad(function() {		
		voidedClicked(document.getElementById("voided"));
		crumbledClicked(document.getElementById("crumbled"));
	})
</script>

<script type="text/javascript">
	function confirmPurge() {
		if (confirm("Are you sure you want to purge this object? It will be permanently removed from the system.")) {
			return true;
		} else {
			return false;
		}
	}

	function voidedClicked(input) {
		var reason = document.getElementById("voidReason");
		var voidedBy = document.getElementById("voidedBy");
		if (input) {
			if (input.checked) {
				reason.style.display = "";
				if (voidedBy)
					voidedBy.style.display = "";
			} else {
				reason.style.display = "none";
				if (voidedBy)
					voidedBy.style.display = "none";
			}
		}
	}
	
	function crumbledClicked(input) {
		var reasonCrub = document.getElementById("reasonCrumbled");
		var dateCrub = document.getElementById("dateCrumbled1");
		if (input) {
			if (input.checked) {
				reasonCrub.style.display = "";
				dateCrub.style.display = "";
				
			} else {				
				reasonCrub.style.display = "none";
				dateCrub.style.display = "none";
			}
		}
	}
	
</script>

<h2>
	<spring:message code="gaac.manage.title" />
</h2>

<form method="post">
	<fieldset>
		<table>
			<tr>
				<td align="right"><spring:message code="general.name" /></td>
				<td><spring:bind path="gaac.name">
						<input type="text" name="name" value="${status.value}" size="35" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr>
				<td align="right"><spring:message code="gaac.manage.identifier" /></td>
				<td><spring:bind path="gaac.gaacIdentifier">
						<input type="text" name="gaacIdentifier" value="${status.value}"
							size="35" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr>
				<td align="right"><spring:message code="gaac.manage.startDate" /></td>
				<td><spring:bind path="gaac.startDate">						
						<input type="text" name="startDate" size="10" value="${status.value}"
					onClick="showCalendar(this)" />	
					<c:if test="${status.errorMessage != ''}">
						<span class="error">${status.errorMessage}</span>
					</c:if>				  	
					</spring:bind>
				</td>
			</tr>
			
			<tr>
				<td align="right"><spring:message code="gaac.manage.location" /></td>
				<td><spring:bind path="gaac.location">
						<openmrs_tag:locationField formFieldName="location"
							initialValue="${status.value}" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr>
				<td align="right"><spring:message code="gaac.manage.affinity" /></td>
				<td><spring:bind path="gaac.affinity">
						<select name="${status.expression}">
							<option value=""></option>
							<c:forEach items="${affinityTypes}" var="type">
								<option value="${type.affinityTypeId}"
									<c:if test="${type.affinityTypeId == status.value}">selected</c:if>>${type.name}</option>
							</c:forEach>
						</select>
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
			<tr>
				<td align="right"><spring:message code="gaac.manage.focal" /></td>
				<td><spring:bind path="gaac.focalPatient">
						<openmrs_tag:patientField formFieldName="focalPatient"
							searchLabelCode="Patient.find" initialValue="${status.value}"
							linkUrl="" callback="" allowSearch="true" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr>
				<td align="right"><spring:message code="general.description" /></td>
				<td valign="top"><spring:bind path="gaac.description">
						<textarea name="description" rows="3" cols="40">${status.value}</textarea>
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
			<c:if test="${gaac.gaacId != null}">
				<tr>
					<td align="right"><spring:message code="general.createdBy" /></td>
					<td>${gaac.creator.personName} - <openmrs:formatDate
							date="${gaac.dateCreated}" type="long" />
					</td>
				</tr>
				
				<tr>
					<td align="right"><spring:message code="gaac.manage.crumbled" /></td>
					<td><spring:bind path="gaac.crumbled">
							<input type="hidden" name="_${status.expression}" />
							<input type="checkbox" name="${status.expression}" id="crumbled"
								onClick="crumbledClicked(this)"
								<c:if test="${gaac.crumbled}">checked</c:if> />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>
				
				<tr id="dateCrumbled1">
					<td align="right"><spring:message code="gaac.manage.DateCrumbled" /></td>
					<td><spring:bind path="gaac.dateCrumbled">						
							<input type="text" name="dateCrumbled" size="10" value="${status.value}"
						onClick="showCalendar(this)" />	
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>				  	
						</spring:bind>
					</td>
				</tr>			
				
				<tr id="reasonCrumbled">
					<td align="right"><spring:message code="gaac.manage.crumbledReason" /></td>
					<td><spring:bind path="gaac.reasonCrumbled">
							<input type="text" value="${status.value}"
								name="${status.expression}" size="40" />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>
				
				
				<tr>
					<td align="right"><spring:message code="general.voided" /></td>
					<td><spring:bind path="gaac.voided">
							<input type="hidden" name="_${status.expression}" />
							<input type="checkbox" name="${status.expression}" id="voided"
								onClick="voidedClicked(this)"
								<c:if test="${gaac.voided}">checked</c:if> />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>				
				
				<tr id="voidReason">
					<td align="right"><spring:message code="general.voidReason" /></td>
					<td><spring:bind path="gaac.voidReason">
							<input type="text" value="${status.value}"
								name="${status.expression}" size="40" />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>
				<c:if test="${gaac.voidedBy != null}">
					<tr id="voidedBy">
						<td align="right"><spring:message code="general.voidedBy" /></td>
						<td>${gaac.voidedBy.personName} - <openmrs:formatDate
								date="${gaac.dateVoided}" type="medium" />
						</td>
					</tr>
				</c:if>
			</c:if>
		</table>
		<input type="hidden" name="gaacId:int" value="${gaac.gaacId}">
		<br /> <input type="submit"
			value="<spring:message code="gaac.manage.save"/>" name="save" />
	</fieldset>
</form>

<br />

<c:if test="${gaac.gaacId != null}">
	<b class="boxHeader"><spring:message code="gaac.member.list" /></b>
	<form method="post" class="box">
		<fieldset>
			<table>
				<tr>
					<th><spring:message code="Patient.identifier" /></th>
					<th><spring:message code="general.name" /></th>
					<th><spring:message code="gaac.member.startDate" /></th>
					<th><spring:message code="gaac.member.endDate" /></th>
					<th><spring:message code="gaac.member.reasonLeaving" /></th>

				</tr>
				<c:forEach var="gaacmember" items="${gaac.members}">
				<c:if test="${gaacmember.voided == false }">
					<tr>

						<td valign="top"><a
							href="gaacMemberForm.form?gaacMemberId=${gaacmember.gaacMemberId}&gaacId=${gaac.gaacId}">
								<c:choose>
									<c:when test="${gaacmember.leaving == true}">
										<del>${gaacmember.member.patientIdentifier}</del>
									</c:when>
									<c:otherwise>
								${gaacmember.member.patientIdentifier}
							</c:otherwise>
								</c:choose>
						</a></td>
						<td valign="top">${gaacmember.member.personName}</td>
						<td valign="top"><openmrs:formatDate
								date="${gaacmember.startDate}" type="medium" /></td>
						<td valign="top"><c:if test="${gaacmember.endDate != null}">
								<openmrs:formatDate date="${gaacmember.endDate}" type="medium" />
							</c:if></td>
						<td valign="top"><c:if
								test="${gaacmember.reasonLeaving != null}">
						${gaacmember.reasonLeaving.name}
					</c:if></td>
					</tr>
					</c:if>
				</c:forEach>
			</table>
			<a href="gaacMemberForm.form?gaacId=${gaac.gaacId}"><spring:message
					code="gaac.member.addNew" /></a>
			
		</fieldset>
	</form>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>