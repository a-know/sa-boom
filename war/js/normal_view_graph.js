$(function(){
	$('#normalViewButton').click(function(){
		//var
		var controll_data_graph = {};
		var controll_data_diary = {};
		var data_song = new Array();
		var tick_data_song = new Array();
		var tempArray_song = new Array();
		var normalView_songTable = "<table class='condensed-table'><thead><tr><th>順位</th><th>曲名</th><th>アーティスト名</th><th>レート</th><th>再生回数</th></tr><tbody>";
		var data_artist = new Array();
		var tick_data_artist = new Array();
		var tempArray_artist = new Array();
		var normalView_artistTable = "<table class='condensed-table'><thead><tr><th>順位</th><th>アーティスト名</th><th>レート</th><th>再生回数</th></tr></thead><tbody>";
		var normalView_amazonArea = "<div class='row'>";

		//initialize
		$('#normalViewInfoZone').css('display', 'none');
		$('#normalViewZone').css('display', 'none');
		$('#normalViewDiaryZone').css('display', 'none');
		$('#graph_NormalPlayCount_songs').empty();
		$('#graph_NormalPlayCount_artist').empty();
		$('#loadingZoneNormalView').html("<br><br><br><img src='../images/loading_big.gif'>");

		//html edit
		var yymmddhhmm = $('#normalSelect option:selected').val();
		var normalViewInfo = "<table class='condensed-table'><tbody><tr><th>再生回数情報の見出し</th><td>";
		normalViewInfo = normalViewInfo.concat($('#normalSelect option:selected').text());
		normalViewInfo = normalViewInfo.concat("</td></tr><tr><th>再生回数情報の採取日</th><td>20");
		normalViewInfo = normalViewInfo.concat(yymmddhhmm.slice(0,2));
		normalViewInfo = normalViewInfo.concat("/");
		normalViewInfo = normalViewInfo.concat(yymmddhhmm.slice(2,4));
		normalViewInfo = normalViewInfo.concat("/");
		normalViewInfo = normalViewInfo.concat(yymmddhhmm.slice(4,6));
		normalViewInfo = normalViewInfo.concat(" ");
		normalViewInfo = normalViewInfo.concat(yymmddhhmm.slice(6,8));
		normalViewInfo = normalViewInfo.concat(":");
		normalViewInfo = normalViewInfo.concat(yymmddhhmm.slice(8,10));
		normalViewInfo = normalViewInfo.concat("</td></tr><tr><th>各種ランキング情報</th><td></td></tr></tbody></table>");

		//prepare for ajax
		controll_data_graph.functionCode = '2';
		controll_data_graph.targetLoginID = loginID_this_page;
		controll_data_graph.normalViewValue = $('#normalSelect option:selected').val();
		controll_data_diary.loginID = loginID_this_page;
		controll_data_diary.from = $('#normalSelect option:selected').val();
		controll_data_diary.to = $('#normalSelect option:selected').val();

		if(loginID_this_page == loginID_in_session){
			$('#normalDiaryContentError').empty();
		}

		//start ajax
		$.ajax({
			type : 'GET',
			url : '/loadDiary',
			data : controll_data_diary,
			cache : false,
			dataType : 'json',

			success : function(json) {
				if(json.title == ''){
					if(loginID_this_page == loginID_in_session){//見ているページがログイン者自身のページだったら
						$('#diaryTitle').val('');
						$('#diaryContent').val('');
					}else{
						$('#view-diary-button').attr('title','－');
						$('#view-diary-button').attr('data-content','（まだ日記は書かれていません。）');
					}
				}else{
					if(loginID_this_page == loginID_in_session){//見ているページがログイン者自身のページだったら
						$('#diaryTitle').val(json.title);
						$('#diaryContent').val(json.content);
					}else{
						$('#view-diary-button').attr('title',json.title);
						$('#view-diary-button').attr('data-content',json.content);
					}
				}
			},
			complete : function() {
				//通信終了
			}
		});

		$.ajax({
			type : 'GET',
			url : '/graph/GetGraphData',
			data : controll_data_graph,
			cache : false,
			dataType : 'json',

			success : function(json) {
				$.each(json.reverse(), function(i, NormalPlayCountGraphByUser){
					tempArray_song = new Array();
					tempArray_song.push(NormalPlayCountGraphByUser.playcount_bySong);
					tempArray_song.push(i + 1);
					data_song.push(tempArray_song);
					tick_data_song.push(NormalPlayCountGraphByUser.songName_bySong);

					tempArray_artist = new Array();
					tempArray_artist.push(NormalPlayCountGraphByUser.playcount_byArtist);
					tempArray_artist.push(i + 1);
					data_artist.push(tempArray_artist);
					tick_data_artist.push(NormalPlayCountGraphByUser.artistName_byArtist);
				});
				$.each(json.reverse(), function(i, NormalPlayCountGraphByUser){
					normalView_songTable = normalView_songTable.concat("<tr><th>");
					normalView_songTable = normalView_songTable.concat(NormalPlayCountGraphByUser.rank_bySong);
					normalView_songTable = normalView_songTable.concat("</th><td>");
					normalView_songTable = normalView_songTable.concat(NormalPlayCountGraphByUser.songName_bySong);
					normalView_songTable = normalView_songTable.concat("</td><td>");
					normalView_songTable = normalView_songTable.concat(NormalPlayCountGraphByUser.artist_bySong);
					normalView_songTable = normalView_songTable.concat("</td><td>");
					normalView_songTable = normalView_songTable.concat(NormalPlayCountGraphByUser.rate_bySong);
					normalView_songTable = normalView_songTable.concat("</td><td>")
					normalView_songTable = normalView_songTable.concat(NormalPlayCountGraphByUser.playcount_bySong);
					normalView_songTable = normalView_songTable.concat("</td></tr>");

					normalView_artistTable = normalView_artistTable.concat("<tr><th>");
					normalView_artistTable = normalView_artistTable.concat(NormalPlayCountGraphByUser.rank_byArtist);
					normalView_artistTable = normalView_artistTable.concat("</th><td>");
					normalView_artistTable = normalView_artistTable.concat(NormalPlayCountGraphByUser.artistName_byArtist);
					normalView_artistTable = normalView_artistTable.concat("</td><td>");
					normalView_artistTable = normalView_artistTable.concat(NormalPlayCountGraphByUser.rate_byArtist);
					normalView_artistTable = normalView_artistTable.concat("</td><td>");
					normalView_artistTable = normalView_artistTable.concat(NormalPlayCountGraphByUser.playcount_byArtist);
					normalView_artistTable = normalView_artistTable.concat("</td></tr>");

					normalView_amazonArea = normalView_amazonArea.concat("<div class='span2'>");
					if(NormalPlayCountGraphByUser.url_byArtist == ""){
						normalView_amazonArea = normalView_amazonArea.concat("<img src='");
						normalView_amazonArea = normalView_amazonArea.concat(NormalPlayCountGraphByUser.imageUrl_byArtist);
						normalView_amazonArea = normalView_amazonArea.concat("'></div>");
					}else{
						normalView_amazonArea = normalView_amazonArea.concat("<a href='");
						normalView_amazonArea = normalView_amazonArea.concat(NormalPlayCountGraphByUser.url_byArtist);
						normalView_amazonArea = normalView_amazonArea.concat("'><img src='");
						normalView_amazonArea = normalView_amazonArea.concat(NormalPlayCountGraphByUser.imageUrl_byArtist);
						normalView_amazonArea = normalView_amazonArea.concat("'></a></div>");
					}

					if(i == 4){
						normalView_amazonArea = normalView_amazonArea.concat("</div><div class='row'>");
					}

					if(i==9){
						normalView_songTable = normalView_songTable.concat("</tbody></table>");
						normalView_artistTable = normalView_artistTable.concat("</tbody></table>");
						normalView_amazonArea = normalView_amazonArea.concat("</div>");

						$('#normalView_SongTable').html(normalView_songTable);
						$('#normalView_ArtistTable').html(normalView_artistTable);
						$('#normalView_amazonArea').html(normalView_amazonArea);

						$('#loadingZoneNormalView').empty();
						$('#normalViewInfoZone').html(normalViewInfo);

						$('#normalViewInfoZone').css('display', '');
						$('#normalViewZone').css('display', '');
						$('#normalViewDiaryZone').css('display', '');
					}
				});

				$.jqplot(
						'graph_NormalPlayCount_songs',[data_song],
						{
							title: '再生回数ランキング＜曲別＞',
							seriesColors: [ "#F5CBCB", "#F2BABA", "#EEA7A7", "#EB9595", "#E88282", "#E57171", "#E15E5E", "#DE4D4D", "#DB3A3A", "#d82727"],
							seriesDefaults: {
								renderer: $.jqplot.BarRenderer,
								pointLabels: { show: true },
								rendererOptions: {
									showDataLabels: true,
									barDirection: 'horizontal',
									varyBarColor: true
								}
							},
							axes: {
								yaxis: {
									renderer: $.jqplot.CategoryAxisRenderer,
									ticks: tick_data_song,
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
							},
							highlighter: {showTooltip: false}
						}
				);

				$.jqplot(
						'graph_NormalPlayCount_artist',[data_artist],
						{
							title: '再生回数ランキング＜アーティスト＞',
							seriesColors: [ "#F5CBCB", "#F2BABA", "#EEA7A7", "#EB9595", "#E88282", "#E57171", "#E15E5E", "#DE4D4D", "#DB3A3A", "#d82727"],
							seriesDefaults: {
								renderer: $.jqplot.BarRenderer,
								pointLabels: { show: true },
								rendererOptions: {
									showDataLabels: true,
									barDirection: 'horizontal',
									varyBarColor: true
								}
							},
							axes: {
								yaxis: {
									renderer: $.jqplot.CategoryAxisRenderer,
									ticks: tick_data_artist,
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
							},
							highlighter: {showTooltip: false}
						}
				);

			},
			complete : function() {
				//通信終了
			}
		});
		//for graph click
		$('#graph_NormalPlayCount_artist').bind('jqplotDataClick',
				function (ev, seriesIndex, pointIndex, data) {
			location.href="/artistSearch?artistName=" + tick_data_artist[pointIndex];
		}
		);
	});
});