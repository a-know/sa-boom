$(function(){
	$('#saboomViewButton').click(function(){
		//initialize
		$.jqplot.config.enablePlugins = true;
	    $('#saboomSelectErrorZone').empty();

	    if(loginID_this_page == loginID_in_session){
	    	$('#saboomDiaryContentError').empty();
	    }
	    //get value
	    var from_yymmddhhmm = $('#saboomFromSelect option:selected').val();
	    var to_yymmddhhmm = $('#saboomToSelect option:selected').val();

	    //validating
	    if(from_yymmddhhmm == to_yymmddhhmm){
	    	$('#saboomSelectErrorZone').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>同じ情報をサ・ブーンすることはできません。</p></div>");
	    	return;
	    }else if(from_yymmddhhmm > to_yymmddhhmm){
	    	$('#saboomSelectErrorZone').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>サ・ブーン先情報よりも古い情報を、サ・ブーン元情報として選んで下さい。</p></div>");
	    	return;
	    }

	    //show loading image
	    $('#loadingZoneSaboomView').html("<br><br><br><img src='../images/loading_big.gif'>");

	    //change display property
	    $('#saboomViewInfoZone').css('display' ,'none');
	    $('#saboomViewZone').css('display' ,'none');
	    $('#saboomViewDiaryZone').css('display' ,'none');

	    //prepare for show graph
	    $('#graph_SabunPlayCount_songs').empty();
	    $('#graph_HikakuPlayCount_songs').empty();
	    $('#graph_SabunPlayCount_artist').empty();
	    $('#graph_HikakuPlayCount_artist').empty();

	    //ajax通信・グラフ用のコントロール情報作成
	    var controll_data_graph = {};
	    controll_data_graph.functionCode = '3';// 3 : saboom用グラフ作成
	    controll_data_graph.targetLoginID = loginID_this_page;
	    controll_data_graph.from = from_yymmddhhmm;
	    controll_data_graph.to = to_yymmddhhmm;

	    //ajax通信・日記用のコントロール情報作成
	    var controll_data_diary = {};
	    controll_data_diary.loginID = loginID_this_page;
	    controll_data_diary.from = from_yymmddhhmm;
	    controll_data_diary.to = to_yymmddhhmm;

	    //グラフ作成用の各種変数
	    var data_song_saboom = new Array();
	    var tick_data_song_saboom = new Array();
	    var data_song_hikaku_from = new Array();
	    var data_song_hikaku_to = new Array();
	    var tick_data_song_hikaku = new Array();
	    var tempArray_song = new Array();
	    var tempArray_song_hikaku = new Array();

	    var data_artist_saboom = new Array();
	    var tick_data_artist_saboom = new Array();
	    var data_artist_hikaku_from = new Array();
	    var data_artist_hikaku_to = new Array();
	    var tick_data_artist_hikaku = new Array();
	    var tempArray_artist = new Array();
	    var tempArray_artist_hikaku = new Array();

	    //テーブル作成用の各種変数
	    var saboomView_songSabunTable = "<table class='condensed-table'><thead><tr><th>順位</th><th>曲名</th><th>アーティスト名</th><th>レート</th><th>期間内再生回数</th></tr><tbody>";
	    var saboomView_artistSabunTable = "<table class='condensed-table'><thead><tr><th>順位</th><th>アーティスト名</th><th>レート</th><th>期間内再生回数</th></tr></thead><tbody>";
	    var saboomView_songHikakuTable = "<table class='condensed-table'><thead><tr><th>順位(前回)</th><th>曲名</th><th>アーティスト名</th><th>レート</th><th>再生回数(増分)</th></tr><tbody>";
	    var saboomView_artistHikakuTable = "<table class='condensed-table'><thead><tr><th>順位(前回)</th><th>アーティスト名</th><th>レート</th><th>再生回数(増分)</th></tr></thead><tbody>";

	    var saboomViewInfo = "<table class='condensed-table'><tbody><tr><th>再生回数情報（元）の見出し</th><td>";
	    saboomViewInfo = saboomViewInfo.concat($('#saboomFromSelect option:selected').text());
	    saboomViewInfo = saboomViewInfo.concat("</td></tr><tr><th>再生回数情報（元）の採取日</th><td>20");
	    saboomViewInfo = saboomViewInfo.concat(from_yymmddhhmm.slice(0,2));
	    saboomViewInfo = saboomViewInfo.concat("/");
	    saboomViewInfo = saboomViewInfo.concat(from_yymmddhhmm.slice(2,4));
	    saboomViewInfo = saboomViewInfo.concat("/");
	    saboomViewInfo = saboomViewInfo.concat(from_yymmddhhmm.slice(4,6));
	    saboomViewInfo = saboomViewInfo.concat(" ");
	    saboomViewInfo = saboomViewInfo.concat(from_yymmddhhmm.slice(6,8));
	    saboomViewInfo = saboomViewInfo.concat(":");
	    saboomViewInfo = saboomViewInfo.concat(from_yymmddhhmm.slice(8,10));
	    saboomViewInfo = saboomViewInfo.concat("</td></tr><tr><th></th><td>");
	    saboomViewInfo = saboomViewInfo.concat("</td></tr><tr><th>再生回数情報（先）の見出し</th><td>");
	    saboomViewInfo = saboomViewInfo.concat($('#saboomToSelect option:selected').text());
	    saboomViewInfo = saboomViewInfo.concat("</td></tr><tr><th>再生回数情報（先）の採取日</th><td>20");
	    saboomViewInfo = saboomViewInfo.concat(to_yymmddhhmm.slice(0,2));
	    saboomViewInfo = saboomViewInfo.concat("/");
	    saboomViewInfo = saboomViewInfo.concat(to_yymmddhhmm.slice(2,4));
	    saboomViewInfo = saboomViewInfo.concat("/");
	    saboomViewInfo = saboomViewInfo.concat(to_yymmddhhmm.slice(4,6));
	    saboomViewInfo = saboomViewInfo.concat(" ");
	    saboomViewInfo = saboomViewInfo.concat(to_yymmddhhmm.slice(6,8));
	    saboomViewInfo = saboomViewInfo.concat(":");
	    saboomViewInfo = saboomViewInfo.concat(to_yymmddhhmm.slice(8,10));
	    saboomViewInfo = saboomViewInfo.concat("</td></tr><tr><th>各種ランキング情報</th><td></td></tr></tbody></table>");

	    //start ajax (diary)
	    $.ajax({
	        type : 'GET',
	        url : '/loadDiary',
	        data : controll_data_diary,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	if(json.title == ''){
	                if(loginID_this_page == loginID_in_session){//見ているページがログイン者自身のページだったら
	                	$('#saboomDiaryTitle').val('');
	                	$('#saboomDiaryContent').val('');
	                }else{
	                	$('#view-saboom-diary-button').attr('title', '－');
	                	$('#view-saboom-diary-button').attr('data-content', '（まだ日記は書かれていません。）');
	                }
	        	}else{
	        		if(loginID_this_page == loginID_in_session){//見ているページがログイン者自身のページだったら
	        			$('#saboomDiaryTitle').val(json.title);
	        			$('#saboomDiaryContent').val(json.content);
	        		}else{
	                	$('#view-saboom-diary-button').attr('title', json.title);
	                	$('#view-saboom-diary-button').attr('data-content', json.content);
	        		}
	        	}
	        },
	        complete : function() {
	          //通信終了
	        }
	    });

	    //start ajax (graph)
	    $.ajax({
	        type : 'GET',
	        url : '/graph/GetGraphData',
	        data : controll_data_graph,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	              $.each(json.reverse(), function(i, SaboomPlayCountGraphByUser){
	            	  //差分グラフ情報・曲別
	            	  tempArray_song = new Array();
	            	  tempArray_song.push(SaboomPlayCountGraphByUser.playcount_bySong);
	            	  tempArray_song.push(i + 1);
	            	  data_song_saboom.push(tempArray_song);
	            	  tick_data_song_saboom.push(SaboomPlayCountGraphByUser.songName_bySong);

	            	  //差分グラフ情報・アーティスト別
	            	  tempArray_artist = new Array();
	            	  tempArray_artist.push(SaboomPlayCountGraphByUser.playcount_byArtist);
	            	  tempArray_artist.push(i + 1);
	            	  data_artist_saboom.push(tempArray_artist);
	            	  tick_data_artist_saboom.push(SaboomPlayCountGraphByUser.artistName_byArtist);

	            	  //比較グラフ情報・曲別
	            	  tempArray_song_hikaku = new Array();
	            	  tempArray_song_hikaku.push(SaboomPlayCountGraphByUser.sabuncount_hikakuBySong);
	            	  tempArray_song_hikaku.push(i + 1);
	            	  data_song_hikaku_to.push(tempArray_song_hikaku);

	            	  tempArray_song_hikaku = new Array();
	            	  tempArray_song_hikaku.push(SaboomPlayCountGraphByUser.motocount_hikakuBySong);
	            	  tempArray_song_hikaku.push(i + 1);
	            	  data_song_hikaku_from.push(tempArray_song_hikaku);

	            	  tick_data_song_hikaku.push(SaboomPlayCountGraphByUser.songName_hikakuBySong);

	            	  //比較グラフ情報・アーティスト別
	            	  tempArray_artist_hikaku = new Array();
	            	  tempArray_artist_hikaku.push(SaboomPlayCountGraphByUser.sabuncount_hikakuByArtist);
	            	  tempArray_artist_hikaku.push(i + 1);
	            	  data_artist_hikaku_to.push(tempArray_artist_hikaku);

	            	  tempArray_artist_hikaku = new Array();
	            	  tempArray_artist_hikaku.push(SaboomPlayCountGraphByUser.motocount_hikakuByArtist);
	            	  tempArray_artist_hikaku.push(i + 1);
	            	  data_artist_hikaku_from.push(tempArray_artist_hikaku);
	            	  tick_data_artist_hikaku.push(SaboomPlayCountGraphByUser.artistName_hikakuByArtist)
	              });
	              $.each(json.reverse(), function(i, SaboomPlayCountGraphByUser){
	            	  //差分情報テーブル編集・曲別
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("<tr><th>");
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat(SaboomPlayCountGraphByUser.rank_bySong);
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("</th><td>");
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat(SaboomPlayCountGraphByUser.songName_bySong);
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("</td><td>");
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat(SaboomPlayCountGraphByUser.artist_bySong);
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("</td><td>");
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat(SaboomPlayCountGraphByUser.rate_bySong);
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("</td><td>")
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat(SaboomPlayCountGraphByUser.playcount_bySong);
	            	  saboomView_songSabunTable = saboomView_songSabunTable.concat("</td></tr>");

	            	  //差分情報テーブル・アーティスト別
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("<tr><th>");
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat(SaboomPlayCountGraphByUser.rank_byArtist);
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("</th><td>");
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat(SaboomPlayCountGraphByUser.artistName_byArtist);
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("</td><td>");
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat(SaboomPlayCountGraphByUser.rate_byArtist);
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("</td><td>");
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat(SaboomPlayCountGraphByUser.playcount_byArtist);
	            	  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("</td></tr>");

	            	  //比較情報テーブル・曲別
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("<tr><th>");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.rank_hikakuBySong_now);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("(");
	            	  if(SaboomPlayCountGraphByUser.rank_hikakuBySong_pre == 0){
	            		  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("-");
	            	  }else{
	            		  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.rank_hikakuBySong_pre);
	            	  }
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(")");
	            	  if('new' == SaboomPlayCountGraphByUser.rankImage_hikakuBySong){
	                	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("<span class='label success'>New!!</span>");
	            	  }else if('up' == SaboomPlayCountGraphByUser.rankImage_hikakuBySong){
	            		  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("<span class='label warning'>Up!</span>");
	            	  }else if('down' == SaboomPlayCountGraphByUser.rankImage_hikakuBySong){
	            		  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("<span class='label notice'>Down</span>");
	            	  }else if('stay' == SaboomPlayCountGraphByUser.rankImage_hikakuBySong){
	            		  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("<span class='label'>Stay</span>");
	            	  }
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</th><td>");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.songName_hikakuBySong);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</td><td>");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.artistName_hikakuBySong);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</td><td>");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.rate_hikakuBySong);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</td><td>")
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.playcount_hikakuBySong);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("(+");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(SaboomPlayCountGraphByUser.sabuncount_hikakuBySong);
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat(")");
	            	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</td></tr>");

	            	  //比較情報テーブル・アーティスト別
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("<tr><th>");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.rank_hikakuByArtist_now);
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("(");
	            	  if(SaboomPlayCountGraphByUser.rank_hikakuByArtist_pre == 0){
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("-");
	            	  }else{
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.rank_hikakuByArtist_pre);
	            	  }
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(")");
	            	  if('new' == SaboomPlayCountGraphByUser.rankImage_hikakuByArtist){
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("<span class='label success'>New!!</span>");
	            	  }else if('up' == SaboomPlayCountGraphByUser.rankImage_hikakuByArtist){
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("<span class='label warning'>Up!</span>");
	            	  }else if('down' == SaboomPlayCountGraphByUser.rankImage_hikakuByArtist){
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("<span class='label notice'>Down</span>");
	            	  }else if('stay' == SaboomPlayCountGraphByUser.rankImage_hikakuByArtist){
	            		  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("<span class='label'>Stay</span>");
	            	  }
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("</th><td>");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.artistName_hikakuByArtist);
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("</td><td>");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.rate_hikakuByArtist);
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("</td><td>");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.playcount_hikakuByArtist);
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("(+");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(SaboomPlayCountGraphByUser.sabuncount_hikakuByArtist);
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat(")");
	            	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("</td></tr>");

	            	  if(i==9){
	            		  saboomView_songSabunTable = saboomView_songSabunTable.concat("</tbody></table>");
	            		  saboomView_artistSabunTable = saboomView_artistSabunTable.concat("</tbody></table>");
	                	  saboomView_songHikakuTable = saboomView_songHikakuTable.concat("</tbody></table>");
	                	  saboomView_artistHikakuTable = saboomView_artistHikakuTable.concat("</tbody></table>");

	                      $('#sabunView_SongTable').html(saboomView_songSabunTable);
	                      $('#sabunView_ArtistTable').html(saboomView_artistSabunTable);
	                      $('#hikakuView_SongTable').html(saboomView_songHikakuTable);
	                      $('#hikakuView_ArtistTable').html(saboomView_artistHikakuTable);

	                      $('#loadingZoneSaboomView').empty();
	                      $('#saboomViewInfoZone').html(saboomViewInfo);

	                      $('#saboomViewInfoZone').css('display', '');
	                      $('#saboomViewZone').css('display', '');
	                      $('#saboomViewDiaryZone').css('display', '');
	            	  }
	              });

	              $.jqplot(
	                      'graph_SabunPlayCount_songs',[data_song_saboom],
	                      {
	                        title: 'サ・ブーン！！＜曲別＞',
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
	                            ticks: tick_data_song_saboom,
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
	                'graph_SabunPlayCount_artist',[data_artist_saboom],
	                {
	                  title: 'サ・ブーン！！＜アーティスト別＞',
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
	                      ticks: tick_data_artist_saboom,
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
	                'graph_HikakuPlayCount_songs',[data_song_hikaku_from,data_song_hikaku_to],
	                {
	                  title: '順位変動グラフ＜曲別＞',
	                  stackSeries: true,
	                  seriesColors: ["#d82727", "#FFA500" ],
	                  seriesDefaults: {
	                    renderer: $.jqplot.BarRenderer,
	                    pointLabels: { show: false },
	                    rendererOptions: {
	                      showDataLabels: true,
	                      barDirection: 'horizontal',
	                      varyBarColor: true
	                    }
	                  },
	                  axes: {
	                    yaxis: {
	                      renderer: $.jqplot.CategoryAxisRenderer,
	                      ticks: tick_data_song_hikaku,
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
	                  highlighter: {bringSeriesToFront:true, tooltipOffset: 10, sizeAdjust: 10, showTooltip: true, tooltipLocation: 's', tooltipAxes: 'x'}
	                }
	              );

	              $.jqplot(
	                'graph_HikakuPlayCount_artist',[data_artist_hikaku_from,data_artist_hikaku_to],
	                {
	                  title: '順位変動グラフ＜アーティスト別＞',
	                  stackSeries: true,
	                  seriesColors: ["#d82727", "#FFA500" ],
	                  seriesDefaults: {
	                    renderer: $.jqplot.BarRenderer,
	                    pointLabels: { show: false },
	                    rendererOptions: {
	                      showDataLabels: true,
	                      barDirection: 'horizontal',
	                      varyBarColor: true
	                    }
	                  },
	                  axes: {
	                    yaxis: {
	                      renderer: $.jqplot.CategoryAxisRenderer,
	                      ticks: tick_data_artist_hikaku,
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
	                  highlighter: {bringSeriesToFront:true, tooltipOffset: 10, sizeAdjust: 10, showTooltip: true, tooltipLocation: 's', tooltipAxes: 'x'}
	                }
	              );

	        },
	        complete : function() {
	          //通信終了
	        }
	    });

	    //bind on graph
	    $('#graph_SabunPlayCount_artist').bind('jqplotDataClick',
	            function (ev, seriesIndex, pointIndex, data) {
	                location.href="/artistSearch?artistName=" + tick_data_artist_saboom[pointIndex];
	            }
	        );

	    $('#graph_HikakuPlayCount_artist').bind('jqplotDataClick',
	            function (ev, seriesIndex, pointIndex, data) {
	                location.href="/artistSearch?artistName=" + tick_data_artist_hikaku[pointIndex];
	            }
	        );
	});
});