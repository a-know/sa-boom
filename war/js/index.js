var firstLoaded = false;
var secondLoaded = false;
var thirdLoaded = false;

$(function(){

	//総再生回数ランキング（ユーザー）用のグラフ生成
	$('#graph_TotalPlayCountRanking_User').empty();
	$('#graph_TotalPlayCountRanking_User').css('display', 'none');

	var controll_data_user_graph = {};
	controll_data_user_graph.functionCode = '5';

	var loginIDlist_user = new Array();
	var data_playcount_user = new Array();
	var tick_data_id_user = new Array();

	var user_count = 0;

	$.ajax({
		type : 'GET',
		url : '/graph/GetGraphData',
		data : controll_data_user_graph,
		cache : false,
		dataType : 'json',

		success : function(json) {
			$.each(json.reverse(), function(i, UserRankingGraphData){
				tempArray = new Array();
				tempArray.push(UserRankingGraphData.playcount);
				tempArray.push(i + 1);
				data_playcount_user.push(tempArray);
				tick_data_id_user.push(UserRankingGraphData.loginIDwithRank);
				loginIDlist_user.push(UserRankingGraphData.loginID);
				user_count++;
			});

			$('#graph_TotalPlayCountRanking_User').css('display','block');
			$('#graph_TotalPlayCountRanking_User').css('height', '600px');

			$.jqplot(
					'graph_TotalPlayCountRanking_User',[data_playcount_user],
					{
						title: "総再生回数ランキング＜ユーザー別＞",
						seriesColors: [ "#F5CBCB", "#F2BABA", "#EEA7A7", "#EB9595", "#E88282", "#E57171", "#E15E5E", "#DE4D4D", "#DB3A3A", "#d82727"],
						seriesDefaults: {
							renderer: $.jqplot.BarRenderer,
							pointLabels: { show: true },
							rendererOptions: {
								showDataLabels: true,
								barWidth: 30,
								barDirection: 'horizontal',
								varyBarColor: true
							}
						},
						axes: {
							yaxis: {
								renderer: $.jqplot.CategoryAxisRenderer,
								ticks: tick_data_id_user,
								tickRenderer: $.jqplot.CanvasAxisTickRenderer,
								tickOptions: {
									angle: -30
								}
							},
							xaxis: {
								tickRenderer: $.jqplot.CanvasAxisTickRenderer,
								tickOptions: {
									angle: -30
								}
							}
						}
					}
			)
		},
		complete : function() {
			//通信終了
		}
	});

	//総再生回数ランキング（アーティスト）用のグラフ生成
	$('#graph_TotalPlayCountRanking_Artist').empty();
	$('#graph_TotalPlayCountRanking_Artist').css('display', 'none');

	var controll_data_artist_graph = {};
	controll_data_artist_graph.functionCode = '6';

	var loginIDlist_artist = new Array();
	var data_playcount_artist = new Array();
	var tick_data_id_artist = new Array();

	$.ajax({
		type : 'GET',
		url : '/graph/GetGraphData',
		data : controll_data_artist_graph,
		cache : false,
		dataType : 'json',

		success : function(json) {
			$.each(json.reverse(), function(i, ArtistRankingGraphData){
				tempArray = new Array();
				tempArray.push(ArtistRankingGraphData.playcount);
				tempArray.push(i + 1);
				data_playcount_artist.push(tempArray);
				tick_data_id_artist.push(ArtistRankingGraphData.artistNameWithRank);
				loginIDlist_artist.push(ArtistRankingGraphData.artistName);
			});

			$('#graph_TotalPlayCountRanking_Artist').css('display', '');

			$.jqplot(
					'graph_TotalPlayCountRanking_Artist',[data_playcount_artist],
					{
						title: "総再生回数ランキング＜アーティスト別＞",
						seriesColors: [ "#F5CBCB", "#F2BABA", "#EEA7A7", "#EB9595", "#E88282", "#E57171", "#E15E5E", "#DE4D4D", "#DB3A3A", "#d82727"],
						seriesDefaults: {
							renderer: $.jqplot.BarRenderer,
							pointLabels: { show: true },
							rendererOptions: {
								showDataLabels: true,
								barWidth: 30,
								barDirection: 'horizontal',
								varyBarColor: true
							}
						},
						axes: {
							yaxis: {
								renderer: $.jqplot.CategoryAxisRenderer,
								ticks: tick_data_id_artist,
								tickRenderer: $.jqplot.CanvasAxisTickRenderer,
								tickOptions: {
									angle: -30
								}
							},
							xaxis: {
								tickRenderer: $.jqplot.CanvasAxisTickRenderer,
								tickOptions: {
									angle: -30
								}
							}
						}
					}
			)
		},
		complete : function() {
			//通信終了
		}
	});

	$('#graph_TotalPlayCountRanking_User').bind('jqplotDataClick',
			function (ev, seriesIndex, pointIndex, data) {
		location.href="/user/" + loginIDlist_user[pointIndex];
	}
	);

	$('#graph_TotalPlayCountRanking_Artist').bind('jqplotDataClick',
			function (ev, seriesIndex, pointIndex, data) {
		location.href="/artistSearch?artistName=" + loginIDlist_artist[pointIndex];
	}
	);
});
