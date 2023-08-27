
$(document).ready(function() {
  $("#login").on("submit", loginAction);
  
  $("#changeUserLogin").on("click", function(event) { 
    event.preventDefault();
   // event.stopPropagation(); 
    
   
    window.location.href = "http://localhost:8080/Pharmacy_Management/";
  });
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
LoginForm={
  login:function(){
   
    var params={};
    params.username=$("#username").val();
    params.password=$("#password").val();

    $.ajax({url:"adminLogin", data: params, type:"post", success: function(result)
  {
	  if(result==="")
	  {
		   $("#loginText").html("Invalid User");
		   $("#loginText").css("color","red");
		   $("#error").html("<h4 style='color:red'>Invalid Credentials</h4>");
		   
	  }
	  else{
		  window.location.href="http://localhost:8080/Pharmacy_Management/admin/dashboard/AdminDashboard.html";
	  }
   
    
  },fail: () => {
      console.log("action calling failed");
    }});

  }

}






function validateForm() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const errorDiv = document.getElementById("error");

  
  errorDiv.textContent = "";

  
  if (!username || !password) {
    errorDiv.textContent = "Please fill in all fields.";
    return false;
  }

  
  /*const minPasswordLength = 6;
  if (password.length < minPasswordLength) {
    errorDiv.textContent = "Password should be at least " + minPasswordLength + " characters long.";
    return false;
  }*/

 
  return true;
}
