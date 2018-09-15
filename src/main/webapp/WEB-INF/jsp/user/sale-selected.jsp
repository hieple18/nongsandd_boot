<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../form_page/user-head.jsp"></jsp:include>
<div class="price_container container">
	<div>
		<table class="display nowrap responsive-table" id="sale_selected">
			<caption>Danh Sách Đã Bán</caption>
			<thead>
				<tr>
					<th scope="col"></th>
					<th scope="col" style="width: 20%">Nhà Buôn</th>
					<th scope="col" style="width: 20%">Thông tin</th>
					<th scope="col" style="width: 15%">Số Lượng</th>
					<th scope="col" style="width: 15%">Diện Tích</th>
					<th scope="col" style="width: 15%">Ngày Xác nhận</th>
					<th scope="col" style="width: 10%">Hành Động</th>
				</tr>
			</thead>
			<tbody>
				<c:set var = "stt" scope = "session" value = "1"/>
				<c:forEach var="data" items="${datas}">
				<tr>
					<td><c:out value="${stt}"/></td>
					<td><ul>
						<li><a href="/NguoiDung/nb/thong-tin?id=${data[0].id}">${data[0].name}</a></li>
						<li>SDT: ${data[0].phoneNum}</li>
					</ul></td>
					<td><ul>
						<li>${data[1].agriculture.name}</li>
						<li>Giá: ${data[1].price} (triệu)</li>
					</ul></td>
					<td>${data[1].quantity} (Ngìn Cây)</td>
					<td>${data[1].area} (Sào)</td>
					<td>"${data[2]}"</td>
					<td>
						<select class="form-control form-control-lg action_option">
							<option selected disabled>Chọn</option>
							<option value="/NguoiDung/tin-ban/chi-tiet?id=${data[1].id}">Chi Tiết</option>
							<option value="/NguoiDung/tin-ban/chinh-sua?id=${data[1].id}">Sửa</option>
						</select>
					</td>
				</tr>
				<c:set var = "stt" scope = "session" value = "${stt + 1}"/>
				</c:forEach>
			</tbody>
		</table>
	</div>
		<div id="my_sale_slide_img" style="display:none;"> 	
		</div>
</div>
<jsp:include page="../form_page/page-foot.jsp"></jsp:include>
<!-- user/index -->

<script type='text/javascript'>
$(document).ready(function() {
	$('.option_action').chosen();
	
	$("#sale_selected").on('change','.action_option', function () { 
	    var value = $(this).val();
	    window.location.href = this.value;
	});
});
	
	$('#sale_selected').DataTable({
        responsive: true,
		"bLengthChange" : false,
		"bFilter" : true,
		"bInfo" : true,
		"bAutoWidth" : false,
		"columnDefs" : [ {
			"targets" : [ 0, 1, 5],
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
	
	function confirmRequest(id){
		$.confirm({
	        content: 'Bạn có muốn các nhận yêu cầu của Nhà buôn',
	        icon: 'fa fa-question-circle',
	        animation: 'scale',
	        closeAnimation: 'scale',
	        opacity: 0.5,
	        buttons: {
	            'confirm': function (){
	            	window.location.href = "/NguoiDung/yeu-cau/xac-nhan?id=" + id;
	            	
	            },
	            cancel: function () {
	            }
	        }
	    });
	}
	
</script>
