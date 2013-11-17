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
    <title><c:out value="${loginID_this_page}" />'s SA-BOOM!!</title>
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
      </script>

      <script type="text/javascript" src="../js/bootstrap-alerts.js"></script>
      <script type="text/javascript" src="../js/bootstrap-tabs.js"></script>
      <script type="text/javascript" src="../js/bootstrap-modal.js"></script>
      <script type="text/javascript" src="../js/bootstrap-twipsy.js"></script>
      <script type="text/javascript" src="../js/bootstrap-popover.js"></script>
      <script type="text/javascript" src="../js/dist/jquery.jqplot.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.barRenderer.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.pointLabels.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.enhancedLegendRenderer.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.categoryAxisRenderer.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.canvasTextRenderer.min.js"></script>
      <script type="text/javascript" src="../js/dist/plugins/jqplot.highlighter.min.js"></script>

      <script type="text/javascript" src="../js/sha256.js"></script>
      <script type="text/javascript" src="../js/login.js"></script>
      <script type="text/javascript" src="../js/normal_view_graph.js"></script>
      <script type="text/javascript" src="../js/normal_diary.js"></script>
      <script type="text/javascript" src="../js/popover.js"></script>
      <script type="text/javascript" src="../js/normal_view.js"></script>
      <script type="text/javascript" src="../js/saboom_view_graph.js"></script>
      <script type="text/javascript" src="../js/saboom_diary.js"></script>
      <script type="text/javascript" src="../js/backtotop.js"></script>
      <script type="text/javascript" src="../js/settings.js"></script>
      <script type="text/javascript" src="../js/message.js"></script>


      <script type="text/javascript">
        var controll_data = {};
        controll_data.functionCode = '1';
        controll_data.targetLoginID = loginID_this_page;

        var data = new Array();
        var tick_data = new Array();


        $(document).ready(function(){
            $.ajax({
                type : 'GET',
                url : '/graph/GetGraphData',
                data : controll_data,
                cache : false,
                dataType : 'json',

                success : function(json) {
                      $.each(json, function(i, TotalPlayCountByUser){
                    	  data.push(TotalPlayCountByUser.playCount);
                    	  tick_data.push(TotalPlayCountByUser.yyyymmdd);
                      });

                      $.jqplot(
                              'graph_TotalPlayCount',[data],
                              {
                                title: '総再生回数の推移グラフ',
                                seriesColors: [ "#f7d4d4", "#efa9a9", "#e77e7e", "#e05252", "#d82727"],
                                seriesDefaults: {
                                  renderer: $.jqplot.BarRenderer,
                                  pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
                                  rendererOptions: {
                                    showDataLabels: true,
                                    barWidth: 30,
                                    varyBarColor: true
                                  }
                                },
                                axes: {
                                  xaxis: {
                                    renderer: $.jqplot.CategoryAxisRenderer,
                                    ticks: tick_data,
                                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                                    tickOptions: {
                                      angle: -30
                                    }
                                  }
                                }
                              }
                      );
                },
                complete : function() {
                  //通信終了
                }
            });
        });
      </script>

    <!-- Le styles -->
    <link href="../js/dist/jquery.jqplot.css" rel="stylesheet">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/original.css" rel="stylesheet">
    <link href="../css/backtotop.css" rel="stylesheet">
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

