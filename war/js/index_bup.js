$(document).ready(function(){


//    document.getElementById("graph_RandomArtistRanking").innerHTML="";
//    document.getElementById("graph_RandomArtistRanking").innerHTML="";
//    document.getElementById("graph_RandomArtistRanking").style.display = "none";
//
//    var controll_data_graph_random = {};
//    controll_data_graph_random.functionCode = '4';
//    controll_data_graph_random.randomArtist = randomArtist;
//
//    var loginIDlist_random = new Array();
//    var data_playcount_random = new Array();
//    var tick_data_id_random = new Array();
//    var tempArray = new Array();
//
//    var count = 0;
//
//    var title = 'アーティスト「' + randomArtist + '」再生回数ランキング';
//
//    $.ajax({
//        type : 'GET',
//        url : '/graph/GetGraphData',
//        data : controll_data_graph_random,
//        cache : false,
//        dataType : 'json',
//
//        success : function(json) {
//              $.each(json.reverse(), function(i, RandomArtistRankingGraphData){
//                 tempArray = new Array();
//                 tempArray.push(RandomArtistRankingGraphData.playcount);
//                 tempArray.push(i + 1);
//                 data_playcount_random.push(tempArray);
//                 tick_data_id_random.push(RandomArtistRankingGraphData.loginIDwithRank);
//                 loginIDlist_random.push(RandomArtistRankingGraphData.loginID);
//                 count = count + i;
//              });
//
//              if(count == 0){
//            	  document.getElementById("randomArtistRankingError").innerHTML="<br>表示できるランキング情報がありません。<br><hr>";
//              }else{
//                  document.getElementById("graph_RandomArtistRanking").style.display = "";
//              }
//
//              $.jqplot(
//                'graph_RandomArtistRanking',[data_playcount_random],
//                {
//                  title: title,
//                  seriesColors: [ "#f7d4d4", "#efa9a9", "#e77e7e", "#e05252", "#d82727"],
//                  seriesDefaults: {
//                    renderer: $.jqplot.BarRenderer,
//                    pointLabels: { show: true },
//                    rendererOptions: {
//                      showDataLabels: true,
//                      barWidth: 30,
//                      barDirection: 'horizontal',
//                      varyBarColor: true
//                    }
//                  },
//                  axes: {
//                    yaxis: {
//                      renderer: $.jqplot.CategoryAxisRenderer,
//                      ticks: tick_data_id_random,
//                      tickRenderer: $.jqplot.CanvasAxisTickRenderer,
//                      tickOptions: {
//                        angle: -30
//                      }
//                    },
//                    xaxis: {
//                      tickRenderer: $.jqplot.CanvasAxisTickRenderer,
//                      tickOptions: {
//                        angle: -30
//                      }
//                    }
//                  }
//                }
//              )
//        },
//        complete : function() {
//          //通信終了
//        }
//    });

	//総再生回数ランキング（ユーザー）用のグラフ生成
    document.getElementById("graph_TotalPlayCountRanking_User").innerHTML="";
    document.getElementById("graph_TotalPlayCountRanking_User").style.display = "none";

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

              document.getElementById("graph_TotalPlayCountRanking_User").style.display = "";
              if(user_count == 1) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "140px";
              if(user_count == 2) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "200px";
              if(user_count == 3) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "250px";
              if(user_count == 4) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "300px";
              if(user_count == 5) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "350px";
              if(user_count == 6) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "400px";
              if(user_count == 7) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "450px";
              if(user_count == 8) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "500px";
              if(user_count == 9) document.getElementById("graph_TotalPlayCountRanking_User").style.height  = "550px";
              if(user_count == 10) document.getElementById("graph_TotalPlayCountRanking_User").style.height = "600px";

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
    document.getElementById("graph_TotalPlayCountRanking_Artist").innerHTML="";
    document.getElementById("graph_TotalPlayCountRanking_Artist").style.display = "none";

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

              document.getElementById("graph_TotalPlayCountRanking_Artist").style.display = "";

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

//    $('#graph_RandomArtistRanking').bind('jqplotDataClick',
//            function (ev, seriesIndex, pointIndex, data) {
//                location.href="/user/" + loginIDlist_random[pointIndex];
//            }
//        );

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

function rankingButtonClick_first(){

	//総再生回数ランキング・最新月（アーティスト）用のグラフ生成
    document.getElementById("graph_TotalPlayCountRanking_Artist_first").innerHTML="";
    document.getElementById("graph_TotalPlayCountRanking_Artist_first").style.display = "none";
    document.getElementById("rankingButtonClick_first").style.display = "none";

    var controll_data_artist_graph = {};
    controll_data_artist_graph.functionCode = '7';

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

              document.getElementById("graph_TotalPlayCountRanking_Artist_first").style.display = "";

              $.jqplot(
                'graph_TotalPlayCountRanking_Artist_first',[data_playcount_artist],
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

    $('#graph_TotalPlayCountRanking_Artist_first').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
                location.href="/artistSearch?artistName=" + loginIDlist_artist[pointIndex];
            }
        );
}


function rankingButtonClick_second(){

	//総再生回数ランキング・最新月（アーティスト）用のグラフ生成
    document.getElementById("graph_TotalPlayCountRanking_Artist_second").innerHTML="";
    document.getElementById("graph_TotalPlayCountRanking_Artist_second").style.display = "none";
    document.getElementById("rankingButtonClick_second").style.display = "none";

    var controll_data_artist_graph = {};
    controll_data_artist_graph.functionCode = '8';

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

              document.getElementById("graph_TotalPlayCountRanking_Artist_second").style.display = "";

              $.jqplot(
                'graph_TotalPlayCountRanking_Artist_second',[data_playcount_artist],
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

    $('#graph_TotalPlayCountRanking_Artist_second').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
                location.href="/artistSearch?artistName=" + loginIDlist_artist[pointIndex];
            }
        );
}