//this is all the javascript code fot the YanivWeb.jsp file.

$(document).ready(function() {
       
       //this toggle is to be executed when a player click on one of his cards
       //the first function is executed when a card is selected:
       //the card is marked, and the cards details are written as a parameter to the form, to later be sent to the servlet.

                  $(".currentPlayerCards").toggle(function () {
                  var x = $(this).attr("id");
                  $(this).addClass("selected");
                  $("#userChoiceForm").append("<input id=" +x + " TYPE=hidden  NAME=" + x + ">");
                  
                  //when a card is selected, that means that at least one card is selected, so the deck and the pile are now clickables.
                  $(".deck").addClass("deckHover");
                  $(".deck").bind('click', submitByDeck);
                  $(".pile").bind('click',submitByPile);
                  $('.pile').addClass("pileHover");
                  
                  //the second function is executed when a card is deselected.
                  //the card is being unmarked.
                  //the parameter for this cards is deleted from the form.
                 
              }, function (){
                  var x = $(this).attr("id");
                  $(this).removeClass("selected");
                $("#userChoiceForm  #" + x).remove();
                var cardsSelectedCount = $(".selected").length;
                
                 //if there aren't other cards that are selected, the pile and deck are now unclickable.
                if (cardsSelectedCount === 0)
                    {
                          $(".deck").removeClass("deckHover");
                           $('.pile').removeClass("pileHover");
                           $('.deck').unbind('click');
                           $('.pile').unbind('click');
                    }
              });
 });              
 
 function submitByDeck()
 {
     $("#userChoiceForm").append("<input  TYPE=hidden  NAME=cardSource VALUE=DECK>");
     $("form#userChoiceForm").submit();
 }
 
 function submitByPile()
 {
        var x = $(this).attr("id");
        $("#userChoiceForm").append("<input  TYPE=hidden  NAME=cardSource VALUE=" + x + ">");
        $("form#userChoiceForm").submit();
 }

         
