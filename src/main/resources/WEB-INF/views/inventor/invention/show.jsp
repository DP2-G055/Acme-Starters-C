<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="inventor.invention.form.ticker" path="ticker"/>
	<acme:form-textbox code="inventor.invention.form.name" path="name"/>
	<acme:form-textbox code="inventor.invention.form.description" path="description"/>
	<acme:form-textbox code="inventor.invention.form.startMoment" path="startMoment"/>
	<acme:form-textbox code="inventor.invention.form.endMoment" path="endMoment"/>
	<acme:form-textbox code="inventor.invention.form.moreInfo" path="moreInfo"/>
	
	<acme:button code="inventor.invention.form.button.parts" action="/inventor/part/list?inventionId=${id}" />
	<acme:button code="inventor.invention.form.button.edit" action="/inventor/invention/update?id=${id}" />
	<acme:button code="inventor.invention.form.button.publish" action="/inventor/invention/publish?id=${id}" />
</acme:form>