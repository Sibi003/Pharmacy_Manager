document.addEventListener("DOMContentLoaded", function() {
  const profilePic = document.getElementById("profilePic");
  const logoutOptions = document.getElementById("logoutOptions");

  profilePic.addEventListener("click", function(event) {
    event.stopPropagation();
    logoutOptions.classList.toggle("show");
  });

  document.addEventListener("click", function() {
    logoutOptions.classList.remove("show");
  });

  const logoutLink = document.getElementById("logoutLink");
  logoutLink.addEventListener("click", function(event) {
    event.preventDefault(); // Prevent the default link behavior

    $.ajax({
      url: "userLogout",
      type: "POST",
      success: function(result) {
        console.log("user Logged out");
        window.location.href = "http://localhost:8080/Pharmacy_Management/";
      },
      error: function() {
        console.log("Logout request failed.");
      },
    });
  });
});

var tabletsNameCache = null;

function storeTabletsName(row) {
  var tabletsName = tabletsNameCache;
  console.log(tabletsName);
  if (row) {
    var select2Field = $(row).find("#dynamicSelect");
    tabletsName.forEach(function(option) {
      console.log(option);
      var newOption = new Option(option, option, false, false);
      select2Field.append(newOption);
    });
    select2Field.select2();
  }
}

window.onload = function(event) {
  getCurrentDayBills();
  $("#date input").on("change",function()
  {
	  selectedDate=$(this).val();
	  getCurrentDayBills({date:selectedDate});
  });
  $("#bill-search").on("click",function(){
	  var id=$("#bill-search input").val();
	  getCurrentDayBills({id:id});
  });
  
};

function getCurrentDayBills(params) {
  $.ajax({
    url: "CurrentDayBills",
    type: "GET",
    data:params,
    dataType: "json",
    success: function(data) {
      // Check if data is not null or empty
      if (data != null) {
        var tableBody = $("#data-table");
        console.log("bills fetched problem is with rendering");
        tableBody.empty();
        var tableHead = "<tr><th>BILL_ID</th><th>BILL_DATE</th><th>BILL_AMOUNT</th></tr>";
        tableBody.append(tableHead);
        for (var i = 0; i < data.dataList.length; i++) {
          var bill = data.dataList[i];
          var row =
            "<tr onclick=clicked(this)>" +
            "<td>" + bill.bill_id + "</td>" +
            "<td>" + bill.bill_date + "</td>" +
            "<td>" + bill.bill_amount + "</td>" +
            "</tr>";
          tableBody.append(row);
        }
      } else {
        console.log("No data available.");
      }
    },
    error: function() {
      console.log("Error fetching data.");
      window.location.href = "http://localhost:8080/Pharmacy_Management/";
      alert("Session Expired!");
    },
  });
}

