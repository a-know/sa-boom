<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="date" class="java.util.Date"/>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>アーティスト【<c:out value="${artistName}" />】を聴いているユーザーの検索結果 - SA-BOOM!!</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.15/jquery-ui.min.js"></script>

    <meta name="description" content="">
    <meta name="author" content="a-know">

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
      <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="./jquery/dist/excanvas.js"></script><![endif]-->

      <script type="text/javascript">
        var loginID_this_page = "${loginID_this_page}";
        var loginID_in_session;
        if('true' == "${sessionScope.logon}"){
        	loginID_in_session = "${sessionScope.loginID}";
        }else{
        	loginID_in_session = "";
        }
      </script>

      <script type="text/javascript" src="../js/bootstrap-alerts.js"></script>
      <script type="text/javascript" src="../js/bootstrap-twipsy.js"></script>
      <script type="text/javascript" src="../js/bootstrap-popover.js"></script>

      <script type="text/javascript" src="../js/sha256.js"></script>
      <script type="text/javascript" src="../js/login.js"></script>
      <script type="text/javascript" src="../js/popover.js"></script>
      <script type="text/javascript" src="../js/backtotop.js"></script>
      <script type="text/javascript" src="../js/jquery.circlelist.js"></script>
      <script type="text/javascript" src="../js/cssua.min.js"></script>


    <!-- Le styles -->
    <link href="../js/dist/jquery.jqplot.css" rel="stylesheet">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/original.css" rel="stylesheet">
    <link href="../css/backtotop.css" rel="stylesheet">
    <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.15/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
      /* Override some defaults */
      html, body {
        background-color: #FFFF78;
        background-image: url("../images/background.png"); /* 全体の背景画像 */
        background-attachment:fixed;             /* 背景画像を固定する */
        font-family: "メイリオ","Meiryo";
      }
      body {
        padding-top: 40px; /* 40px to make the container go all the way to the bottom of the topbar */
      }
      .container > footer p {
        text-align: center; /* center align it with the container */
      }
      .container {
        width: 920px; /* downsize our container to make the content feel a bit tighter and more cohesive. NOTE: this removes two full columns from the grid, meaning you only go to 14 columns and not 16. */
      }

      /* The white background content wrapper */
      .container > .content {
        background-color: #FFFFFF;
        padding: 20px;
        margin: 0 -20px; /* negative indent the amount of the padding to maintain the grid system */
        -webkit-border-radius: 0 0 6px 6px;
           -moz-border-radius: 0 0 6px 6px;
                border-radius: 0 0 6px 6px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.15);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.15);
                box-shadow: 0 1px 2px rgba(0,0,0,.15);
      }

      /* Page header tweaks */
      .page-header {
        background-color: #FFC000;
        padding: 20px 60px 10px;
        margin: -20px -20px 20px;
      }

      /* Styles you shouldn't keep as they are for displaying this base example only */
      .content .span11,
      .content .span4 {
        min-height: 500px;
      }
      /* Give a quick and non-cross-browser friendly divider */
      .content .span4 {
        margin-right: 0;
        padding-right: 19px;
        border-right: 1px solid #eee;
      }

      .topbar {
        width: 100%;
      }
      .topbar .btn {
        border: 0;
      }

      h4.welcome {
        color: #dddddd;
        display: inline-block;
        _display: inline;
      }

      a.welcome {
        color: #dddddd;
        text-decoration: underline;
        display: inline-block;
        _display: inline;
      }

      .well {
        background-color: #ffeecc;
      }

      h2 small {
        color: #626262;
      }

      div.normalInfo {
        text-decoration: underline;
        display: inline-block;
      }

      table.condensed-table-saboom {
        border-collapse: collapse;
      }
      table.condensed-table-saboom td {
        vertical-align: middle;
        padding: 2px;
        border-width: 0px 0px;
      }
      table.condensed-table-saboom th {
        padding: 2px;
      }

#preview{
	position:relative;
	bottom: 20px;
	left: 70px;
	float: left;
	width: 350px;
	height: 200px;
	margin: 5px;
}

.ua-chrome #preview{
	position:relative;
	bottom: 20px;
	left: 470px;
	float: left;
	width: 350px;
	height: 200px;
	margin: 5px;
}

