<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!-- Header *******************************  -->
<header>
	<div class="bottom_header">
		<div class="container">
			<div class="row">
				<div class="col-md-4 col-sm-5 col-xs-6 logo-responsive">
					<div class="logo-area">
						<a href="/NongSanDD/" class="pull-left logo"><img
							src="/images/logo/logo.png" alt="LOGO"></a>
					</div>
				</div>

				<div class="col-md-4 col-sm-12 col-xs-12" style="float: right">
					<div class="autor">
						<a class="first-login" href="/NguoiDung">Đăng nhập</a>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- End of .bottom_header -->
</header>

<!-- Menu ******************************* -->
<div class="theme_menu color1_bg">
	<div class="container">
		<nav class="menuzord pull-left" id="main_menu">
			<ul class="menuzord-menu">
				<li id="price-page"><a href="/gia/hom-nay">Danh
						Sách Giá</a></li>
				<li id="list-page"><a href="/nha-buon/danh-sach">DS Nhà
						Buôn</a></li>
			</ul>
			<!-- End of .menuzord-menu -->
		</nav>
		<div class="nav_side_content pull-right">
			<ul class="icon_header">
				<li class="border_round tran3s"><a href="https://www.facebook.com/hiep96.name" target="_blank"><i
						class="fab fa-facebook-f"></i></a></li>
				<li class="border_round tran3s"><a href="mailto:hiep96.uit@gmail.com" target="_blank"><i
						class="far fa-envelope"></i></a></li>
				<li class="border_round tran3s"><a href="#" target="_blank"><i
						class="fas fa-info"></i></a></li>
			</ul>
		</div>
		<!-- End of #main_menu -->

	</div>
	<!-- End of .conatiner -->
</div>

<script>
	var pathname = window.location.pathname;
	if (pathname.indexOf("gia-hom-nay") !== -1) {
		$("#price-page").addClass("current_page");
	} else if (pathname.indexOf("ds-nha-buon") !== -1) {
		$("#list-page").addClass("current_page");
	}
</script>
<!-- End of .theme_menu -->