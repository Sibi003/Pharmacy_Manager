$(document).ready(function(){
	 const profilePic = document.getElementById("profilePic");
  const logoutOptions = document.getElementById("logoutOptions");

	$("#profilePic").on("click",function(){
		$("#logoutOptions").toggle();
	});
	profilePic.addEventListener("click", function(event) {
    event.stopPropagation();
    logoutOptions.classList.toggle("show");
    });
});



  const logoutLink = document.getElementById("logoutLink");
  logoutLink.addEventListener("click", function(event) {
    event.preventDefault(); // Prevent the default link behavior

    $.ajax({
      url: "AdminLogout",
      type: "POST",
      success: function(result) {
        console.log("admin Logged out");
        window.location.href = "http://localhost:8080/Pharmacy_Management/admin/login/AdminLogin.html";
      },
      error: function() {
        console.log("Logout request failed.");
      },
    });
  });
  
   var offset=0;
   var selectedDate=null;
  window.onload=function(){
	
 $(document).ready(function(){
	 $("#date input").on("change",function()
	 {
		 selectedDate=$(this).val();
		 offset=0;
		console.log('selectedDate:',selectedDate);
		getAllBills({date:selectedDate});
	 });
	 $("#bill-search button").on("click",function()
	 {
		 offset=0;
		  var bill_id=$("#bill-search input").val();
		 console.log('bill_id:',bill_id);
		 getAllBills({bill_id:bill_id});
	 });
	 $("#all-bill").on("click",function()
	 {
		 offset=0;
		 $("#bill-data").show();
		 $("#stock-fields").hide();
		 $("#add new user").hide();
		  $("#audit-log-container").hide();
		  $("#add-user-buttons").hide();
		  $("#users-list-container").hide();
		 getAllBills();
	 });
	 $("#add-users").on("click",function()
	 {
		 $("#bill-data").hide();
		 $("#bill-details-container").hide();
		 $("#formContainer").hide();
		 $("#add-user-buttons").show();
		 $("#audit-log-container").hide();
		 $("#users-list-container").hide();
		addUsers(); 
	 });
	 $("#audit-logs").on("click",function()
	 {
		 $("#audit-log-container").show();
		 $("#bill-data").hide();
		 $("#bill-details-container").hide();
		 $("#formContainer").hide();
		 $("#manage-users-buttons").hide();
		 $("#users-list-container").hide();
		 
		 $("#date-log input").on("change",function()
	    {
		  
		  //console.log('selectedDate:',selectedDate);
		  fetchAuditLogs();
		       
	    });
	    $("#id-log-search").on("click",function(){
			
			fetchAuditLogs();
			
		});
		
		$("#user-type-select").on("change",function()
		{
			fetchAuditLogs();
		});
		
		 
		 fetchAuditLogs();
	 });
	 
	 
	 getBills();
 });

	  
  };
  /*-----------------------------------------------fetching  all bills-------------------------------*/
 
   function getAllBills(params) {
	  
  $.ajax({
    url: "GetAllBills",
    type: "GET",
   data:params,
    dataType: "json",
    success: function(data) {
		
     
      if (data.billList != null  && data.billList.length>0) {
		  
		  const totalElements=data.totalBills;
		  
		 $("#bill-search-elements").show();
        $("#today-bill-title").hide();
        var tableBody = $("#data-table");
        console.log("bills fetched problem is with rendering");
        tableBody.empty();
        
        var tableHead = "<tr><th>BILL_ID</th><th>BILL_DATE</th><th>BILL_AMOUNT</th></tr>";
        tableBody.append(tableHead);
        for ( var i =0; i < data.billList.length; i++) {
			
          var bill = data.billList[i];
          var row =
            "<tr onclick=clicked(this)>" +
            "<td>" + bill.bill_id + "</td>" +
            "<td>" + bill.bill_date + "</td>" +
            "<td>" + bill.bill_amount + "</td>" +
            "</tr>";
          tableBody.append(row);
        }
        if(data.billList.length>=2){
		  const prevNext = $("#prevNext");
		  prevNext.html("<span><p align='center'>TotalBills:"+totalElements+"</p><button id='nextBtn'>Next</button></span><p align='center'>"+(offset+1)+"</p>");
		  if(offset>0)
		  {
			  prevNext.html("<span><button id='prevBtn'>prev</button><p align='center'>TotalBills:"+totalElements+"</p><button id='nextBtn'>Next</button><p align='center'>"+(offset+1)+"</p></span>");
		  }
		  $("#nextBtn").off('click').on('click',function(){
			  offset+=1;
			  limit=offset*2;
			  selectedDate=$("#date input").val();
			  getAllBills({offSet:limit,date:selectedDate});
			  
		  });
		  $("#prevBtn").off('click').on('click',function(){
			 offset-=1;
			  limit=offset*2;
			  selectedDate=$("#date input").val();
			  getAllBills({offSet:limit,date:selectedDate});
			 
		  });
		  
		}else {
			  
        	console.log("No data available.");
			$("#nextBtn").remove();
		
      }
		
      }else{
		   $("#bill-search-elements").show();
        $("#today-bill-title").hide();
        var tableBody = $("#data-table");
        tableBody.empty();
        tableBody.html("<h2 align='center'>No bills Found</h2>")
        $("#nextBtn").remove();
	  }
    },
    error: function() {
      console.log("Error fetching data.");
      window.location.href = "http://localhost:8080/Pharmacy_Management/admin/login/AdminLogin.html";
      alert("Session Expired!");
    },
  });
  }
 
    
  
   
   
   /*-----------------------------------fetching particular bills------------------------*/
  
  function getBills() {
	  
	 //var limit=0;
	 // params = params || {};
     // params.offSet = limit;
     // console.log(params);
  $("#bill-search-elements").hide();
  $("#today-bill-title").show();
   $("#users-list-container").hide();
  
	  
  $.ajax({
    url: "GetBills",
    type: "GET",
   // data:params,
    dataType: "json",
    success: function(data) {
		
     
      if (data.billList != null) {
		  
		
        var tableBody = $("#data-table");
        console.log("bills fetched problem is with rendering");
        tableBody.empty();
        var tableHead = "<tr><th>BILL_ID</th><th>BILL_DATE</th><th>BILL_AMOUNT</th></tr>";
        tableBody.append(tableHead);
        for ( var i =0; i <data.billList.length; i++) {
			
          var bill = data.billList[i];
          var row =
            "<tr onclick=clicked(this)>" +
            "<td>" + bill.bill_id + "</td>" +
            "<td>" + bill.bill_date + "</td>" +
            "<td>" + bill.bill_amount + "</td>" +
            "</tr>";
          tableBody.append(row);
        }
        if(data.billList.length===1)
          $("#today-bill-title").html("TodayBills:"+data.billList.length+" Bill");
        else
            $("#today-bill-title").html("TodayBills:"+data.billList.length+" Bills");
        
      } else {
        console.log("No data available.");
        $("#today-bill-title").append(":No Bills Today");
      }
    },
    error: function() {
      console.log("Error fetching data.");
      window.location.href = "http://localhost:8080/Pharmacy_Management/admin/login/AdminLogin.html";
      alert("Session Expired!");
    },
  });
  }
 
  
  /*function getCurrentDateFormatted() {
  const today = new Date();
  const day = String(today.getDate()).padStart(2, '0');
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const year = today.getFullYear();

  return `${day}/${month}/${year}`;
}*/




  
  /* ------------------------------------ bill details------------------------*/
  function clicked(element) {
  var id = $(element).children("td:first").text();
  var clickedRow = element;
  element.classList.add("clicked");
  setTimeout(function() {
    clickedRow.classList.remove("clicked");
  }, 100);
   
  $.ajax({
    url: "AdminBillDetails",
    type: "GET",
    data: { Bill_id: id },
    success: function(data) {
      if (data != null) {
		   var table = $("#details-table");
	if(data.billDetails!=null)
	{
        var details = data.billDetails;
       
       
       var tBody = "<tr><th>TABLET_NAME</th><th>QUANTITY</th><th>TABLET_PRICE</th><th>TOTAL_PRICE</th>";
        $("#bill_id").html(id);
        var totalSum = 0;
        for (var i = 0; i < details.length; i++) {
          var bill_details = details[i];
          var row =
            "<tr>" +
            "<td>" + bill_details.tablet_name + "</td>" +
            "<td>" + bill_details.quantity + "</td>" +
            "<td>" + bill_details.tablet_price + "</td>" +
            "<td>" + bill_details.total_tablet_price + "</td>" +
            "</tr>";
          totalSum += bill_details.total_tablet_price;
          tBody += row;
        }
        tBody +=
          "<tr><td></td><td></td><td><b>Total</b></td>" +
          "<td><b>" + totalSum + "</b></td>" +
          "</tr>";
        table.html(tBody);
        }else{
			var msg=data.errorMsg;
			var tBody="<tr><th><msg/th></tr>";
			table.html(tbody);
		}

        $("#bill-details-container").show();
        $("#bill-data").hide();
        

        $("#cancel").off("click").click(function(event) {
          $("#bill-details-container").hide();
          $("#bill-data").show();
          
        });
        console.log("bill call success");
      }
    },
    error: function() {
      console.log("failure fetching bill details");
    },
  });
}