.popover .inner {
	width: 500px;
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
    <link rel="SHORTCUT ICON" href="/images/favicon.ico">
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
      <c:if test="${sessionScope.twitter_success == '1'}">
        <div class="alert-message success fade in" data-alert="alert" >
          <a class="close" href="#">&times;</a>
          <p>Twitterとの連携が完了しました。</p>
        </div>
      </c:if>
      <c:if test="${sessionScope.twitter_release_success == '1'}">
        <div class="alert-message success fade in" data-alert="alert" >
          <a class="close" href="#">&times;</a>
          <p>Twitterとの連携の解除が完了しました。</p>
        </div>
      </c:if>

      <div class="content">
        <div class="page-header">
          <a href="/"><img src="../images/sa-boom_logo_small.png" alt="サ・ブーン - iTunes再生回数解析＆共有サイト"></a>
          <h2>サ・ブーン！！（β）<small>iTunes再生回数解析＆共有サイト</small></h2>
        </div>
        <div class="row">
          <div class="span4">
            <c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" />
            <c:if test="${flg}" >
              <c:if test="${infomationMap['totalInfomationCount'] > 0}" var="messageFlg" />
              <c:if test="${messageFlg}" >
              <div class="alert-message block-message error fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong>お知らせ：<span style="color:red;"><c:out value="${infomationMap['totalInfomationCount']}" /></span>件</strong></p>
                <br>
                <ul>
                  <li>未読メッセージ：<c:out value="${infomationMap['unreadMessageCount']}" />件</li>
                </ul>
              </div>
              </c:if>
            </c:if>
            <c:if test="${!flg}" >
            </c:if>

            <c:if test="${errorCode_userView == 0}">
              <div class="alert-message block-message warning fade in" data-alert="alert">
                <p><strong><span style="color:blue;">このページへのアクセス数</span></strong></p>
                <br>
                <p><div align="center"><span style="font-size:20px;"><strong><c:out value="${accessCount}" /> Hits!!</strong></span></div></p>
              </div>

              <div class="alert-message block-message success fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong><span style="color:blue;">Tips</span></strong></p>
                <p><strong>「日記数」って？</strong></p>
                <br>
                <p>そのユーザーが書いた日記の数です。「再生回数ごとの日記」と「任意の期間ごとの日記」の総数です。</p>
              </div>

              <div class="alert-message block-message success fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong><span style="color:blue;">Tips</span></strong></p>
                <p><strong>「sa-boom!!開始日時」って？</strong></p>
                <br>
                <p>sa-boom!! clientを通じて、sa-boom!!ユーザー登録を行った日時です。</p>
              </div>

              <div class="alert-message block-message success fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong><span style="color:blue;">Tips</span></strong></p>
                <p><strong>「大好き！」って？</strong></p>
                <br>
                <p>そのユーザーが最も聴いているアーティストの上位10位を表示しています。</p>
                <p>既にアップロードされた情報のうち、最新の情報を元に表示しています。</p>
              </div>

              <div class="alert-message block-message info fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong><span style="color:blue;">Tips</span></strong></p>
                <p><strong>「再生回数情報」タブって？</strong></p>
                <br>
                <p>sa-boom!! clientを通じてユーザーから送信された、ある時点における全登録楽曲の再生回数情報を閲覧する画面です。</p>
                <p>この情報を元に、「曲別ランキング情報」「アーティスト別ランキング情報」を計算し表示します。</p>
                <p>また、その情報に紐付く日記を書く・見ることもできます。</p>
              </div>

              <div class="alert-message block-message info fade in" data-alert="alert">
                <a href="#" class="close">&times;</a>
                <p><strong><span style="color:blue;">Tips</span></strong></p>
                <p><strong>「サ・ブーン！！」タブって？</strong></p>
                <br>
                <p>指定された２つの再生回数情報を元に、その差分（その期間でどの曲をどれだけ聴いたか？）を計算し表示する画面です。</p>
                <p>その期間に紐付く日記を書く・見ることもできます。</p>
              </div>
            </c:if>

          </div>
          <div class="span11">
            <c:if test="${errorCode_userView == 1}">
              <div class="alert-message error fade in" data-alert="alert" >
                <a class="close" href="#">&times;</a>
                <p><strong>ユーザー情報表示エラー！</strong>　ログインＩＤ「<c:out value="${loginID_this_page}" />」は存在しません。</p>
              </div>
            </c:if>
            <c:if test="${errorCode_userView == 2}">
              <div class="alert-message error fade in" data-alert="alert" >
                <a class="close" href="#">&times;</a>
                <p><strong>ユーザー情報表示エラー！</strong>　ログインＩＤ「<c:out value="${loginID_this_page}" />」は非公開設定になっています。</p>
              </div>
            </c:if>
            <c:if test="${errorCode_userView == 0}">
            <ul class="tabs" data-tabs="tabs">
                <li class="active"><a href="#userTop"><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >マイページ</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" />さんのページ</c:if></a></li>
                <li><a href="#saboom-list">再生回数情報</a></li>
                <li><a href="#my-saboom">サ・ブーン！！</a></li>
                <li><a href="#settings"><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >設定・メッセージ確認</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" />さんとコンタクト</c:if></a></li>
            </ul>

            <div id="my-tab-content" class="tab-content">
              <div class="active tab-pane" id="userTop">
                <div class="section-orange">
                <div class="heading">
                <h3><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >マイプロフィール</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" />のプロフィール</c:if></h3>
                </div>
                  <div class="content" align="center">
                  <table class="condensed-table">
                      <tbody>
                        <tr>
                          <th>ユーザーＩＤ</th>
                          <td><c:out value="${loginID_this_page}" /></td>
                        </tr>
                        <tr>
                          <th>総再生回数</th>
                          <td><c:out value="${requestUser.totalPlayCount}" />回</td>
                        </tr>
                        <tr>
                          <th>アップロード回数</th>
                          <td><c:out value="${requestUser.uploadCount}" />回</td>
                        </tr>
                        <tr>
                          <th>日記数</th>
                          <td><c:out value="${requestUser.diaryCount}" />日</td>
                        </tr>
                        <tr>
                          <th>sa-boom!!開始日時</th>
                          <td><fmt:formatDate value="${requestUser.firstRegistDate}" pattern="yyyy/MM/dd HH:mm" /></td>
                        </tr>
                        <tr>
                          <th>最終アップロード日時</th>
                          <td><fmt:formatDate value="${requestUser.lastUploadDate}" pattern="yyyy/MM/dd HH:mm" /></td>
                        </tr>
                        <tr>
                          <th>URL</th>
                          <td><a href="<c:out value="${requestUser.url}" />" target="_new"><c:out value="${requestUser.url}" /></a></td>
                        </tr>
                        <tr>
                          <th>自己紹介</th>
                          <td><c:out value="${requestUser.introduction}" /></td>
                        </tr>
                      </tbody>
                    </table>
                    <div id='graph_TotalPlayCount' style='width:500px; height:300px;'></div>
                    <div align="right"><span class="help-block">このグラフは、不定期間隔で更新されます。</span></div>
                  </div>
                </div>

                <div class="section-orange">
                <div class="heading">
                <h3>大好き！</h3>
                </div>
                  <div class="content" align="center">
                    <div class="row">
                      <c:forEach var="artistName" items="${requestUser.top10ArtistDataList}" varStatus="status">
                        <div class="span2">
                          <c:if test="${urlTop10Artist[artistName] == ''}" var="flgUrl" /><c:if test="${flgUrl}" ><img src="<c:out value="${imagesTop10Artist[artistName]}"/>"></c:if><c:if test="${!flgUrl}" ><a href="<c:out value="${urlTop10Artist[artistName]}"/>"><img src="<c:out value="${imagesTop10Artist[artistName]}"/>"></a></c:if><br><c:out value="${artistName}"/><br><c:out value="${playCountTop10Artist[artistName]}"/> Hits
                        </div>
                        <c:if test="${status.index == 4}">
                    </div><!-- /row -->
                    <div class="row">
                        </c:if>
                      </c:forEach>
                    </div>
                  </div>
                </div>

                <div class="section-orange">
                <div class="heading">
                <h3>最近書いた日記</h3>
                </div>
                  <div class="content" align="center">
                  <c:if test="${fn:length(diaryList) == 0}" var="flg" />
                  <c:if test="${flg}" >
                    まだ日記が書かれていません。
                  </c:if>

                  <c:if test="${!flg}" >
                  <table class="condensed-table">
                    <thead>
                      <tr>
                        <th>タイトル</th>
                        <th>対象の再生回数情報</th>
                      </tr>
                    </thead>
                    <tbody>
                  <c:forEach var="diary" items="${diaryList}">
                      <tr>
                        <td><c:out value="${diary['title']}"/></td>
                        <c:if test="${diary['from'] == diary['to']}" var="diaryFlg" />
                        <c:if test="${diaryFlg}" >
                          <td>「<c:out value="${diary['from']}"/>」<br>（<fmt:formatDate value="${diary['date']}" pattern="yyyy/MM/dd HH:mm" />更新）</td>
                        </c:if>
                        <c:if test="${!diaryFlg}" >
                          <td>「<c:out value="${diary['from']}"/>」～「<c:out value="${diary['to']}"/>」<br>（<fmt:formatDate value="${diary['date']}" pattern="yyyy/MM/dd HH:mm" />更新）</td>
                        </c:if>
                      </tr>
                  </c:forEach>
                    </tbody>
                  </table>
                  </c:if>
                  </div>
                </div>

                <div class="section-orange">
                <div class="heading">
                <h3><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >最近のアクティビティ</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" />の最近のアクティビティ</c:if></h3>
                </div>
                  <div class="content" align="center">
                <table class="condensed-table">
                  <tbody>
                    <c:forEach var="activity" items="${userActivityList}">
                    <tr>
                      <th><fmt:formatDate value="${activity.activityDate}" pattern="yyyy/MM/dd HH:mm" /></th>
                      <c:if test="${activity.activityCode == '1'}"><td><span class="label success">New!!</span>新規ユーザー登録を行いました。</td></c:if>
                      <c:if test="${activity.activityCode == '2'}"><td><span class="label notice">Update!</span>再生回数情報をアップロードしました。</td></c:if>
                      <c:if test="${activity.activityCode == '3'}"><td><span class="label important">Congraturaions!!</span>再生回数が、<c:out value="${activity.activityInfo['overCnt']}"/>回を超えました。</td></c:if>
                      <c:if test="${activity.activityCode == '4'}"><td><span class="label important">Congraturaions!!</span>アーティスト「<c:out value="${activity.activityInfo['artistName']}"/>」の再生回数ランキング1位になりました。</td></c:if>
                      <c:if test="${activity.activityCode == '5'}"><td><span class="label notice">Update!!</span>再生回数情報「<c:out value="${activity.activityInfo['label']}"/>」に日記を書きました。</td></c:if>
                      <c:if test="${activity.activityCode == '6'}"><td><span class="label notice">Update!!</span>再生回数情報「<c:out value="${activity.activityInfo['label_from']}"/>」～「<c:out value="${activity.activityInfo['label_to']}"/>」に差分日記を書きました。</td></c:if>
                    </tr>
                    </c:forEach>
                  </tbody>
                </table>
                  </div>
                </div>
              </div><!-- ユーザーページTOP　ここまで -->

              <div class="tab-pane" id="saboom-list"><!-- 再生回数情報参照ページ　ここから -->

                <div class="section-orange">
                <div class="heading">
                <h3><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >アップロード済みの再生回数情報</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" />の再生回数情報</c:if></h3>
                </div>
                  <div class="content" align="center">
                    <c:if test="${fn:length(dataLabelList) == 0}" var="flg" />
                    <c:if test="${flg}" >
                      再生回数情報がアップロードされていません。
                    </c:if>
                    <c:if test="${!flg}" >
                      <label for="normalSelect">表示する再生回数情報</label>
                      <div class="input" align="right">
                        <select id="normalSelect" name="normalSelect"">
                          <c:forEach var="label" items="${dataLabelList}" varStatus="status">
                          <option value="<c:out value="${dataValueList[status.index]}"/>"><c:out value="${label}"/></option>
                          </c:forEach>
                        </select>
                        <button class="btn success" id="normalViewButton">表示</button> <c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" ><button data-controls-modal="modal-from-dom2" data-backdrop="true" data-keyboard="true" class="btn danger">削除</button></c:if>
                      </div>
                    </c:if>
                    <br>
                    <div id="loadingZoneNormalView">
                    </div>
                    <div id="normalViewInfoZone" style="display:none">
                    </div>
                    <div id="normalViewZone" style="display:none">
                    <div id='graph_NormalPlayCount_songs' style='width:500px; height:600px;'></div>
                    <br>
                    <div id='normalView_SongTable'></div>
                    <hr>
                    <div id='graph_NormalPlayCount_artist' style='width:500px; height:600px;'></div>
                    <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                    <br>
                    <div id='normalView_ArtistTable'></div>
                    <div id='normalView_amazonArea'></div>
                    <br>
                    </div>
                    <div id="normalViewDiaryZone" style="display:none">
                    <table class="condensed-table">
                      <tbody>
                        <tr>
                          <th>この情報に付けられた日記</th>
                          <td></td>
                        </tr>
                      </tbody>
                    </table>
                    <c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" />
                    <c:if test="${flg}" >
                    <div align="left"><input type="text" placeholder="タイトル" name="diaryTitle" id="diaryTitle" class="span7"/><br><br><div id="normalDiaryTitleError"></div>
                    <div class="input"><textarea rows="6" name="diaryContent" id="diaryContent" class="xxlarge" placeholder="日記の本文を入力" ></textarea></div><br><div id="normalDiaryContentError"></div>
                      <div align="right">
                        <button class="btn success" id="saveNormalDiaryButton">日記を保存</button> <button data-controls-modal="modal-from-dom" data-backdrop="true" data-keyboard="true" class="btn danger">日記を削除</button>
                      </div>
                    </div>
                    </c:if>
                    <c:if test="${!flg}" >
                      <a href="#" class="btn info" id="view-diary-button" rel="popover" title="" data-content="">日記を見る</a>
                    </c:if>
                    </div>
                  </div>
                </div>
              </div><!-- 再生回数情報参照ページ　ここまで -->

              <div class="tab-pane" id="my-saboom"><!-- sa-boomページ　ここから -->
                <div class="section-orange">
                <div class="heading">
                <h3><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >sa-boom!!</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" /> の sa-boom!!</c:if></h3>
                </div>
                  <div class="content" align="center">
                    <c:if test="${fn:length(dataLabelList) == 0}" var="flg" />
                    <c:if test="${flg}" >
                      再生回数情報がアップロードされていません。
                    </c:if>
                    <c:if test="${!flg}" >
                      <table class="condensed-table-saboom">
                        <thead>
                          <tr>
                            <th>from（サ・ブーン元）</th>
                            <th></th>
                            <th>to（サ・ブーン先）</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>
                              <select id="saboomFromSelect" name="saboomFromSelect"">
                                <c:forEach var="label" items="${dataLabelList}" varStatus="status">
                                <option value="<c:out value="${dataValueList[status.index]}"/>"><c:out value="${label}"/></option>
                                </c:forEach>
                              </select>
                            </td>
                            <td><img src="../images/saboom_arrow.png"></td>
                            <td>
                              <select id="saboomToSelect" name="saboomToSelect"">
                                <c:forEach var="label" items="${dataLabelList}" varStatus="status">
                                <option value="<c:out value="${dataValueList[status.index]}"/>"><c:out value="${label}"/></option>
                                </c:forEach>
                              </select>
                            </td>
                          </tr>
                          <tr>
                            <td></td>
                            <td></td>
                            <td><div align="right"><button class="btn success" id="saboomViewButton">サ・ブーン！！</button></div></td>
                        </tbody>
                      </table>
                    </c:if>
                    <div id="saboomSelectErrorZone"></div>
                    <br>
                    <div id="loadingZoneSaboomView">
                    </div>
                    <div id="saboomViewInfoZone" style="display:none">
                    </div>
                    <div id="saboomViewZone" style="display:none">
                      <div id='graph_SabunPlayCount_songs' style='width:500px; height:600px;'></div>
                      <br>
                      <div id='sabunView_SongTable'></div>
                      <hr>
                      <div id='graph_HikakuPlayCount_songs' style='width:500px; height:600px;'></div>
                      <br>
                      <div id='hikakuView_SongTable'></div>

                      <hr>

                      <div id='graph_SabunPlayCount_artist' style='width:500px; height:600px;'></div>
                      <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                      <br>
                      <div id='sabunView_ArtistTable'></div>
                      <br>
                      <hr>
                      <div id='graph_HikakuPlayCount_artist' style='width:500px; height:600px;'></div>
                      <div align="right"><span class="help-block">グラフをクリックすると、そのアーティストの検索ページに移動します。</span></div>
                      <br>
                      <div id='hikakuView_ArtistTable'></div>
                      <br>
                    </div>
                    <div id="saboomViewDiaryZone" style="display:none">
                    <table class="condensed-table">
                      <tbody>
                        <tr>
                          <th>この情報に付けられた日記</th>
                          <td></td>
                        </tr>
                      </tbody>
                    </table>
                    <c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" />
                    <c:if test="${flg}" >
                    <div align="left"><input type="text" placeholder="タイトル" name="saboomDiaryTitle" id="saboomDiaryTitle" class="span7"/><br><br><div id="saboomDiaryTitleError"></div>
                    <div class="input"><textarea rows="6" name="saboomDiaryContent" id="saboomDiaryContent" class="xxlarge" placeholder="日記の本文を入力" ></textarea></div><br><div id="saboomDiaryContentError"></div>
                      <div align="right">
                        <button class="btn success" id="saveSaboomDiaryButton">日記を保存</button> <button data-controls-modal="modal-from-dom3" data-backdrop="true" data-keyboard="true" class="btn danger">日記を削除</button>
                      </div>
                    </div>
                    </c:if>
                    <c:if test="${!flg}" >
                      <a href="#" class="btn info" id="view-saboom-diary-button" rel="popover" title="" data-content="">日記を見る</a>
                    </c:if>
                    </div>
                  </div>
                </div>
              </div><!-- sa-boomページ　ここまで -->

              <div class="tab-pane" id="settings">
                <div class="section-orange">
                <div class="heading">
                <h3><c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" /><c:if test="${flg}" >設定</c:if><c:if test="${!flg}" ><c:out value="${loginID_this_page}" /> さんに メッセージを送る</c:if></h3>
                </div>
                  <div class="content" align="center">
                    <c:if test="${flg}" >
                      <table class="condensed-table">
                        <tbody>
                          <tr>
                            <th>ログインID</th>
                            <td><c:out value="${sessionScope.loginID}" /></td>
                          </tr>
                          <tr>
                            <th>URL<br>（ブログなど）</th>
                            <td><input type="url" placeholder="URL（ブログなど）を入力" name="inputUrl" id="inputUrl" class="span7" value="<c:out value="${requestUser.url}" />"/></td>
                          </tr>
                          <tr>
                            <th>自己紹介</th>
                            <td>
                              <div class="input"><textarea rows="3" name="inputIntroduction" id="inputIntroduction" class="xlarge" placeholder="自己紹介を入力" width="150px"><c:out value="${requestUser.introduction}" /></textarea></div>
                            </td>
                          </tr>
                          <tr>
                            <th>プライベートモード</th>
                            <td>
                              <div class="input">
                                <ul class="inputs-list">
                                  <li>
                                    <label>
                                      <c:if test="${requestUser.isPrivate}" >
                                        <input type="checkbox" value="cb-private" id="cb-private" name="privateModeCheckBox" checked/>
                                      </c:if>
                                      <c:if test="${!requestUser.isPrivate}" >
                                        <input type="checkbox" value="cb-private" id="cb-private" name="privateModeCheckBox"/>
                                      </c:if>
                                      <span>プライベートモードをオン</span>
                                    </label>
                                  </li>
                                </ul>
                              </div>
                              <span class="help-block">プライベートモードでは、再生回数情報・アクティビティなど<br>全てが非公開となります。<br>Twitterへの投稿も行われません。</span>
                            </td>
                          </tr>
                          <tr>
                            <th>Twitter連携</th>
                            <td>
                              <img src="../images/twitter_32.png">
                              <c:if test="${'' == requestUser.twitterAccessToken}" >
                              <a href="../twitterOauth">未連携・連携する</a>
                              <div class="input">
                                <ul class="inputs-list">
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption1}" >
                                      <input type="checkbox" value="tweet-option1" id="tweet-option1" name="tweetCheckBox1" disabled="" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption1}" >
                                      <input type="checkbox" value="tweet-option1" id="tweet-option1" name="tweetCheckBox1" disabled="" />
                                    </c:if>
                                      <span>「sa-boom!!に、再生回数情報『』をアップロードしたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption2}" >
                                      <input type="checkbox" value="tweet-option2" id="tweet-option2" name="tweetCheckBox2" disabled="" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption2}" >
                                      <input type="checkbox" value="tweet-option2" id="tweet-option2" name="tweetCheckBox2" disabled="" />
                                    </c:if>
                                      <span>「私のiTunes 総再生回数がxx回を超えたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption3}" >
                                      <input type="checkbox" value="tweet-option3" id="tweet-option3" name="tweetCheckBox3" disabled="" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption3}" >
                                      <input type="checkbox" value="tweet-option3" id="tweet-option3" name="tweetCheckBox3" disabled="" />
                                    </c:if>
                                      <span>「アーティスト『』の私の総再生回数が、sa-boom!!で一位になったよ！」（未実装）</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption4}" >
                                      <input type="checkbox" value="tweet-option4" id="tweet-option4" name="tweetCheckBox4" disabled="" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption4}" >
                                      <input type="checkbox" value="tweet-option4" id="tweet-option4" name="tweetCheckBox4" disabled="" />
                                    </c:if>
                                      <span>「再生回数情報『』に対して、日記を書いたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption5}" >
                                      <input type="checkbox" value="tweet-option5" id="tweet-option5" name="tweetCheckBox5" disabled="" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption5}" >
                                      <input type="checkbox" value="tweet-option5" id="tweet-option5" name="tweetCheckBox5" disabled="" />
                                    </c:if>
                                      <span>「再生回数情報『』から『』の差分情報に対して、日記を書いたよ！」</span>
                                    </label>
                                  </li>
                                </ul>
                              </div>
                              </c:if>
                              <c:if test="${'' != requestUser.twitterAccessToken}" >
                              連携済み（<a href="../twitterRelease">連携解除</a>）
                              <div class="input">
                                <ul class="inputs-list">
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption1}" >
                                      <input type="checkbox" value="tweet-option1" id="tweet-option1" name="tweetCheckBox1" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption1}" >
                                      <input type="checkbox" value="tweet-option1" id="tweet-option1" name="tweetCheckBox1" />
                                    </c:if>
                                      <span>「sa-boom!!に、再生回数情報『』をアップロードしたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption2}" >
                                      <input type="checkbox" value="tweet-option2" id="tweet-option2" name="tweetCheckBox2" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption2}" >
                                      <input type="checkbox" value="tweet-option2" id="tweet-option2" name="tweetCheckBox2" />
                                    </c:if>
                                      <span>「私のiTunes 総再生回数がxx回を超えたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption3}" >
                                      <input type="checkbox" value="tweet-option3" id="tweet-option3" name="tweetCheckBox3" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption3}" >
                                      <input type="checkbox" value="tweet-option3" id="tweet-option3" name="tweetCheckBox3" />
                                    </c:if>
                                      <span>「アーティスト『』の私の総再生回数が、sa-boom!!で一位になったよ！」（未実装）</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption4}" >
                                      <input type="checkbox" value="tweet-option4" id="tweet-option4" name="tweetCheckBox4" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption4}" >
                                      <input type="checkbox" value="tweet-option4" id="tweet-option4" name="tweetCheckBox4" />
                                    </c:if>
                                      <span>「再生回数情報『』に対して、日記を書いたよ！」</span>
                                    </label>
                                  </li>
                                  <li>
                                    <label>
                                    <c:if test="${requestUser.isTweetOption5}" >
                                      <input type="checkbox" value="tweet-option5" id="tweet-option5" name="tweetCheckBox5" checked/>
                                    </c:if>
                                    <c:if test="${!requestUser.isTweetOption5}" >
                                      <input type="checkbox" value="tweet-option5" id="tweet-option5" name="tweetCheckBox5" />
                                    </c:if>
                                      <span>「再生回数情報『』から『』の差分情報に対して、日記を書いたよ！」</span>
                                    </label>
                                  </li>
                                </ul>
                              </div>
                              </c:if>
                            </td>
                          </tr>
                          <tr>
                            <th>アカウントの削除</th>
                            <td><span class="help-block">お手数ですが、アカウントの削除はsa-boom clientより行なって下さい。</span></td>
                          </tr>
                          <tr>
                            <th></th>
                            <td><div id="saveSettingsMessage"></div><div align="right"><button id="saveSettingsButton" class="btn primary">変更を保存する</button></div></td>
                          </tr>
                        </tbody>
                      </table>
                    </c:if>
                    <c:if test="${!flg}" >
                      <div class="content" align="center">
                        <div align="left">
                          <input type="text" placeholder="送信者名を入力（省略可）" name="messageSender" id="messageSender" class="span7" value="${sessionScope.loginID}"/><br><br>
                          <div id="messageSenderError"></div>
                          <div class="input">
                            <textarea rows="3" name="sendMessageContent" id="sendMessageContent" class="xxlarge" placeholder="メッセージの本文を入力" ></textarea>
                          </div><br>
                          <div id="sendMessageContentError"></div>
                            <div align="right">
                              <button class="btn success" id="sendMessageButton">メッセージを送信</button>
                            </div>
                        </div>
                      </div>
                    </c:if>
                  </div>
                </div>

                <c:if test="${sessionScope.loginID == loginID_this_page}" var="flg" />
                <c:if test="${flg}" >
                <div class="section-orange">
                <div class="heading">
                <h3><c:out value="${sessionScope.loginID}" /> さん宛のメッセージ</h3>
                </div>
                  <div class="content">
                    <c:if test="${fn:length(messageLabelList) == 0}" var="flg" />
                    <c:if test="${flg}" >
                      メッセージはありません。
                    </c:if>
                    <c:if test="${!flg}" >
                      <div class="input" align="right">
                        <select id="messageSelect" name="messageSelect"" class="span7">
                          <option value="">メッセージを選択して下さい</option>
                          <c:forEach var="label" items="${messageLabelList}" varStatus="status">
                          <option value="<c:out value="${messageValueList[status.index]}"/>"><c:out value="${label}"/></option>
                          </c:forEach>
                        </select>
                        <button data-controls-modal="modal-from-dom4" data-backdrop="true" data-keyboard="true" class="btn danger">選択中のメッセージを削除</button>
                      </div>
                      <hr>
                      <div align="center">
                      <a href="#" rel="popover" title="－" data-content="（メッセージを選択して下さい）" id="messageIconHref"><img src="../images/message_icon.png" id="messageIcon"></a>
                      <div id="messageSenderNameArea"></div>
                      </div>
                    </c:if>
                  </div>
                </div>
                </c:if>
                </c:if>
              </div>
            </div>
          </div>
        </div>
      </div>







          <div id="modal-from-dom" class="modal hide fade">
            <div class="modal-header">
              <a href="#" class="close">&times;</a>
              <h3>日記の削除の確認</h3>
            </div>
            <div class="modal-body">

              <p>この日記を削除します。本当によろしいですか？</p>
              <div id="deleteModalLoadingZone" align="center"></div>
            </div>
            <div class="modal-footer">
              <a href="#" class="close"><button class="btn danger" id="deleteNormalDiaryButton">削除</button></a>
              <a href="#" class="close"><button class="btn">キャンセル</button></a>
            </div>
          </div>

          <div id="modal-from-dom2" class="modal hide fade">
            <div class="modal-header">
              <a href="#" class="close">&times;</a>
              <h3>再生回数情報の削除の確認</h3>
            </div>
            <div class="modal-body">

              <p>この再生回数情報を削除します。</p>
              <p>これに紐付く日記情報も削除されます。本当によろしいですか？</p>
              <div id="deleteModalLoadingZone2" align="center"></div>
            </div>
            <div class="modal-footer">
              <a href="#" class="close"><button class="btn danger" id="deleteInfoButton">削除</button></a>
              <a href="#" class="close"><button class="btn">キャンセル</button></a>
            </div>
          </div>

          <div id="modal-from-dom3" class="modal hide fade">
            <div class="modal-header">
              <a href="#" class="close">&times;</a>
              <h3>日記の削除の確認</h3>
            </div>
            <div class="modal-body">

              <p>この日記を削除します。本当によろしいですか？</p>
              <div id="deleteModalLoadingZone3" align="center"></div>
            </div>
            <div class="modal-footer">
              <a href="#" class="close"><button class="btn danger" id="deleteSaboomDiaryButton">削除</button></a>
              <a href="#" class="close"><button class="btn">キャンセル</button></a>
            </div>
          </div>

          <div id="modal-from-dom4" class="modal hide fade">
            <div class="modal-header">
              <a href="#" class="close">&times;</a>
              <h3>メッセージの削除の確認</h3>
            </div>
            <div class="modal-body">

              <p>選択中のメッセージを削除します。本当によろしいですか？</p>
              <div id="deleteModalLoadingZone4" align="center"></div>
            </div>
            <div class="modal-footer">
              <a href="#" class="close"><button class="btn danger" id="deleteMessageButton">削除</button></a>
              <a href="#" class="close"><button class="btn">キャンセル</button></a>
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
