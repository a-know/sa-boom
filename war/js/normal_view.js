$(function(){
	$('#deleteInfoButton').click(function(){
		//ローディング画像に差し替え
		$('#deleteModalLoadingZone2').html("<img src='../images/progressbar_green.gif'>");

	    //ajax通信用のコントロールデータ作成
	    var controll_data = {};
	    controll_data.date    = $('#normalSelect option:selected').val();
	    controll_data.loginID = loginID_this_page;

		$.ajax({
	        type : 'POST',
	        url : '/deleteInfo',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	window.alert("削除が完了しました。");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });

		$('#deleteModalLoadingZone2').empty();
		location.href="./" + loginID_this_page;
	});
});