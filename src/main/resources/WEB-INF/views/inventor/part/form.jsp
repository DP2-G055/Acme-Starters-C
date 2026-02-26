<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<input type="hidden" name="inventionId" value="${inventionId}" />

	<acme:form-textbox code="inventor.part.form.name" path="name"/>
	<acme:form-textbox code="inventor.part.form.description" path="description"/>
	<acme:form-textbox code="inventor.part.form.cost" path="cost"/>
	<acme:form-select choices="${kindOptions}" code="inventor.part.form.kind" path="kind"/>
	
	<jstl:if test="${draftMode}">
		<jstl:choose>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="inventor.invention.form.button.create" action="create"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:submit code="inventor.invention.form.button.update" action="update"/>
				<acme:submit code="inventor.invention.form.button.delete" action="delete"/>
			</jstl:otherwise>
		</jstl:choose>	
	</jstl:if>
</acme:form>