var refreshRate = 1000; //miliseconds


$(document).ready(function() {
         
        triggerAjax();
})


function triggerAjax() {
    setTimeout(ajaxQuery, refreshRate);
}

function ajaxQuery() {
    $.ajax({
        url: "CheckForCompleteInit",
      dataType: 'json',
        success: function(data) {
            
            
            if (data.complete == "true")
                {
                    window.location.href = "YanivWeb.jsp";
                }
                else
                    {
                        $("#count").text(data.left);
                    }
                    
            triggerAjax();
            },
        
        error: function(error) {
            console.log(error);
          triggerAjax();    
        }
     });
}
    
