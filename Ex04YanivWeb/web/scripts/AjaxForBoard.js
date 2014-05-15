var refreshRate = 1000; //miliseconds
var timeToPlay = 0;

$(document).ready(function() {
         
        triggerAjax();
})


function triggerAjax() {
    setTimeout(ajaxQuery, refreshRate);
}

function ajaxQuery() {
    $.ajax({
        url: "YanivLogger",
      dataType: 'json',
        success: function(data) {
            if (data.versionsEqual == "true")
                {
                   console.log("ok");
                   timeToPlay++;
                   console.log(timeToPlay);
                   if(timeToPlay > 60)
                       {
                          $("form#quitForm").submit();
                       }
                }
                else
                    {
                        if (data.EndGame == "true")
                            {
                                window.location.href = "EndOfGame.jsp";
                            }
                            else
                                {
                                     window.location.href = "YanivWeb.jsp";
                                }
                       
                    }
                
                
            triggerAjax();
            },
        
        error: function(error) {
            console.log(error);
          triggerAjax();    
        }
    });
}
    
