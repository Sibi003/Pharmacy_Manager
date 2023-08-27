
$(document).ready(function()
{
$("#login").on("submit",loginAction);
$("#changeAdminLogin").on("click",function(){
	
	//alert("changing to admin login");
	window.location.href="http://localhost:8080/Pharmacy_Management/admin/login/AdminLogin.html";
})
});



function loginAction(event)
{
event.preventDefault();
  var validation=validateForm();
  if(validation)
  {
    LoginForm.login();
    console.log("login function is called");
  }else{
    return false;
  }
}
LoginForm = {
  login: function () {
    var params = {};
    params.username = $("#username").val();
    params.password = $("#password").val();

    $.ajax({
      url: "userLogin",
      data: params,
      type: "POST",
      success: function (result) {
        console.log("ajax request for userLogin function is successful");

      if(result=="")
       {
       $("#loginText").html("Invalid User");
       $("#loginText").css("color","red");
         $("#error").html("<h4 style='color:red'>Invalid Credentials</h4>");


       }else
       {
        window.location.href="user/dashboard/UserDashboard.html";
       }



      },
      fail: function () {
        console.log("action calling failed");
      },
    });
  },
};











function validateForm() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const errorDiv = document.getElementById("error");


  errorDiv.textContent = "";

  
  if (!username || !password) {
    errorDiv.textContent = "Please fill in all fields.";
    return false;
  }

  // Perform additional checks, such as minimum password length
  /*const minPasswordLength = 6;
  if (password.length < minPasswordLength) {
    errorDiv.textContent = "Password should be at least " + minPasswordLength + " characters long.";
    return false;
  }*/

 
  return true;
}
