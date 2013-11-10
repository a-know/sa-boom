$(function(){
	$('#sendMessageButton').click(function(){
		//initialize
		$('#messageSenderError').empty();
		$('#sendMessageContentError').empty();

		var messageSender = $('#messageSender').val();//メッセージの送信者名
		var sendMessageContent = $('#sendMessageContent').val();//メッセージの内容

		if('' == messageSender){//送信者名が未入力の場合は匿名送信であることがわかるようにする
			messageSender = '（送信者名なし）';
		}

		//validating
		if(messageSender.length > 200){
			$('#messageSenderError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>送信者名は200文字以内で入力して下さい。</p></div>");
			return;
		}else if('' == sendMessageContent){
			$('#sendMessageContentError').html("<div class='alert-message error fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>メッセージの本文は必須入力です。</p></div>");
			return;
		}

		//show loading image
		$('#sendMessageContentError').html("<img src='../images/progressbar_green.gif'>");

		//control data for ajax
	    var controll_data = {};
	    controll_data.to      = loginID_this_page;
	    controll_data.sender  = messageSender;
	    controll_data.content = sendMessageContent;

	    //start ajax
		$.ajax({
	        type : 'POST',
	        url : '/sendMessage',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	$('#sendMessageContentError').html("<div class='alert-message success fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>メッセージを送信しました。</p></div>");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});

$(function(){
	$('#messageSelect').change(function(){
		//initialize
		$('#messageSenderNameArea').empty();

	    //メッセージのキーを取得
	    var objectValue = $('#messageSelect option:selected').val();

	    if(objectValue == ""){
	    	return;
	    }

	    //タイトルを取得
	    var title = $('#messageSelect option:selected').text();

	    //ajax通信用のコントロール情報作成
	    var controll_data = {};
	    controll_data.key = objectValue;

	    //start ajax
	    $.ajax({
	        type : 'POST',
	        url : '/loadMessage',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	$('#messageIconHref').attr('title',title);
	        	$('#messageIconHref').attr('data-content',json.content);
	        	$('#messageSenderNameArea').html(json.sender + "さん");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});


$(function(){
	$('#deleteMessageButton').click(function(){
		//ローディング画像に差し替え
		$('#deleteModalLoadingZone4').html("<img src='../images/progressbar_green.gif'>");

	    //メッセージのキーを取得
	    var objectValue = $('#messageSelect option:selected').val();

	    if(objectValue == ""){
	    	window.alert("削除したいメッセージを選択して下さい。");
	    	return;
	    }

	    //ajax通信用のコントロール情報作成
	    var controll_data = {};
	    controll_data.key = objectValue;

	    //start ajax
	    $.ajax({
	        type : 'POST',
	        url : '/deleteMessage',
	        data : controll_data,
	        cache : false,
	        dataType : 'json',

	        success : function(json) {
	        	$('#messageSelect option:selected').remove();
	        	$('#messageSenderNameArea').empty();
	        	window.alert("削除が完了しました。");
	        },
	        complete : function() {
	          //通信終了
	        }
	    });
	});
});