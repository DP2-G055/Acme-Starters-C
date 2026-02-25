<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="inventor.invention.form.ticker" path="ticker"/>
	<acme:form-textbox code="inventor.invention.form.name" path="name"/>
	<acme:form-textbox code="inventor.invention.form.description" path="description"/>
	<acme:form-textbox code="inventor.invention.form.startMoment" path="startMoment"/>
	<acme:form-textbox code="inventor.invention.form.endMoment" path="endMoment"/>
	<acme:form-textbox code="inventor.invention.form.moreInfo" path="moreInfo"/>
	
	<acme:submit code="inventor.invention.form.button.save" action="update"/>
</acme:form>