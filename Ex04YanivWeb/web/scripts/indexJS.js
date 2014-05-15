////this is all the javascript code fot the index.htm file (the input for all the game parameters).

$(document).ready(function() {
    
    
         
         $("#submitManual").click(function(){
             var name = $("#name").attr("value");
             if (name == "")
                 {
                     $("#msg").text(" Must enter a name");
                 }
                 else
                     {
                         $("form#manualInitForm").submit();
                     }
             
             
             
         })
         
         printPlayerInputPanels(2); //the default is the minimum = 2 players.
         
         //when the user click on a radio button, input panels for the players are being generated, according to the number of players the radio button  indicates.
         $("#2").click(function () {
              printPlayerInputPanels(2);
          })
          
          $("#3").click(function () {
              printPlayerInputPanels(3);
          })
          
          $("#4").click(function () {
              printPlayerInputPanels(4);
          })
          
          //when the user 'leaves'  the input field for the game end valud, a validity check is executed to see wether the value is a legal integer.
          $("#endGameValue").blur(function (){
             var x = $(this).attr("value");
             var intRegex = /^\d+$/;
            if(!(intRegex.test(x))) {
                //$(this).parent().append("<span id=warning>NOT a valid int!<span style='font-size: 15px;'>returning to default</span>");
                alert("not a valid int. returning to default (50)");
                $(this).attr("value","50");
            }
          })
})
     
     //this function prints the panels (number of panels is according to the radio button selected),  where the details for the players are entered.
     function printPlayerInputPanels(numOfPlayers)
     {
          $("#playersDetailsPanel").html("");

$("#playersDetailsPanel").append("Choose the number of human players (including you):<br />");
        
        for (i = 1 ; i <= numOfPlayers ; i++)
            {
                $("#playersDetailsPanel").append("<input id=" + i + " type='radio' name='numberOfHumanPlayersRadio' value=" + i + " checked='checked'/>" + i + "<br />");
            }
       
        }
             
 
     
       
             
              