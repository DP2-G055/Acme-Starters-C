<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${!draftMode}">
	<acme:form-textbox code="inventor.invention.form.ticker" path="ticker"/>
	<acme:form-textbox code="inventor.invention.form.name" path="name"/>
	<acme:form-textbox code="inventor.invention.form.description" path="description"/>
	<acme:form-textbox code="inventor.invention.form.startMoment" path="startMoment"/>
	<acme:form-textbox code="inventor.invention.form.endMoment" path="endMoment"/>
	<acme:form-textbox code="inventor.invention.form.moreInfo" path="moreInfo"/>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="inventor.invention.form.button.parts" action="/inventor/part/list?inventionId=${id}"/>
	</jstl:if>
			
	<jstl:if test="${draftMode}">
		<jstl:choose>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="inventor.invention.form.button.create" action="create"/>
			</jstl:when>
			<jstl:otherwise>
				<acme:submit code="inventor.invention.form.button.update" action="update"/>
				<acme:submit code="inventor.invention.form.button.publish" action="publish"/>
				<acme:submit code="inventor.invention.form.button.delete" action="delete"/>
			</jstl:otherwise>
		</jstl:choose>			
	</jstl:if>
</acme:form>