<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<c:if test="${role == all}"><jsp:include page="form_page/page-head.jsp"></jsp:include></c:if>
<c:if test="${role == user}">
	<jsp:include page="form_page/user-head.jsp"></jsp:include>
</c:if>
<c:if test="${role == trader}">
	<jsp:include page="form_page/trader-head.jsp"></jsp:include>
</c:if>
<div class=container style="padding-top: 30px">
	<div id="line-example">
		<label style="text-align: center">So Sánh Với: </label> 
		<select data-style="btn-primary" id="compare_select" style="width: 150px; margin-bottom: 14px;" 
			data-placeholder="Chọn Nông Sản">
			<option selected value="-1">Chọn</option>
			<c:forEach var="category" items="${agriCategories}">
				<optgroup label="${category.name }">
					<c:forEach var="agri" items="${agris}">
						<c:if test="${agri.agriCategory.id == category.id }">
							<option value="${agri.id}">${agri.name}</option> </c:if>
					</c:forEach>
				</optgroup>
			</c:forEach>
		</select>

		<div id="chartContainer" style="height: 400px; width: 100%;"></div>

		<!-- <p>The above chart is generated using the data in the view model
			and rendered into the canvas</p> -->
	</div>
</div>
<%@ include file = "form_page/page-foot.jsp" %>
<script>
var chart;
$("#compare_select").chosen();
$(function () {
	chart = new Highcharts.stockChart('chartContainer', {
		tooltip: {
		    dateTimeLabelFormats:{
		    	day: '%d/%m/%y'
	    	}
		},
		xAxis: {
		    type: 'datetime',
		    labels: {
		        format: '{value:%d/%m/%y}',
		        align: 'left'
		    }
		},
		legend: {
	        enabled: true,
		},
		series: [{
			data: ${agriPrices},
			name: "${currAgriName}",
		    pointStart: Date.UTC(2018, 8, 14),
		    pointInterval: 24 * 3600 * 1000 // one day
		}]
	});
});

$(document).ready(function(){
	$("#compare_select").on("change", function(){
		var $option = $(this).find('option:selected');
		var idSelected = $option.val();
		if(idSelected !== -1){
			$.ajax({
				data : {
					"id" : idSelected
				},
				url : "/gia/so-sanh",
				type : "get",

				success : function(data) {
					chart.addSeries({
				        data: data,
				        name: $option.text(),
				        pointStart: Date.UTC(2018, 8, 14),
					    pointInterval: 24 * 3600 * 1000 // one day
				    });
				}
			});
			
			$('option:selected', this).remove();
			$("#compare_select").trigger('chosen:updated');
			$('#compare_select option[value=-1]').attr('selected','selected');
		}
	})
})
</script>

