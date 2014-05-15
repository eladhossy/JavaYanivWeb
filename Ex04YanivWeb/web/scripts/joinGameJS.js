var bool = new Boolean(0);

$(document).ready(function() {
         
          $("#submitButton").click(function(){
              var name = $("#inputName").attr("value");
              if(name == "")
              {
                  $("#msg").text(" Must enter a name");
              }
              else
                  {
                      $("span").each(function(){                                                                             
                     var name = $("#inputName").attr("value");
                      if ($(this).text() == name)
                      {
                         $("#msg").text(" There is another player named: " + name + ". \nPlease choose another name.");
                         bool = 1;
                      }
                      
                    })
                    if(bool == 0) //no other player has the same name
                          {
                              $("form#joinGameForm").submit();
                          }
                          else
                              {
                                  bool = 0;
                              }
                              
                  }
           
          });
       
})