function clicked(element) {
  var id = $(element).children("td:first").text();
  var clickedRow = element;
  element.classList.add("clicked");
  setTimeout(function() {
    clickedRow.classList.remove("clicked");
  }, 100);

  $.ajax({
    url: "BillDetails",
    type: "GET",
    data: { Bill_id: id },
    success: function(data) {
      if (data != null) {
        var details = data.billDetails;
        var table = $("#details-table");
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

$(document).ready(function() {
	
    $("#new-bill").click(function() {
      event.preventDefault();
      $("#bill-data").hide();
      $("#bill-details-container").hide();
        $("#display-stock-search").hide();
      $("#new-bill-block").show();
      populateTabletsName();
      $("#bill-submit").off("click").click(function() {
		 /* var field_check=true;
		  var quantities=$("#quantity");
		  var locations=$("#storage-id");
		  var tablets=$("#dynamicSelect");
		for(var i=0;i<$(".dynamicSelect").length;i++)
		{
			console.log(locations[i].textContent);
			if(quantities[i].textContent==="" || (locations[i].textContent==="" || locations[i].textContent==="select") || (tablets[i].textContent==="" || tablets[i].textContent==="select"))
			 { 
				 field_check=false;
				 break;
			 }
		}*/
		
		newBillGeneration();
		  
      });
      var firstBillField=$("#first-bill-field").find("#dynamicSelect");
      firstBillField.on("change",function(){
		  console.log(this);
		  var storageIdElement = $(this);
        populateStorageId(storageIdElement);
	  });
	  
      $("#add-bill-fields").off("click").click(function() {
    var newBillField = $("#new-bill-table");
    var newField = "<tr><td><select class='dynamicSelect' id='dynamicSelect'><option value=''>select</option></select></td>" +
        "<td><input type='text' class='quantity' id='quantity'/></td>" +
        "<td><select class='storage-id' id='storage-id'><option value=''>select</option></select></td>" +
        "<td><button class='remove-bill-fields'>-</button></td>" +
        "</tr>";

    newBillField.append(newField);

    var newRow = newBillField.find("tr:last");

    newRow.find('.dynamicSelect').on("change", function() {
        var storageIdElement = $(this);
        populateStorageId(storageIdElement);
        
    });

    newRow.find('.remove-bill-fields').off("click").click(function() {
        $(this).parent().parent().remove();
    });

    storeTabletsName(newRow);
});

function populateStorageId(element) {
    var tabletName = element.val();
    console.log(element);

    $.ajax({
        url: "StorageIds",
        type: "GET",
        data: { "tabletName": tabletName },
        success: function(data) {
            var locations = data.storageIds;
            var locationField = element.closest("tr").find(".storage-id");
            console.log(locationField);

            locationField.empty();

            locations.forEach(function(option) {
                var newOption = new Option(option, option,false,false);
                locationField.append(newOption);
            });

            locationField.select2();
        },
        error: function() {
            console.log("Error fetching storage ids");
        }
    });
}

      
    });

    $("#home").click(function() {
      $("#bill-data").show();
      $("#new-bill-block").hide();
    });	
	
  $("#stock-details").click(function() {
    $("#bill-data").hide();
    $("#bill-details-container").hide();
    $("#new-bill-block").hide();
    $("#display-stock-search").show();
    //if(tabletsNameCache === null ){
		$.ajax({
	      url: "PopulateTablets",
	      type: "GET",
	      success: function(data) {
	       var tabletsNameCache2 = data.tabletsName;
	        var select2Field = $("#tabletSearch");
	        tabletsNameCache2.forEach(function(option) {
	          var newOption = new Option(option, option, false, false);
	          select2Field.append(newOption);
	        });
	        select2Field.select2();
	      },
	      error: function() {
	        console.log("error fetching tablets data");
	      }
	    });
	

    $("#AddStocks").off("click").on("click",function(){
		
        addStocksForm();
    });
    
    $("#updateStockposition").off("click").on("click",function(){
        updateStockpositionForm();
    });
      
    $("#searchTabletDetails").off("click").click(function(){
		var tabletName=$("#tabletSearch").val();
		getTabletDetails(tabletName);
	});
	
    });
  });
  
 function populateStorageId(element) {
    var tabletName = element.val(); 
    console.log(tabletName);
    
    $.ajax({
        url: "StorageIds",
        type: "GET",
        data: { "tabletName": tabletName },
        success: function(data) {
            var locations = data.storageIds;
            var locationField = $(element); 
            
            locationField.empty(); 
            
            locations.forEach(function(option) {
                var newOption = new Option(option, option);
                locationField.append(newOption);
            });
            
            locationField.select2(); 
        },
        error: function() {
            console.log("Error fetching storage ids");
        }
    });
}

  
 function updateStockpositionForm(){
	 
    var dynamicFormContainer = $("#formContainer");

    var newFormFields = 
      '<div id="stock-fields" class="stock-fields"><span><button id="cancel-form">x</button></span>' +
        '<input type="text" id="fromPosition" placeholder="From Position" />' +
        '<input type="text" id="toPosition" placeholder="To Position" />' +
        '<select  id="updateTablet" placeholder="TabletName"><option value="">select</option></select><br>' +
        '<input type="text" id="updateQuantity" placeholder="Quantity" />' +
        '<input id="updateStockSubmit" type="submit" name="submit" value="submit"/>' +
        '</div>'
        
        dynamicFormContainer.html(newFormFields);
        
        if(tabletsNameCache === null ){
		$.ajax({
	      url: "PopulateTablets",
	      type: "GET",
	      success: function(data) {
	       var tabletsNameCache = data.tabletsName;
	        var select2Field = $("#updateTablet");
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
		   var select2Field = $("#updateTablet");
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
    $("#updateStockSubmit").on("click", function() {
      var fromPosition = $("#fromPosition").val();
      var toPosition = $("#toPosition").val();
      var tabletId = $("#updateTablet").val();
      var remainingQuantity = $("#updateQuantity").val();
      
      if(fromPosition === "" || toPosition ==="" || tabletId === "" || remainingQuantity === ""){
		  $("#stock-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }

      var stockData = {
        fromPosition: fromPosition,
        toPosition:toPosition,
        tablet_Id: tabletId,
        quantity: remainingQuantity,
      };

      $.ajax({
        url: "UpdateStocks",
        type: "POST",
        data: {"stockData":JSON.stringify(stockData)},
        success: function() {
         
          console.log("Stocks added successfully.");
          $("#stock-fields").remove();
        },
        error: function() {
          alert("Failed to update stocks.");
          $("#stock-fields").remove();
        },
         
      });
    });
 } 
  
 function addStocksForm() {
	  event.preventDefault();
    var dynamicFormContainer = $("#formContainer");
    console.log("addStocks form is called");

    var newFormFields = 
      '<div id="stock-fields" class="stock-fields"><span><button id="cancel-form">x</button></span>' +
        '<input type="text" id="locationId" placeholder="Location ID" required/>' +
        '<select  id="addTablet" placeholder="TabletName"><option value="">select</option></select><br>' +
        '<input type="text" id="addQuantity" placeholder="Quantity" required/>' +
        '<input id="addStocksSubmit" type="submit" name="submit" value="submit"/>'+
        "</div>"
    

    dynamicFormContainer.html(newFormFields);
    
    if(tabletsNameCache === null ){
		$.ajax({
	      url: "PopulateTablets",
	      type: "GET",
	      success: function(data) {
	       var tabletsNameCache = data.tabletsName;
	        var select2Field = $("#addTablet");
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
		   var select2Field = $("#addTablet");
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
      var locationId = $("#locationId").val();
      var tabletId = $("#addTablet").val();
      var remainingQuantity = $("#addQuantity").val();
      
      if(locationId === "" || tabletId === "" || remainingQuantity === ""){
		  $("#stock-fields").append("<p style='color:red;'>Fill all the fields.</p>")
		  return;
	  }

      var stockData = {
        location_Id: locationId,
        tablet_Id: tabletId,
        quantity: remainingQuantity,
      };

      $.ajax({
        url: "AddStocks",
        type: "POST",
        data: {"stockData":JSON.stringify(stockData)},
        success: function() {
         
          console.log("Stocks added successfully.");
          $("#stock-fields").remove();
        },
        error: function() {
          alert("Failed to add stocks.");
          $("#stock-fields").remove();
        },
         
      });
    });
  }
  
  function getTabletDetails(tabletName)
  {
	  $.ajax({
		 url:"TabletDetails",
		 type:"GET",
		 data:{"tabletName":tabletName},
		 success:function(data)
		 {
			 if(data!=null)
			 {
				
               var table = $("#tablet-data");
               var tBody = "<tr><th>TABLET_ID</th><th>TABLET_NAME</th><th>LOCATION_ID</th><th>QUANTITY</th>";
        
         
            for(var i=0;i<data.tabletDetails.length;i++)
              {
				   var tablet_details = data.tabletDetails[i];
                   var row =
                   "<tr>" +
                    "<td>" + tablet_details.tablet_id + "</td>" +
                    "<td>" + tablet_details.tablet_name + "</td>" +
                    "<td>" + tablet_details.location_id + "</td>" +
                    "<td>" + tablet_details.quantity+ "</td>" +
                    "</tr>";
         
                   tBody += row;
              }
        
        table.html(tBody);
        
	 }
		 },
		 error:function(){
			 console.log("fetching tablet details failed");
			 alert("fetching tablet data failed");
		 },
	  });
	   $("#bill-data").hide();
    $("#bill-details-container").hide();
    $("#new-bill-block").hide();
    $("#tabletDetails").show();
	  $("#cancel-tablet").click(function() {
          $("#tabletDetails").hide();
        });
          
  }

  function populateTabletsName() {
    $.ajax({
      url: "PopulateTablets",
      type: "GET",
      success: function(data) {
        tabletsNameCache = data.tabletsName;
        storeTabletsName($("#new-bill-table").find("tr:last"));
      },
      error: function() {
        console.log("error fetching tablets data");
      }
    });
  }

  function newBillGeneration() {
    var billCreationTable = $("#new-bill-table");
    var newBillRows = billCreationTable.find('tr');
    var rowdata = [];
    var length = newBillRows.length;
    var key = 1;
    while (key < length) {
      var singleFieldata = {};
      var row = newBillRows[key];
      singleFieldata.tabletName = $(row).find("#dynamicSelect").val();
      singleFieldata.quantity = $(row).find("#quantity").val();
      singleFieldata.locationId = $(row).find("#storage-id").val();
      rowdata.push(singleFieldata);
      key = key + 1;
    }
    $.ajax({
      url: "GenerateNewBill",
      type: "POST",
      data: { "rowdata": JSON.stringify(rowdata) },
      success: function(data) {
        if (data.quantityWarning != null) {
          $("#quantityInfo").html(data.quantityWarning);
        } else {
          window.location.href = "http://localhost:8080/Pharmacy_Management/user/dashboard/UserDashboard.html";
        }
        console.log("success");
      },
      error: function() {
        console.log("error fetching tablets data");
      }
    });
  }