.ua-safari #preview{
	position:relative;
	bottom: 20px;
	left: 470px;
	float: left;
	width: 350px;
	height: 200px;
	margin: 5px;
}
    </style>

    <!-- Le fav and touch icons -->
    <link rel="SHORTCUT ICON" href="images/favicon.ico">
  </head>

  <body>

    <div class="topbar">
      <div class="fill">
        <div class="container">
          <a class="brand" href="/">sa-boom!!</a>
          <!-- <ul class="nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul> -->
          <form action="/artistSearch" method="get">
            <input class="input-small" type="search" placeholder="Artist Name" name="artistName" required>
            <button class="btn" type="submit">検索</button>
          </form>
          <form action="/userSearch" method="post">
            <input class="input-small" type="search" placeholder="User Name" name="login-ID" style="margin: 0px 0px 0px 20px;" required>
            <button class="btn" type="submit">検索</button>
          </form>
          <c:if test="${sessionScope.logon}">
            <form action="/Logout" name="logoutForm" class="pull-right" method="post">
            <ul class="nav"><h4 class="welcome">ようこそ、<a class="welcome" href="/user/<c:out value="${sessionScope.loginID}" />"><c:out value="${sessionScope.loginID}" /></a>さん！</h4></ul>
              <button class="btn" type="submit">ログアウト</button>
            </form>
          </c:if>
          <c:if test="${!sessionScope.logon}">
            <form action="/Login" name="loginForm" class="pull-right" method="post"  onSubmit="return loginSubmit()">
              <input class="input-small" type="text" placeholder="login ID" id="loginID" name="login-ID" value="<c:out value="${sessionScope.loginID}" />" required>
              <input class="input-small" type="password" placeholder="Password" id="pass" name="pass-word" required>
              <button class="btn" type="submit">ログイン</button>
            </form>
          </c:if>
        </div>
      </div>
    </div>

    <div class="container">


      <c:if test="${sessionScope.no_loginID}">
        <div class="alert-message error fade in" data-alert="alert" >
          <a class="close" href="#">&times;</a>
          <p><strong>ログインエラー！</strong>　ログインＩＤが未入力です。</p>
        </div>
      </c:if>
      <c:if test="${sessionScope.no_pass}">
        <div class="alert-message error fade in" data-alert="alert" >
          <a class="close" href="#">&times;</a>
          <p><strong>ログインエラー！</strong>　パスワードが未入力です。</p>
        </div>
      </c:if>
      <c:if test="${sessionScope.fail_login}">
        <div class="alert-message error fade in" data-alert="alert" >
          <a class="close" href="#">&times;</a>
          <p><strong>ログインエラー！</strong>　ログインＩＤかパスワードが間違っています。</p>
        </div>
      </c:if>

      <div class="content">
        <div class="page-header">
          <a href="/"><img src="../images/sa-boom_logo_small.png" alt="サ・ブーン - iTunes再生回数解析＆共有サイト"></a>
          <h2>サ・ブーン！！（β）<small>iTunes再生回数解析＆共有サイト</small></h2>
        </div>
        <div class="row">
          <div class="span14">
            <div class="boxAlong">
              <h2>アーティスト【<c:out value="${artistName}" />】を聴いているユーザーの検索結果</h2>
              <div class="boxB">
                <c:if test="${fn:length(userList) == 0}"  var="zeroFlg" />
                <c:if test="${zeroFlg}" >
                アーティスト【<c:out value="${artistName}" />】に合致するデータがありません。<br>
                入力条件を変更（「spitz」→「スピッツ」、「ワッフルズ」→「waffles」　など）してみて下さい。<br>
                </c:if>
                <c:forEach var="user" items="${userList}" varStatus="status">
                <c:if test="${status.index < 3}"  var="outFlg" />
                <c:if test="${outFlg}" >
                  <h1><c:out value="${countList[status.index]}" /> Hits : <a href="/user/<c:out value="${user.loginId}" />"><c:out value="${user.loginId}" /></a> さん</h1>
                  <div class="row">
                    <div class="span7">
                      <table class="condensed-table">
                        <tbody>
                          <tr>
                            <th>ユーザーＩＤ</th>
                            <td><c:out value="${user.loginId}" /></td>
                          </tr>
                          <tr>
                            <th>総再生回数</th>
                            <td><c:out value="${user.totalPlayCount}" />回</td>
                          </tr>
                          <tr>
                            <th>アップロード回数</th>
                            <td><c:out value="${user.uploadCount}" />回</td>
                          </tr>
                          <tr>
                            <th>日記数</th>
                            <td><c:out value="${user.diaryCount}" />日</td>
                          </tr>
                          <tr>
                            <th>sa-boom!!開始日時</th>
                            <td><fmt:formatDate value="${user.firstRegistDate}" pattern="yyyy/MM/dd HH:mm" /></td>
                          </tr>
                          <tr>
                            <th>最終アップロード日時</th>
                            <td><fmt:formatDate value="${user.lastUploadDate}" pattern="yyyy/MM/dd HH:mm" /></td>
                          </tr>
                          <tr>
                            <th>URL</th>
                            <td><a href="<c:out value="${user.url}" />" target="_new"><c:out value="${user.url}" /></a></td>
                          </tr>
                          <tr>
                            <th>よく聴くアーティスト</th>
                            <td>
                              <c:forEach var="artistName" items="${user.top10ArtistDataList}" varStatus="index">
                                <c:out value="${artistName}"/><br>
                              </c:forEach>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="span7" style="position:absolute;">
                      <div id="preview">
                        <c:if test="${status.index == 0}" >
                        <ul id='artworklist0'>
                          <c:forEach var="artistName" items="${user.top10ArtistDataList}" varStatus="index">
                          <li><c:if test="${urlTop10Artist0[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist0[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist0[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist0[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/></li>
                          </c:forEach>
                        </ul>
                        <script>
                          $(function() {
                              var abc = $('#artworklist0').circlelist({
                                  item_width: 80,
                                  speed: 2475,
                                  reverse: false,
                                  start: 80
                                  });
                              });
                        </script>
                        </c:if>
                        <c:if test="${status.index == 1}" >
                        <ul id='artworklist1'>
                          <c:forEach var="artistName" items="${user.top10ArtistDataList}" varStatus="index">
                          <li><c:if test="${urlTop10Artist1[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist1[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist1[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist1[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/></li>
                          </c:forEach>
                        </ul>
                        <script>
                          $(function() {
                              var abc = $('#artworklist1').circlelist({
                                  item_width: 80,
                                  speed: 2475,
                                  reverse: false,
                                  start: 80
                                  });
                              });
                        </script>
                        </c:if>
                        <c:if test="${status.index == 2}" >
                        <ul id='artworklist2'>
                          <c:forEach var="artistName" items="${user.top10ArtistDataList}" varStatus="index">
                          <li><c:if test="${urlTop10Artist2[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist2[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist2[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist2[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/></li>
                          </c:forEach>
                        </ul>
                        <script>
                          $(function() {
                              var abc = $('#artworklist2').circlelist({
                                  item_width: 80,
                                  speed: 2475,
                                  reverse: false,
                                  start: 80
                                  });
                              });
                        </script>
                        </c:if>
                      </div>
                    </div>
                  </div>
                  <hr>
                  </c:if>

                  <c:if test="${!outFlg}" >
                    <h3><c:out value="${countList[status.index]}" /> Hits : <a href="/user/<c:out value="${user.loginId}" />"><c:out value="${user.loginId}" /></a> さん</h3>
                  </c:if>
                </c:forEach>
                <hr>
                  <div class="row">
                    <c:forEach var="thisArtist" items="${urlThisArtist}" varStatus="status">
                      <div class="span1.5">
                      <c:if test="${urlThisArtist[status.index] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesThisArtist[status.index]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlThisArtist[status.index]}"/>"><img src="<c:out value="${imagesThisArtist[status.index]}"/>"></a></c:if>
                      </div>
                    </c:forEach>
                  </div><!-- /row -->
              </div>
            </div>
          </div>
        </div>
      </div>





<p id="back-top">
    <a href="#"><span></span>Back To Top</a>
</p>


      <footer>
        <p>&copy; a-know 2012</p>
      </footer>

    </div> <!-- /container -->




  </body>
</html>
