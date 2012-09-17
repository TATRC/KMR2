$('input[title]').each(function() {
    var qinp = $(this), title = qinp.attr('title');
    if(qinp.val() === '') qinp.val(title);

    qinp.focus(function() {
        if(qinp.val() === title)
            qinp.val('').addClass('focused');
    });

    qinp.blur(function() {
        if(qinp.val() === '')
            qinp.val(title).removeClass('focused');
    });
});