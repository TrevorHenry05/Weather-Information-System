
 
 document.getElementById("submitbutton").addEventListener("click", function() {
	 var xhr;
	
	try{
		// Opera 8.0+, Firefox, Safari
		xhr = new XMLHttpRequest();
	} catch (e){
		// Internet Explorer Browsers
		try{
			xhr = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try{
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e){
				// Something went wrong
				alert("Your browser broke!");
				return false;
			}
		}
	}
	
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if(xhr.status == 200) {
				 removeChildElements("title");
				 removeChildElements("container")
				 var weather = JSON.parse(xhr.responseText);
				 var location = weather.resolvedAddress;
				 var descrip = weather.description;
				 
				 var titleDiv = document.createElement('div');
				 titleDiv.className = "titlediv"
				 titleDiv.innerHTML = "<span class='under'>Location:</span> " + location + "<br><span class='under'>Description:</span> " + descrip;
				 document.getElementById('title').appendChild(titleDiv);
				    
				 for(var i = 0; i < weather.days.length; i++) {
					var card = document.createElement("div");
					card.className = "weathercard"
				    var date = weather.days[i].datetimeEpoch;
				    var daydescr = weather.days[i].description;
				    var temp = weather.days[i].temp;
				    var tempmax = weather.days[i].tempmax;
				    var tempmin = weather.days[i].tempmin;
				    var uv = weather.days[i].uvindex;
				    var humidity = weather.days[i].humidity;
				    var sunrise = weather.days[i].sunriseEpoch;
				    var sunset = weather.days[i].sunsetEpoch;
				    card.innerHTML = "<span class='undercard'>Date:</span> " + date + "<br><span class='undercard'>Description:</span> " + daydescr + "<br><span class='undercard'>Temp:</span> " + temp +
				    				 "<br><span class='undercard'>Max Temp:</span> " + tempmax + " <span class='undercard'>Min Temp:</span> " + tempmin + 
				    				 "<br><span class='undercard'>Humidity:</span> " + humidity + "<br><span class='undercard'>UV Index:</span> " + uv +
				    				 "<br><span class='undercard'>Sunrise:</span> " + covertTime(sunrise) + "<br><span class='undercard'>Sunset:</span> " + covertTime(sunset);
				    document.getElementById("container").appendChild(card);
		    	}			 
		    } else {
				alert("Invalid location or cant connect to resources");
			}
		}
	}
  	 
	 xhr.open("POST", "http://localhost:8080/app/WeatherServlet");

	 var formData = new FormData(document.getElementById("form")); 
	 console.log("sent");
	 xhr.send(formData);
 });
 
 function removeChildElements(element) {
 	const myNode = document.getElementById(element);
  	while (myNode.lastElementChild) {
    	myNode.removeChild(myNode.lastElementChild);
  	}	
}

function covertTime(time) {
	time = time.split(':'); // convert to array

	// fetch
	var hours = Number(time[0]);
	var minutes = Number(time[1]);
	var seconds = Number(time[2]);
	
	// calculate
	var timeValue;
	
	if (hours > 0 && hours <= 12) {
	  timeValue= "" + hours;
	} else if (hours > 12) {
	  timeValue= "" + (hours - 12);
	} else if (hours == 0) {
	  timeValue= "12";
	}
	 
	timeValue += (minutes < 10) ? ":0" + minutes : ":" + minutes;  // get minutes
	timeValue += (seconds < 10) ? ":0" + seconds : ":" + seconds;  // get seconds
	timeValue += (hours >= 12) ? " P.M." : " A.M.";  // get AM/PM
	
	return timeValue;
}
 
 
 
 
 

