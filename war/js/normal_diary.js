$(function(){
	$('#saveNormalDiaryButton').click(function(){
		//get value
		var normalDiaryTitle = $('#diaryTitle').val();
		var normalDiaryContent = $('#diaryContent').val();

		//initialize
		$('#normalDiaryTitleError').html('');
		$('#normalDiaryContentError').html('');

		//validating
		if('' == normalDiaryTitle){
			$('#normalDiaryTitleError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記のタイトルは必須入力です。</p></div>");
			return;
		}else if(normalDiaryTitle.length > 200){
			$('#normalDiaryTitleError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記のタイトルは200文字以内で入力して下さい。</p></div>");
			return;
		}else if('' == normalDiaryContent){
			$('#normalDiaryContentError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記の本文は必須入力です。</p></div>");
			return;
		}

		//show loading image
		$('#normalDiaryContentError').html("<img src='../images/progressbar_green.gif'>");

		//prepare for ajax
	    var controll_data = {};
	    controll_data.from    = $('#normalSelect option:selected').val();
	    controll_data.to      = $('#normalSelect option:selected').val();
	    controll_data.title   = normalDiaryTitle;
	    controll_data.content = normalDiaryContent;
	    controll_data.loginID = loginID_in_session;

	    //start ajax
		$.ajax({
	        type : 'POST',
	        url : '/saveDiary',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	$('#normalDiaryContentError').html("<div class='alert-message success fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>日記を登録しました。</p></div>");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});


$(function(){
	$('#deleteNormalDiaryButton').click(function(){
		//ローディング画像に差し替え
		$('#deleteModalLoadingZone').html("<img src='../images/progressbar_green.gif'>");

	    //ajax通信用のコントロールデータ作成
	    var controll_data = {};
	    controll_data.from    = $('#normalSelect option:selected').val();
	    controll_data.to      = $('#normalSelect option:selected').val();
	    controll_data.loginID = loginID_this_page;

	    //start ajax
		$.ajax({
	        type : 'POST',
	        url : '/deleteDiary',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	if(json.title == ""){
	        		window.alert("まだ日記が登録されていません。");
	        	}else{
	        		window.alert("削除が完了しました。");
	        	}
	        	$('#deleteModalLoadingZone').empty();
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});