<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list show="show">
	<acme:list-column code="any.invention.list.name" path="name" width="20%"/>
	<acme:list-column code="any.invention.list.description" path="description" width="80%"/>
</acme:list>