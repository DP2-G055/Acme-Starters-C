<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${!draftMode}">
	<acme:form-textbox code="spokesperson.campaign.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.name" path="name"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.description" path="description"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.startMoment" path="startMoment"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.endMoment" path="endMoment"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.moreInfo" path="moreInfo"/>

	<jstl:if test="${_command != 'create'}">
		<acme:button code="spokesperson.campaign.form.button.milestones" action="/spokesperson/milestone/list?campaignId=${campaignId}"/>
	</jstl:if>

	<jstl:if test="${draftMode}">
		<jstl:choose>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="spokesperson.campaign.form.button.create" action="create"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:submit code="spokesperson.campaign.form.button.update" action="update"/>
				<acme:submit code="spokesperson.campaign.form.button.publish" action="publish"/>
				<acme:submit code="spokesperson.campaign.form.button.delete" action="delete"/>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:if>
</acme:form>