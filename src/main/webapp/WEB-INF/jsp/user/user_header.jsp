<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- Header *******************************  -->
<header>
	<div class="bottom_header">
		<div class="container">
			<div class="row">
				<div class="col-md-4 col-sm-5 col-xs-6 logo-responsive">
					<div class="logo-area">
						<a href="/NguoiDung" class="pull-left logo"><img
							src="/images/logo/logo.png" alt="LOGO"></a>
					</div>
				</div>
				<c:set var="notifyCount" value="${fn:length(notifies)}" />
				<div class="top-nav notification-row">
					<!-- notification drop down start-->
					<ul class="nav pull-right top-menu">
						<!--notification end -->
						<!-- alert notification start-->
						<li id="alert_notificatoin_bar" class="dropdown">
						<a 	data-toggle="dropdown" class="dropdown-toggle" href="#"> <i
								class="icon-bell-l"></i> <c:if test="${notifyCount > 0}">
									<span class="badge bg-important"> ${notifyCount}</span>
								</c:if>
						</a>
							<ul class="dropdown-menu extended notification">
								<div class="notify-arrow notify-arrow-blue"></div>
								<li>
									<p class="blue">Bạn có ${notifyCount} Thông báo mới</p>
								</li>
								<c:if test="${notifyCount > 0}">
									<c:forEach var="notify" items="${notifies}">
										<c:if test="${notify.status == 2 }">
											<script>
												$(document).ready(function(){
													$.confirm({
												        content: "${notify.content}" + "bạn có muốn khôi phục lại tin bán này cho các Nhà buôn khác có thể xem",
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
												        			updateNotifyNRedirect(${notify.id}, "${notify.link}");
												        		}
												            },
												            'Hủy' : function () {
												            	updateNotify(${notify.id});
												            }
												        }
												    });
												})
											</script>
										</c:if>
										
										<c:if test="${notify.status != 2}">
											<li><div class="notify-content" onclick="updateNotify(${notify.id})"> 
												<span>${notify.content}</span>
												<span class="small italic pull-right">${notify.timeAgo}</span>
												<a href="${notify.link}"><i><u>Chi Tiết</u></i></a>
											</div></li>
										</c:if>
										
									</c:forEach>
								</c:if>
								<li><a href="#"><i><u>Xem tất cả các thông báo</u></i></a></li>
							</ul></li>
						<!-- alert notification end-->
						<!-- user login dropdown start-->
						<li class="dropdown"><a data-toggle="dropdown"
							class="dropdown-toggle" href="#"> <span class="profile-ava">
									<img alt=""
									src="/NongSanDD/resources/images/notify/user_icon.png">
							</span> <span class="username">${userName}</span> <b class="caret"></b>
						</a>
							<ul class="dropdown-menu extended logout">
								<div class="log-arrow-up"></div>
								<li class="eborder-top"><a href="/NguoiDung/tai-khoan">
									<i class="fas fa-user"></i> Tài Khoản</a></li>
								<li><a href="/NongSanDD/dang-xuat"><i class="fas fa-sign-out-alt"></i>
										Đăng Xuất</a></li>
							</ul></li>
						<!-- user login drop down end -->
					</ul>
					<!-- notification drop down end-->
				</div>

			</div>

		</div>
	</div>
	<!-- End of .bottom_header -->
</header>
<script>
function updateNotify(id){
	var currentNotifyCount = ${notifyCount};
	$.ajax({
		type : "GET",
		data : {
			notifyID : id
		},
		url : "/NguoiDung/thong-bao/da-xem",
		success : function(response) {
			if(currentNotifyCount > 1){
				$(".bg-important").val(currentNotifyCount--);
			}else{
				$(".bg-important").remove();
			}
		}
	});
}

function updateNotifyNRedirect(id, link){
	var currentNotifyCount = ${notifyCount};
	$.ajax({
		type : "GET",
		data : {
			notifyID : id
		},
		url : "/NguoiDung/thong-bao/da-xem",
		success : function(response) {
			window.location.href = link;
		}
	});
}
</script>
<!-- Menu ******************************* -->
<div class="theme_menu color1_bg">
	<div class="container" style="height: 50px;">
		<nav class="menuzord pull-left" id="main_menu">
			<ul class="menuzord-menu">
				<li id="price-page"><a href="/NguoiDung/gia/hom-nay">Danh Sách Giá</a></li>
				<li id="user-page"><a href="/NguoiDung">Trang Chủ</a></li>
				<li id="upload-page"><a href="/NguoiDung/tin-ban/dang-tin">Đăng Tin Bán</a></li>
				<li id="sold-page"><a href="/NguoiDung/tin-ban/ds">Tin Đã Bán</a></li>
			</ul>
			<!-- End of .menuzord-menu -->
		</nav>

		<!-- End of #main_menu -->

	</div>
	<!-- End of .conatiner -->
</div>
<script>
var pathname = window.location.pathname;
if(pathname.indexOf("gia-hom-nay") !== -1){
	$("#price-page").addClass("current_page");
}else if(pathname.indexOf("dang-tin") !== -1){
	$("#upload-page").addClass("current_page");
}else if(pathname.indexOf("ds-tin-ban") !== -1){
	$("#sold-page").addClass("current_page");
}else{
	$("#user-page").addClass("current_page");
}
</script>
<!-- End of .theme_menu -->