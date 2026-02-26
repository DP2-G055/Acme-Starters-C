<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="true">
	<acme:list-column code="inventor.invention.list.ticker" path="ticker" width="20%"/>
	<acme:list-column code="inventor.invention.list.name" path="name" width="20%"/>
	<acme:list-column code="inventor.invention.list.description" path="description" width="60%"/>
	
</acme:list>
<acme:button code="inventor.invention.list.button.create" action="/inventor/invention/create"/>