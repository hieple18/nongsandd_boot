<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../form_page/trader-head.jsp"></jsp:include>
<div class="price_container container">
	<div>
		<table class="display nowrap responsive-table" id="sale_table">
			<caption>Các Tin Đã Mua</caption>
			<thead>
				<tr>
					<th scope="col"></th>
					<th scope="col" style="width: 15%">Thông Tin</th>
					<th scope="col" style="width: 15%">Thông tin</th>
					<th scope="col" style="width: 15%">Vị Trí</th>
					<th scope="col" style="width: 15%">Số Lượng</th>
					<th scope="col" style="width: 15%">Diện Tích</th>
					<th scope="col" style="width: 15%">Ngày Mua</th>
					<th scope="col" style="width: 10%">Hành Động</th>
				</tr>
			</thead>
			<tbody>
				<c:set var = "stt" scope = "session" value = "1"/>
				<c:forEach var="data" items="${datas}">
				<tr>
					<td><c:out value="${stt}"/></td>
					<td><ul>
						<li><a href="/NhaBuon/nd/thong-tin?id=${data[1].user.id}">${data[1].user.name}</a></li>
						<li>${data[1].user.phoneNum}</li>
					</ul></td>
					<td><ul>
						<li>${data[1].agriculture.name}</li>
						<li>Giá: ${data[1].price} (triệu)</li>
					</ul></td>
					<td>${data[1].address.address}</td>
					<td>${data[1].quantity} (Ngìn Cây)</td>
					<td>${data[1].area} (Sào)</td>
					<td>"${data[2]}"</td>
					<td>
						<select class="form-control form-control-lg action_option">
							<option selected disabled>Chọn</option>
							<option value="/NhaBuon/tin-ban/chi-tiet?id=${data[1].id}&state=3"> Chi tiết</option>
							<!-- set id to this value to detected this option need to ask user's confirmation before action -->
							<option value="${data[0]}"> Xóa</option>
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

<script type="text/javascript">
$("#sale_table").on('change','.action_option', function () { 
	var value = $(this).val();
	if (value >= 0) { // value is an id. this option need to ask user's confirmation before acrion
		deleteSale(value);
	} else { // value is a link. just follow this link
		window.location.href = this.value;
	}
});

function deleteSale(id){
	$.confirm({
		content: 'Nông sản này bạn đã mua rồi. Bạn có muốn xóa tin này không?',
        icon: 'fa fa-question-circle',
        title: 'Xác nhận',
        animation: 'scale',
        closeAnimation: 'scale',
        opacity: 0.5,
        type: 'orange',
        buttons: {
        	'Đồng Ý': {
        		btnClass: 'btn-blue',
        		action: function (){
        			window.location.href = "/NhaBuon/tin-ban/da-mua/xoa?id=" + id;
        		}
            },
            'Hủy' : function () {
            }
        }
    });
}
</script>

<script type='text/javascript'>
$(document).ready(function() {
	$('.option_action').chosen();
});
	
	$('#sale_table').DataTable({
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
</script>
