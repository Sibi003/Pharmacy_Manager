// $("h1").css("color","red");
// $("h2").html("<em>hiii</em>");
// //console.log($("img").attr("src"));
// $("a").attr("href","https://www.google.com");
// $(document).keypress(function(){
//     $("h1").slideToggle();
// });

$( document ).ready(function() {
    $("#submitUser").on("click",function(){
        UserRegistration.userSubmit();
    })
});

UserRegistration = {

userSubmit : function(){
    var params = {};
    params.userId = $("#user_id").val();
    params.userEmail = $("#user_email").val();
    params.userName = $("#user_name").val();

    $.ajax({url: "addUser", data: params, type:"POST", success: function(result){
       console.log("Successfully Registered inside of success.");

    }});
    /*.fail(function(jqXHR, textStatus, errorThrown) {
                console.log("Login failed: " + errorThrown);
                // Display appropriate error message to the user or take other actions.
              });
    console.log("Successfully Registered outside of success.");*/

}  

}