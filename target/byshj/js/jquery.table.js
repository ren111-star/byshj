
$.fn.tbhighligt = function(params){
    var options = {
        hoverColor   : '#dbe6ec'
    };
    $.extend(options, params);
    var arr = [];
   $(this).find('tr').each(function(){
           var td =$(this).find('td');
            $(this).hover(function(){
               arr = [];
               td.each(function(i){
                 arr[i] = $(this).css('backgroundColor');
                 $(this).css('backgroundColor', options.hoverColor );
               });
            },function(){
     td.each(function(i){
                 $(this).css('backgroundColor', arr[i] );
                });
            });
    });
};