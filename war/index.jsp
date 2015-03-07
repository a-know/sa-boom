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
    <title>SA-BOOM!!（β） - iTunes Play Count Analyzing and Sharing [サ・ブーン！（β） - iTunes再生回数解析＆共有サイト]</title>
    <script type="text/JavaScript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>

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

        var randomArtist = "${randomArtist}";
      </script>

      <script type="text/javascript" src="js/bootstrap-tabs.js"></script>
      <script type="text/javascript" src="js/bootstrap-alerts.js"></script>
      <script type="text/javascript" src="js/dist/jquery.jqplot.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.barRenderer.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.pointLabels.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.enhancedLegendRenderer.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.categoryAxisRenderer.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
      <script type="text/javascript" src="js/dist/plugins/jqplot.canvasTextRenderer.min.js"></script>

      <script type="text/javascript" src="js/sha256.js"></script>
      <script type="text/javascript" src="js/index.js"></script>
      <script type="text/javascript" src="js/login.js"></script>
      <script type="text/javascript" src="js/backtotop.js"></script>



    <!-- Le styles -->
    <link href="js/dist/jquery.jqplot.css" rel="stylesheet">
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/original.css" rel="stylesheet">
    <link href="css/backtotop.css" rel="stylesheet">
<style type="text/css">
/* Override some defaults */
html,body {
	background-color: #FFFF78;
	background-image: url("../images/background.png"); /* 全体の背景画像 */
	background-attachment: fixed; /* 背景画像を固定する */
	font-family: "メイリオ", "Meiryo";
}

body {
	padding-top: 40px;
	/* 40px to make the container go all the way to the bottom of the topbar */
}

.container>footer p {
	text-align: center; /* center align it with the container */
}

.container {
	width: 920px;
	/* downsize our container to make the content feel a bit tighter and more cohesive. NOTE: this removes two full columns from the grid, meaning you only go to 14 columns and not 16. */
}

/* The white background content wrapper */
.container>.content {
	background-color: #FFFFFF;
	padding: 20px;
	margin: 0 -20px;
	/* negative indent the amount of the padding to maintain the grid system */
	-webkit-border-radius: 0 0 6px 6px;
	-moz-border-radius: 0 0 6px 6px;
	border-radius: 0 0 6px 6px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
}

/* Page header tweaks */
.page-header {
	background-color: #FFC000;
	padding: 20px 60px 10px;
	margin: -20px -20px 20px;
}

