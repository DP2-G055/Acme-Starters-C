<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="true">
	<acme:list-column code="spokesperson.campaign.list.label.ticker" path="ticker" width="20%"/>
	<acme:list-column code="spokesperson.campaign.list.label.name" path="name" width="25%"/>
	<acme:list-column code="spokesperson.campaign.list.label.description" path="description" width="40%"/>
	<acme:list-column code="spokesperson.campaign.list.label.draftMode" path="draftMode" width="15%"/>
</acme:list>

<acme:button code="spokesperson.campaign.list.button.create" action="/spokesperson/campaign/create"/>