/*---------------------------------------------add stocks form-------------------------------*/
var tabletsNameCache=null;
$("#add-Stocks").on("click",function(){
	$("#bill-data").hide();
	$("#bill-details-container").hide();
	$("#formContainer").show();
	$("#add-user-buttons").hide();
	 $("#audit-log-container").hide();
	 $("#users-list-container").hide();

	addStocksForm();
})
 function addStocksForm() {
	  //event.preventDefault();
    var dynamicFormContainer = $("#formContainer");
    console.log("addStocks form is called");

    var newFormFields = '<h3 id="stock-addition-msg"></h3>'+
      '<div id="stock-fields" class="stock-fields"><button id="cancel-form">x</button>' +
        '<select  id="incomingTablet" placeholder="TabletName"><option value="">select</option></select>'+
        '<input type="text" id="addQuantity" placeholder="Quantity" required/>' +
        '<input type="text" id="totalPrice" placeholder="totalPrice" required/>' +
        '<input id="addStocksSubmit" type="submit" name="submit" value="submit"/>'+
        "</div>"
    

    dynamicFormContainer.html(newFormFields);
    
     
	if(tabletsNameCache === null ){
		$.ajax({
	      url: "PopulateTabletsAdmin",
	      type: "GET",
	      success: function(data) {
	       tabletsNameCache = data.tabletsName;
	        var select2Field = $("#incomingTablet");
	        tabletsNameCache.forEach(function(option) {
	          var newOption = new Option(option, option, false, false);
	          select2Field.append(newOption);
	        });
	        select2Field.select2();
	      },
	      error: function() {
	        console.log("error fetching tablets data");
	      }
	    });
	  }else
	  {
		   var select2Field = $("#incomingTablet");
		   tabletsNameCache.forEach(function(option) {
	          var newOption = new Option(option, option, false, false);
	          select2Field.append(newOption);
	          });
	          select2Field.select2();
	  }
    
    $("#cancel-form").off("click").on("click",function()
    {
		$("#stock-fields").hide();
	});

    $("#addStocksSubmit").on("click", function() {
      
      var tabletId = $("#incomingTablet").val();
      var Quantity = $("#addQuantity").val();
      var totalPrice=$("#totalPrice").val();
      
      if(totalPrice === "" || tabletId === "" || Quantity === ""){
		  $("#stock-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }

      var stockData = {
        tabletId:tabletId,
        Quantity:Quantity,
        totalPrice:totalPrice,
        
      };

      $.ajax({
        url: "AddIncomingStocks",
        type: "POST",
        data: {"stockData":JSON.stringify(stockData)},
        success: function() {
         
          console.log("Stocks added successfully.");
          $("#stock-fields").remove();
          $("#stock-addition-msg").html("Stock Addition Success ");
        },
        error: function() {
          alert("Failed to add stocks.");
          $("#stock-fields").remove();
        },
         
      });
    });
  }
  
 /*-----------------------addUser form for adding new admins and users--------------*/

 function  addUsers()
 {
	 var addUsersButtons="<div id='manage-users-buttons'><span id='add new user'><button id='add-user'>Add New User</button><button id='add-admin'>Add New Admin</button>"+
	 						"<button id='view-users'>View Users</button><button id='remove-user'>Remove User</button></span></div>"
	 $("#add-user-buttons").html(addUsersButtons);
	 
	 $("#add-user").on("click",function(){
		 $("#formContainer").show();
		 $("#users-list-container").hide();
		 addUserForm();

	 });
	 
	  $("#add-admin").on("click",function(){
		 $("#formContainer").show();
		$("#users-list-container").hide();
		 addAdminForm();
	 });
	 $("#remove-user").on("click",function()
	 {
		 $("#formContainer").show();
		 $("#users-list-container").hide();
		 removeUsers();
	 });
	 $("#view-users").on("click",function(){
		 $("#formContainer").hide();
		$("#users-list-container").show();
		$("#users-list-table").show();
		viewUsers(); 
	 });
	 
	
	 
 }
 function addUserForm()
 {
	 var dynamicFormContainer = $("#formContainer");
    console.log("addUser form is called");

    var newFormFields = '<h3 id="form-title">ADD USER</h3>'+
      '<div id="user-fields" class="stock-fields"><button id="cancel-form">x</button>' +
        '<input type="text" id="user-name" placeholder="name" />' +
        '<input type="email" id="user-email" placeholder="email" >' +
         '<input type="text" id="user-password" placeholder="password" />' +
       //'<input type="text" id="retype-password" placeholder="re-password" />' 
        '<input id="addUserSubmit" type="submit" name="submit" value="submit"/>'+
        "</div>"
    

    dynamicFormContainer.html(newFormFields);
    
    $("#cancel-form").on("click",function()
    {
		$("#formContainer").hide();
	});
    
    $("#addUserSubmit").on("click", function() {
      
      var name = $("#user-name").val();
      var email = $("#user-email").val();
      var password=$("#user-password").val();
     // var re_password=$("#retype-password").val();
      
      if(name === "" || email === "" || password === "" ){
		  $("#user-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }

      var userData = {
        name:name,
        email:email,
       password:password,
        
      };

      $.ajax({
        url: "AddUser",
        type: "POST",
        data: {"userData":JSON.stringify(userData)},
        success: function() {
         
          console.log("Stocks added successfully.");
            $("#form-title").html("USER ADDED SUCCESSFULLY");
          $("#user-fields").remove();
        },
        error: function() {
          alert("Failed to add user.");
          $("#user-fields").remove();
        },
         
      });
    });
 }
 
 function addAdminForm()
 {
	 var dynamicFormContainer = $("#formContainer");
    console.log("addAdmin form is called");

    var newFormFields = '<h3 id="form-title">ADD ADMIN</h3>'+
      '<div id="admin-fields" class="stock-fields"><button id="cancel-form">x</button>' +
        '<input type="text" id="admin-name" placeholder="name" />' +
        '<input type="email" id="admin-email" placeholder="email" >' +
         '<input type="text" id="admin-password" placeholder="password" />' +
       //'<input type="text" id="retype-password" placeholder="re-password" />' 
        '<input id="addAdminSubmit" type="submit" name="submit" value="submit"/>'+
        "</div>"
    

    dynamicFormContainer.html(newFormFields);
    
    $("#cancel-form").on("click",function()
    {
		$("#formContainer").hide();
	});
    
    $("#addAdminSubmit").on("click", function() {
      
      var name = $("#admin-name").val();
      var email = $("#admin-email").val();
      var password=$("#admin-password").val();
     // var re_password=$("#retype-password").val();
      
      if(name === "" || email === "" || password === "" ){
		  $("#admin-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }

      var adminData = {
        name:name,
        email:email,
       password:password,
        
      };

      $.ajax({
        url: "AddAdmin",
        type: "POST",
        data: {"adminData":JSON.stringify(adminData)},
        success: function() {
         
          console.log("Admin added successfully.");
          $("#form-title").html("ADMIN ADDED SUCCESSFULLY");
          $("#admin-fields").remove();
        },
        error: function() {
          alert("Failed to aadd user.");
          $("#admin-fields").remove();
        },
         
      });
    });
 }
 
 function removeUsers()
 {
	  var dynamicFormContainer = $("#formContainer");
    console.log("addAdmin form is called");

    var newFormFields = '<h3 id="form-title">REMOVE USER</h3>'+
      '<div id="remove-user-fields" class="stock-fields"><button id="cancel-form">x</button>' +
        '<input type="text" id="user-id" placeholder="enter the user_s id to be removed"/>'+
        '<input id="removeUserSubmit" type="submit" name="submit" value="submit"/>'+
        '</div>'
    

    dynamicFormContainer.html(newFormFields);
    
    $("#cancel-form").on("click",function()
    {
		$("#formContainer").hide();
	});
    
    $("#removeUserSubmit").on("click", function() {
      
      var userId= $("#user-id").val();
     
      
      if(userId==="" ){
		  $("#remove-user-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }
	  
	  $.ajax({
        url: "RemoveUser",
        type: "POST",
        data: {"userId":userId},
        success: function() {
         
          console.log("user removed successfully.");
          $("#form-title").html("USER REMOVED SUCCESSFULLY");
          $("#remove-user-fields").remove();
        },
        error: function() {
          alert("Failed to add user.");
          $("#remove-user-fields").remove();
        },
         
      });
    });
 }
 
 function viewUsers()
 {
  $.ajax({
    url: "AllUsers",
    type: "GET",
    success: function(data) {
      if (data != null) {
		   var table = $("#users-list-table");
	if(data.users!=null)
	{
        var details = data.users;
        var tBody = "<tr><th>USER_ID</th><th>USER_NAME</th><th>USER_EMAIL</th></tr>";
        
      
        for (var i = 0; i < details.length; i++) {
          var user = details[i];
          var row =
            "<tr>" +
            "<td>" +user.user_id  + "</td>" + 
            "<td>" +user.user_name + "</td>" +
            "<td>" +user.user_email + "</td>" +
            "</tr>";
          
          tBody += row;
        }
        
        table.html(tBody);
        }else{
			console.log("no user found");
		}
          
      }
    },
    error: function() {
      console.log("fetching users failed");
    },
  });
 }
 /*---------------------------------------------Audit Logs-----------------------------------------*/
 function fetchAuditLogs()
 {
	 
	 var logDate=$("#date-log input").val();
	 var userType=$("#user-type-select").val();
	 var userId=$("#id-log input").val();
	 
	 var logFilters={
		 logDate:logDate,
		 userType:userType,
		 userId:userId,
	 }
    $.ajax({
    url: "AuditLogs",
    type: "GET",
    data: {"logFilters":JSON.stringify(logFilters)},
    success: function(data) {
      if (data != null) {
		   var table = $("#audit-log-table");
	if(data.auditLogs!=null)
	{
        var details = data.auditLogs;
        var tBody = "<tr><th>OPERATION_ID</th><th>USER_TYPE</th><th>ID</th><th>DATE AND TIME</th><th>OPERATION_PERFORMED</th>";
        
      
        for (var i = 0; i < details.length; i++) {
          var log = details[i];
          var row =
            "<tr onclick=getLogDetails(this)>" +
            "<td>" +log.operationId  + "</td>" +
            "<td>" +log.userType + "</td>" +
            "<td>" +log.id + "</td>" +
            "<td>" +log.dateAndTime + "</td>" +
            "<td>" +log.operationPerformed + "</td>" +
            "</tr>";
          
          tBody += row;
        }
        
        table.html(tBody);
        }else{
			console.log("audit logs is null");
		}
          
      }
    },
    error: function() {
      console.log("fetching audit logs failed");
    },
  });
 }
 
 function getLogDetails(element) {
  $("#log-details").show();
  var firstTd = element.querySelector('td:first-child');
  var operationId = firstTd.textContent;
  console.log(operationId);

  $.ajax({
    url: "LogDetails",
    type: "GET",
    data: { "operationId": operationId },
    success: function(data) {
      var logData = $("#log-details");
      var logdetail = "<button id='cancel-details'>x</button>";
     

      if (data.details != null) {
        var eachDetail = data.details.details.split(",");

          for(var i=0;i<eachDetail.length;i++)
         {
			 var values=eachDetail[i].split(":");
			  var detail = "<span><h4>" + values[0] + ":</h4>"+"<p>"+values[1]+"</p></span>";
              logdetail += detail;
          }
        

        // Set the HTML content of log-data element outside the loop
        logData.html(logdetail);
        console.log(logData);

        console.log("log details fetched successfully");
         $("#cancel-details").on("click",function()
      {
		  $("#log-details").hide();
	  });
      }
    },
    error: function() {
      console.log("fetching log details failed");
    }
  });
}