/* Styles you shouldn't keep as they are for displaying this base example only */
.content .span11,.content .span4 {
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

h1 small {
	color: #626262;
}

/* --- ボックス --- */
div.section {
	width: 600px; /* ボックスの幅 */
	background-color: #ffffff; /* ボックスの背景色 */
	border: 1px #c0c0c0 solid; /* ボックスの境界線 */
	font-size: 80%; /* ボックスの文字サイズ */
}

/* --- 見出し --- */
div.section h3 {
	margin: 0; /* 見出しのマージン */
	padding: 5px 10px; /* 見出しのパディング（上下、左右） */
	background-color: #f5f5f5; /* 見出しの背景色 */
	border-bottom: 1px #c0c0c0 dotted; /* 見出しの下境界線 */
	font-size: 100%; /* 見出しの文字サイズ */
}

/* --- 本文領域 --- */
div.section div.textArea {
	height: 120px; /* 本文領域の高さ */
	overflow: auto;
}

/* --- 本文領域内の段落 --- */
div.section div.textArea p {
	margin: 1em 10px; /* 段落のマージン（上下、左右） */
}

/* --- ボックス --- */
div.section-orange {
	padding-bottom: 1px; /* ボックスの下パディング */
	background-color: #ffffff;
	font-size: 80%; /* ボックスの文字サイズ */
	border-top-left-radius: 10px 10px;
	border-top-right-radius: 10px 10px;
	border-bottom-left-radius: 10px 10px;
	border-bottom-right-radius: 10px 10px;
	margin: 10px 0 10px 0;
}

/* --- 見出しエリア --- */
div.section-orange div.heading {
	margin: 0 0 1em; /* 見出しエリアのマージン（上、左右、下） */
	padding: 5px; /* 見出しエリアのパディング */
	background-color: #ffbf00;
	background-image: -moz-linear-gradient(left, #ffbf00, #ffffff);
	background-image: -ms-linear-gradient(left, #ffbf00, #ffffff);
	background-image: -webkit-gradient(linear, left, right, from(#ffffff),
		to(#ffbf00) );
	background-image: -webkit-linear-gradient(left, #ffbf00, #ffffff);
	background-image: -o-linear-gradient(left, #ffbf00, #ffffff);
	background-image: linear-gradient(left, #ffbf00, #ffffff);
	background-repeat: repeat-x;
	border: 0px #666666 solid; /* 見出しエリアの境界線 */
	border-top-left-radius: 10px 10px;
	border-top-right-radius: 0 0;
}
/* --- 見出し --- */
div.heading h3 {
	margin: 0;
	padding: 2px 0 2px 7px; /* 見出しのパディング（上右下左） */
	border-left: 4px #cccccc solid; /* 見出しの左境界線 */
	font-family: "メイリオ", "Meiryo";
	font-size: 120%; /* 見出しの文字サイズ */
	color: #f9f9f9; /* 見出しの文字色 */
	line-height: 100%;
}

/* --- ボックス内の段落 --- */
div.content {
	padding: 1px 0 10px 0px; /* 見出しのパディング（上右下左） */
	margin: 0em 10px; /* 段落のマージン（上下、左右） */
	font-family: "メイリオ", "Meiryo";
	font-size: 110%; /* 見出しの文字サイズ */
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
            <form action="/Login" name="loginForm" class="pull-right" method="post"  id="loginForm">
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
          <a href="/"><img src="images/sa-boom_logo01.png" alt="サ・ブーン（β） - iTunes再生回数解析＆共有サイト"></a>
          <h1>サ・ブーン！！（β）<small>iTunes再生回数解析＆共有サイト</small></h1>
        </div>
        <div class="row">
          <div class="span4">
            <div class="alert-message block-message error fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>sa-boom!!<br>登録済みユーザー数</strong></p>
              <br>
              <p><div align="center"><strong><span style="color:red;font-size:20px;"><c:out value="${userCount}" /></span> 人</strong></div></p>
            </div>

            <div class="alert-message block-message success fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>クライアントダウンロード</strong></p>
              <br>
              <ul>
               <li><a href="http://a-know.sakura.ne.jp/downcount/downcon.cgi?down=http://a-know.sakura.ne.jp/CDiT/saboomForWin.zip&name=saboom-WebTOP&hp=http://d.hatena.ne.jp/a-know">Windows版</a></li>
               <li><a href="http://a-know.sakura.ne.jp/downcount/downcon.cgi?down=http://a-know.sakura.ne.jp/CDiT/saboomForMac.zip&name=saboomForMac-WebTOP&hp=http://d.hatena.ne.jp/a-know/">Mac版</a></li>
              </ul>
              <br>
              動作環境としてJavaをインストールする必要があります。<br>詳しくは<a href="http://d.hatena.ne.jp/a-know/20990331">こちら</a>。
            </div>

            <div class="alert-message block-message success fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>推奨ブラウザ</strong></p>
              <br>
              <ul>
               <li><a href="http://www.google.co.jp/chrome/intl/ja/landing_ff.html">○Google Chrome</a></li>
               <li><a href="http://www.apple.com/jp/safari/">○Safari</a></li>
               <li><a href="http://mozilla.jp/firefox/">△Firefox</a></li>
               <li><a href="http://jp.opera.com/browser/">△Opera</a></li>
              </ul>
            </div>

            <div class="alert-message block-message warning fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>ユーザーランキング<br>【アップロード回数】</strong></p>
              <br>
              <c:forEach var="user" items="${userRankingOfUploadCount}" varStatus="status">
              <p><span style="font-size:10px;"><c:out value="${status.index + 1}"/>位：<a href="./user/<c:out value="${user.loginId}"/>"><c:out value="${user.loginId}"/></a> さん（<c:out value="${user.uploadCount}"/>回）</span></p>
              </c:forEach>
            </div>

            <div class="alert-message block-message warning fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>ユーザーランキング<br>【日記登録数】</strong></p>
              <br>
              <c:forEach var="user" items="${userRankingOfDiaryCount}" varStatus="status">
              <p><span style="font-size:10px;"><c:out value="${status.index + 1}"/>位：<a href="./user/<c:out value="${user.loginId}"/>"><c:out value="${user.loginId}"/></a> さん（<c:out value="${user.diaryCount}"/>回）</span></p>
              </c:forEach>
            </div>

            <div class="alert-message block-message warning fade in" data-alert="alert">
              <a href="#" class="close">&times;</a>
              <p><strong>ユーザーランキング<br>【マイページ訪問者数】</strong></p>
              <br>
              <c:forEach var="user" items="${userRankingOfAccessCount}" varStatus="status">
              <p><span style="font-size:10px;"><c:out value="${status.index + 1}"/>位：<a href="./user/<c:out value="${user.loginId}"/>"><c:out value="${user.loginId}"/></a> さん（<c:out value="${user.accessCount}"/>回）</span></p>
              </c:forEach>
            </div>



          </div>












          <div class="span11">
            <c:if test="${!sessionScope.logon}">
              <div class="alert-message info fade in" data-alert="alert" >
                <a class="close" href="#">&times;</a>
                <p><strong>初めてご利用になる方へ！</strong> ユーザー登録・再生回数情報のアップロードには、<br>別途<a href="http://d.hatena.ne.jp/a-know/20120203">『sa-boom!! クライアント』（フリーウェア）</a>が必要です！</p>
              </div>
            </c:if>
            <div class="section-orange">
              <div class="heading">
                <h3>sa-boom!!（サ・ブーン）って、なに？？</h3>
              </div>
              <div class="content">
                ひとことで言うと、『<b>iTunes再生回数解析＆共有サイト</b>』です。<br>
                『サ・ブーン』とは、すなわち『<b>差分</b>』のこと。<br>
                あらかじめ『sa-boom!! client』で記録しておいた日毎の再生回数情報を元に、期間を指定して『<b>差分</b>』してみましょう！<br>
                さらに『サ・ブーン』では、日毎の再生記録と指定した期間それぞれに対して、日記をつけることも可能です！<br>
                <br>
                No music, No Life.　日々の記憶を、日々の音楽と共に。『どういうときにどんな音楽を聞いていたのか』を、記録しておきましょう。<br>
              </div>
            </div>
            <div class="section-orange">
              <div class="heading">
              <h3>最新のユーザーアクティビティ一覧</h3>
              </div>
              <div class="content">
              <div class="textArea">
                <table class="condensed-table">
                  <tbody>
                    <c:forEach var="activity" items="${activityList}">
                    <tr>
                      <th><fmt:formatDate value="${activity.activityDate}" pattern="yyyy/MM/dd HH:mm" /></th>
                      <c:if test="${activity.activityCode == '1'}"><td><span class="label success">New!!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」が新規ユーザー登録を行いました！</td></c:if>
                      <c:if test="${activity.activityCode == '2'}"><td><span class="label notice">Update!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」が再生回数情報をアップロードしました！</td></c:if>
                      <c:if test="${activity.activityCode == '3'}"><td><span class="label important">Congraturaions!!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」の再生回数が、<c:out value="${activity.activityInfo['overCnt']}"/>回を超えました！</td></c:if>
                      <c:if test="${activity.activityCode == '4'}"><td><span class="label important">Congraturaions!!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」が、アーティスト「<c:out value="${activity.activityInfo['artistName']}"/>」の再生回数ランキング1位になりました！</td></c:if>
                      <c:if test="${activity.activityCode == '5'}"><td><span class="label notice">Update!!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」が、日記を更新しました！</td></c:if>
                      <c:if test="${activity.activityCode == '6'}"><td><span class="label notice">Update!!</span>ユーザー「<a href="./user/<c:out value="${activity.activityInfo['loginID']}"/>"><c:out value="${activity.activityInfo['loginID']}"/></a>」が、差分日記を更新しました！</td></c:if>
                    </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
              </div>
            </div>
            <!-- <div class="boxA">
              <h2>ランダムアーティストランキング＜<c:out value="${randomArtist}"/>＞</h2>
                <div class="boxB" align="center">
                <div id="randomArtistRankingError"></div>
                  <div id='graph_RandomArtistRanking' style='width:500px; height:300px; display:none;'></div>
                <div align="right"><span class="help-block">グラフをクリックすると、そのユーザーのユーザーページに移動します。</span></div>
                <hr>
                  <div class="row">
                    <c:forEach var="randomArtist" items="${urlRandomArtist}" varStatus="status">
                      <div class="span2">
                      <c:if test="${urlRandomArtist[status.index] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesRandomArtist[status.index]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlRandomArtist[status.index]}"/>"><img src="<c:out value="${imagesRandomArtist[status.index]}"/>"></a></c:if>
                      </div>
                    </c:forEach>
                  </div>
                </div>
            </div> -->
            <div class="section-orange">
              <div class="heading">
              <h3>総再生回数ランキング＜ユーザー＞</h3>
              </div>
                <div class="content" align="center">
                  <div id='graph_TotalPlayCountRanking_User' style='width:500px; height:600px; display:none;'></div>
                  <div align="right"><span class="help-block">グラフをクリックすると、そのユーザーのユーザーページに移動します。</span></div>
                  <div align="right"><span class="help-block">このグラフは、不定期間隔で更新されます。</span></div>
                </div>
            </div>
            <div class="section-orange">
              <div class="heading">
              <h3>再生回数ランキング＜アーティスト・サブーン内全体＞</h3>
              </div>
                <div class="content" align="center">

                <ul class="tabs" data-tabs="tabs">
                    <li class="active"><a href="#total">総合</li>
                </ul>

                <div id="my-tab-content" class="tab-content">

                  <div class="active tab-pane" id="total">
                    <div id='graph_TotalPlayCountRanking_Artist' style='width:500px; height:600px; display:none;'></div>
                    <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                    <hr>
                      <div class="row">
                        <c:forEach var="artistName" items="${top10ArtistDataList}" varStatus="status">
                          <div class="span2">
                            <c:if test="${urlTop10Artist[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/><br><c:out value="${playCountTop10Artist[artistName]}"/> Hits
                          </div>
                          <c:if test="${status.index == 4}">
                      </div>
                      <div class="row">
                          </c:if>
                          <c:if test="${status.index == 9}">
                      </div>
                          </c:if>
                        </c:forEach>
                    <div align="right"><span class="help-block">ランキングは、一日に一回、更新されます。</span></div>
                  </div>

                  <div class="tab-pane" id="first">
                    <br>
                    <div id='graph_TotalPlayCountRanking_Artist_first' style='width:500px; height:600px; display:none;'></div>
                    <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                    <hr>
                      <div class="row">
                        <c:forEach var="artistName" items="${top10ArtistDataList_first}" varStatus="status">
                          <div class="span2">
                            <c:if test="${urlTop10Artist_first[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist_first[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist_first[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist_first[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/><br><c:out value="${playCountTop10Artist_first[artistName]}"/> Hits
                          </div>
                          <c:if test="${status.index == 4}">
                      </div>
                      <div class="row">
                          </c:if>
                          <c:if test="${status.index == 9}">
                      </div>
                          </c:if>
                        </c:forEach>
                    <div align="right"><span class="help-block">ランキングは、一日に一回、更新されます。</span></div>
                  </div>

                  <div class="tab-pane" id="second">
                    <br>
                    <div id='graph_TotalPlayCountRanking_Artist_second' style='width:500px; height:600px; display:none;'></div>
                    <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                    <hr>
                      <div class="row">
                        <c:forEach var="artistName" items="${top10ArtistDataList_second}" varStatus="status">
                          <div class="span2">
                            <c:if test="${urlTop10Artist_second[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist_second[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist_second[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist_second[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/><br><c:out value="${playCountTop10Artist_second[artistName]}"/> Hits
                          </div>
                          <c:if test="${status.index == 4}">
                      </div>
                      <div class="row">
                          </c:if>
                          <c:if test="${status.index == 9}">
                      </div>
                          </c:if>
                        </c:forEach>
                  </div>

                  <div class="tab-pane" id="third">
                    <br>
                    <div id='graph_TotalPlayCountRanking_Artist_third' style='width:500px; height:600px; display:none;'></div>
                    <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                    <hr>
                      <div class="row">
                        <c:forEach var="artistName" items="${top10ArtistDataList_third}" varStatus="status">
                          <div class="span2">
                            <c:if test="${urlTop10Artist_third[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist_third[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist_third[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist_third[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/><br><c:out value="${playCountTop10Artist_third[artistName]}"/> Hits
                          </div>
                          <c:if test="${status.index == 4}">
                      </div>
                      <div class="row">
                          </c:if>
                          <c:if test="${status.index == 9}">
                      </div>
                          </c:if>
                        </c:forEach>
                  </div>
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
        <div align="center"><script type="text/javascript"><!--
    hatena_counter_name = "a-know";
    hatena_counter_id = "12";
    hatena_counter_ref = document.referrer+"";
    hatena_counter_screen = screen.width + "x" + screen.height+","+screen.colorDepth;
//--></script>
<script type="text/javascript" src="http://counter.hatena.ne.jp/js/counter.js"></script>
<noscript><img src="http://counter.hatena.ne.jp/a-know/12" border="0" alt="counter"></noscript>
      </footer></div>

    </div> <!-- /container -->

  </body>
</html>
