$(function(){
	$('#loginForm').submit(function(){
		if($('#pass').val() != ''){
			$('#pass').val(sha256.hex($('#pass').val()));
		}
	});
});