<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${role == all}"><jsp:include page="form_page/page-head.jsp"></jsp:include></c:if>
<c:if test="${role == user}">
	<jsp:include page="form_page/user-head.jsp"></jsp:include>
</c:if>
<c:if test="${role == trader}">
	<jsp:include page="form_page/trader-head.jsp"></jsp:include>
</c:if>
<%@ include file = "form_page/page-head.jsp" %>
<div class="price_container container">
	<div>
		<div class="col-md-10 col-xs-12" style="display: flex;">
			<form class="col-md-4" action="gia-ngay-truoc" method="get">
				<div class="col-md-8 col-sm-6 col-xs-12 form-group has-feedback">
					<input type='text' class="form-control" id='price_datepicker' name="date" />
				</div>
				<div class="col-md-4 col-sm-6 col-xs-12 form-group has-feedback">
					<button class="btn btn-success" type="submit">Cập Nhập</button>
				</div>
			</form>
			
		</div>
		<table class="display nowrap responsive-table" id="price_table">
			<caption>Giá Nông Sản ${title}</caption>
			<thead>
				<tr>
					<th scope="col"></th>
					<th scope="col" style="width: 20%">Tên Nông Sản</th>
					<th scope="col" style="width: 15%">Giá Hôm Nay</th>
					<th scope="col" style="width: 15%">Thay Đổi</th>
					<th scope="col" style="width: 15%">Cao nhất tháng này</th>
					<th scope="col" style="width: 15%">thấp nhất tháng này</th>
					<th scope="col" style="width: 15%">TB tháng này</th>
				</tr>
			</thead>
			<tbody>
 				<c:forEach var="price" items="${agriPrices}">
					<tr>
					<td scope="row">${price.id}</td>
					<td scope="row"><a href="/gia/bieu-do?id=${price.id}" class="link_h" style="font-weight: bold; color: #577903" data="${price.id}">
						${price.name}</a></td>
					<td>${price.price}</td>
					<c:choose>
					    <c:when test="${price.change == 0}">
					        <td>0</td>
					    </c:when> 
					    <c:when test="${price.change > 0}">
					        <td style="background-color: #d3f5d7">+${price.change}</td>
					    </c:when>   
					    <c:otherwise>
					        <td style="background-color: #fbdcdc">${price.change}</td>
					    </c:otherwise>
					</c:choose>
					<td>${price.max}</td>
					<td>${price.min}</td>
					<td>${price.avg}</td>
				</tr>
				</c:forEach> 
			</tbody>

		</table>
	</div>
</div>

<%@ include file = "form_page/page-foot.jsp" %>

<script>
$(document).ready(function() {
	

	$('#price_datepicker').datetimepicker({
		format : 'YYYY-MM-DD',
		defaultDate : new Date(),
		maxDate : moment(),
		minDate : "2018/8/20"
	}); 
	
	/* price-list.js */
	$('#price_table').DataTable({
        responsive: true,
		"bLengthChange" : false,
		"bFilter" : true,
		"bInfo" : true,
		"bAutoWidth" : false,
		"columnDefs" : [ {
			"targets" : [ 0, 1 ],
			"orderable" : false
		} ],
		"language" : {
			search : "",
			"zeroRecords" : "Không Có Dữ Liệu Nào Được Tìm Thấy",
			"info" : "Trang _PAGE_ Trên _PAGES_ Trang",
			"emptyTable" : "Không Có Dữ Liệu",
			"infoEmpty" : "Hiện 0 Đến 0 Của 0 Dòng",
			"searchPlaceholder" : "Nhập Tên Nông Sản",
			"paginate" : {
				"first" : "Đầu",
				"last" : "Cuối",
				"next" : "Tiếp",
				"previous" : "Trước"
			},
		},
	});
})
</script>