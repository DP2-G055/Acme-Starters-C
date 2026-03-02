<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list show="show">
	<acme:list-column code="inventor.part.list.name" path="name" width="20%"/>
	<acme:list-column code="inventor.part.list.cost" path="cost" width="20%"/>
	<acme:list-column code="inventor.part.list.description" path="description" width="60%"/>
</acme:list>
<acme:button code="inventor.part.list.button.create" action="/inventor/part/create?inventionId=${id}"/>