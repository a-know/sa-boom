$(function(){
	$('#saveSettingsButton').click(function(){
		//initialize
		$('#saveSettingsMessage').empty();
		//通信用コントロール情報の編集
		var controll_data = {};
		controll_data.loginID = loginID_in_session;
		controll_data.url = $('#inputUrl').val();
		controll_data.introduction = $('#inputIntroduction').val();
		controll_data.pm = $('#cb-private').attr('checked');
		controll_data.tw_option1 = $('#tweet-option1').attr('checked');
		controll_data.tw_option2 = $('#tweet-option2').attr('checked');
		controll_data.tw_option3 = $('#tweet-option3').attr('checked');
		controll_data.tw_option4 = $('#tweet-option4').attr('checked');
		controll_data.tw_option5 = $('#tweet-option5').attr('checked');

		//start ajax
		$.ajax({
			type : 'POST',
			url : '/saveSettings',
			data : controll_data,
			cache : false,
			dataType : 'json',

			success : function(json) {
				//通信成功
				$('#saveSettingsMessage').html("<div class='alert-message success fade in' data-alert='alert' ><a class='close' href='#'>&times;</a><p>設定内容を更新しました。</p></div>");
			},
			complete : function() {
				//通信終了
			}
		});
	});
});