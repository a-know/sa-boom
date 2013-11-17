$(function(){
	$('#saveSaboomDiaryButton').click(function(){
		//get value
		var saboomDiaryTitle = $('#saboomDiaryTitle').val();
		var saboomDiaryContent = $('#saboomDiaryContent').val();

		//initialize
		$('#saboomDiaryTitleError').empty();
		$('#saboomDiaryContentError').empty();

		//validating
		if('' == saboomDiaryTitle){
			$('#saboomDiaryTitleError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記のタイトルは必須入力です。</p></div>");
			return;
		}else if(saboomDiaryTitle.length > 200){
			$('#saboomDiaryTitleError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記のタイトルは200文字以内で入力して下さい。</p></div>");
			return;
		}else if('' == saboomDiaryContent){
			$('#saboomDiaryContentError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記の本文は必須入力です。</p></div>");
			return;
		}

		//show loading image
		$('#saboomDiaryContentError').html("<img src='../images/progressbar_green.gif'>");

		//prepare ajx
	    var controll_data = {};
	    controll_data.from    = $('#saboomFromSelect option:selected').val();
	    controll_data.to      = $('#saboomToSelect option:selected').val();
	    controll_data.title   = saboomDiaryTitle;
	    controll_data.content = saboomDiaryContent;
	    controll_data.loginID = loginID_in_session;

	    //start ajax
		$.ajax({
	        type : 'POST',
	        url : '/saveDiary',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	$('#saboomDiaryContentError').html("<div class='alert-message success fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記を登録しました。</p></div>");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});

$(function(){
	$('#deleteSaboomDiaryButton').click(function(){
		//ローディング画像に差し替え
		$('#deleteModalLoadingZone3').html("<img src='../images/progressbar_green.gif'>");

	    //ajax通信用のコントロールデータ作成
	    var controll_data = {};
	    controll_data.from    = $('#saboomFromSelect option:selected').val();
	    controll_data.to      = $('#saboomToSelect option:selected').val();
	    controll_data.loginID = loginID_in_session;

	    //start ajax
		$.ajax({
	        type : 'POST',
	        url : '/deleteDiary',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	saboomDiaryTitleObject = $('#saboomDiaryTitle');//日記のタイトル
	        	saboomDiaryContentObject = $('#saboomDiaryContent');//日記の内容

	        	$('#saboomFromSelect option:selected').remove();
	        	$('#saboomToSelect option:selected').remove();

	        	window.alert("削除が完了しました。");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });

		$('#deleteModalLoadingZone3').empty();
	});
});