var refreshRate = 1000; //miliseconds


$(document).ready(function() {
         
        triggerAjax();
})


function triggerAjax() {
    setTimeout(ajaxQuery, refreshRate);
}

function ajaxQuery() {
    $.ajax({
        url: "CheckForStartedInit",
      dataType: 'json',
        success: function(data) {
            if (data.initStarted == "true")
                {
                   window.location.href = "index.jsp";
                }
                else
                    {
                       console.log("ok");
                    }
                
                
            triggerAjax();
            },
        
        error: function(error) {
            console.log(error);
          triggerAjax();    
        }
    });
}
    
