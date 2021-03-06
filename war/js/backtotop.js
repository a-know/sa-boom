$(document).ready(function(){

    // hide #back-top first
    $("#back-top").hide();

    // fade in #back-top
    $(function () {
        $(window).scroll(function () {
            if ($(this).scrollTop() > 120) {
                $('#back-top').fadeIn();
            } else if ($(this).scrollTop() < 80) {
                $('#back-top').fadeOut();
            }
        });

        // scroll body to 0px on click
        $('#back-top a').click(function () {
            $('body,html').animate({scrollTop: 0}, 'slow');
            return false;
        });
    });
});