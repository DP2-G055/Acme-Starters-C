<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.invention.form.ticker" path="ticker"/>
	<acme:form-textbox code="any.invention.form.name" path="name"/>
	<acme:form-textbox code="any.invention.form.description" path="description"/>
	<acme:form-textbox code="any.invention.form.startMoment" path="startMoment"/>
	<acme:form-textbox code="any.invention.form.endMoment" path="endMoment"/>
	<acme:form-textbox code="any.invention.form.moreInfo" path="moreInfo"/>
	
	<jstl:if test="${_command == 'show'}">
		<acme:button code="any.invention.form.button.parts" action="/any/part/list?inventionId=${id}" />
	</jstl:if>
</acme:form>
