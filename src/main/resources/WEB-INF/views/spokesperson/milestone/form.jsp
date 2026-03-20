<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${!draftMode}">
	<acme:form-textbox code="spokesperson.milestone.form.label.title" path="title"/>
	<acme:form-textbox code="spokesperson.milestone.form.label.achievements" path="achievements"/>
	<acme:form-textbox code="spokesperson.milestone.form.label.effort" path="effort"/>
	<acme:form-textbox code="spokesperson.milestone.form.label.kind" path="kind"/>

	<jstl:if test="${draftMode}">
		<jstl:choose>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="spokesperson.milestone.form.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:submit code="spokesperson.milestone.form.button.update" action="update"/>
				<acme:submit code="spokesperson.milestone.form.button.delete" action="delete"/>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>

	<acme:button code="spokesperson.milestone.form.button.back" action="/spokesperson/milestone/list?campaignId=${campaignId}"/>
</acme:form>