var refreshRate = 1000; //miliseconds


$(document).ready(function() {
         
        triggerAjax();
})


function triggerAjax() {
    setTimeout(ajaxQuery, refreshRate);
}

function ajaxQuery() {
    $.ajax({
        url: "CheckRestart",
      
        success: function(data) {
            
            
            if (data == "restart")
                {
                    window.location.href = "YanivWeb.jsp";
                }
                else if (data == "init")
                    {
                         window.location.href = "index.jsp";
                    }
                    
            triggerAjax();
            },
        
        error: function(error) {
            console.log(error);
          triggerAjax();    
        }
    });
}
    
