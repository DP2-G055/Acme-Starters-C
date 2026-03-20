<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="true">
	<acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="35%"/>
	<acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="25%"/>
	<acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="20%"/>
</acme:list>

<jstl:if test="${draftMode}">
	<acme:button code="spokesperson.milestone.list.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
</jstl:if>

<acme:button code="spokesperson.milestone.list.button.back" action="/spokesperson/campaign/show?id=${campaignId}"/>