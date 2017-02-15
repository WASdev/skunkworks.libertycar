<!DOCTYPE html> 
<html>
<head>
<title>Driving Liberty Car</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
		<script type="text/javascript" src="./dojo-release-1.7.4/dojo/dojo.js"
			data-dojo-config="async: true"></script>
<script>
var beta = -1;
var gamma = -1;
var oldGamma = -700;
var steeringOffset = 0;
var speedOffset = 90;
var acceleration = 0;
var steering = 0;
var webSocketOpen = false;

var angleBeforeForward = 25;

var myId = "<%=request.getRemoteAddr()%>";

	require([
		"dojo/_base/array",
		"dojo/dom",
		"dojo/_base/lang",
		"dojo/on",
		"dojo/has",
		"dojo/ready",
		"dojo/domReady!",
		"dojo/_base/json",
		"dojo/_base/xhr"
	], function(array, dom, lang, on, has, ready){});

	var webSocket = new WebSocket('ws://192.168.168.2:9080/LibertyCar/control');
	init();

    function init() {
    	if (window.DeviceOrientationEvent) {
        	// Listen for the deviceorientation event and handle the raw data
        	window.addEventListener('deviceorientation', function(eventData) {
        	if(eventData.alpha==null){
        	      	redirectToLegacy();
        	}
        		// gamma is speed
        		gamma = Math.round(eventData.gamma);
          
          		// beta is steering
         		beta = Math.round(eventData.beta);
          
          		sendData();
          	}, false);
      	} else {
      		redirectToLegacy();
    	}
    }
    
    function redirectToLegacy(){
    	//window.location.href = 'http://'+window.document.location.host + '/LibertyCar/remote/legacy.jsp';
    }
    
    function resetSteering(){
    	steeringOffset=0;
    }
				
	function sendData() {
		if(oldGamma == -700){
			oldGamma = gamma;
		}
		if(gamma>oldGamma+10 || gamma<oldGamma-10){
			if(steeringOffset!=0){
				steeringOffset = 0;
			} else {
				if(beta>0){
					steeringOffset = -180;
				} else {
					steeringOffset = 180;
				}
			}
		}
		
		if(steeringOffset!=0){
			if(beta<0){
				steeringOffset = 180;
			} else {
				steeringOffset = -180;
			}
		}
		
		normalizedBeta = beta+steeringOffset;
	
		if(steeringOffset!=0){
			normalizedBeta = normalizedBeta*-1;
		}
	
		if(gamma>0){
			speedOffset=-90;
		} else {
			speedOffset=90;
		}
	

		oldGamma = gamma;
		
		acceleration = limitTo100((gamma-(angleBeforeForward)+speedOffset)*2);
		steering = limitTo100(normalizedBeta*2);
		
		var accelerationAsInt = parseInt(acceleration);

		document.getElementById("message").innerHTML = 
		"Steering: "+steering+
		" Speed: "+accelerationAsInt;
		
//		steering = steering*-1;

		if(webSocketOpen){
				webSocket.send(dojo.toJson({throttle:accelerationAsInt,turning:steering,id:myId}));
		} 
	}

	function limitTo360(number){
		if(number>360){
			number-=360;
		} else if (number<-360){
			number+=360
		}
		return number;
	}

	function limitTo100(number){
		if(number>100){
			number=100;
		} else if (number<-100){
			number=-100;
		}
		return number;
	}

	webSocket.onerror = function(event) {
			document.getElementById("status").innerHTML = "COMMUNICATION ERROR";
	    alert("An error has occurred. More info:\n"+event.data);    		
	};

	webSocket.onopen = function(event) {
		webSocketOpen = true; 
	};
    	
	webSocket.onclose = function(event) {
		webSocketOpen = false; 
	};

	webSocket.onmessage = function(event) {
		var eventData = event.data;
		//If event data starts with #ipident we need to ping the ip servlet
		if(eventData.indexOf('#ipident') == 0){
			var ipArgs = {
				url: "../session/ipIdentifierEndpoint/"+event.data.substr(8),
				error: function(error){ 
					alert(error);
				}
			}
			dojo.xhrPost(ipArgs);
		} else {
			document.getElementById("status").innerHTML = eventData;
		}
	}; 
</script>
	<div id="wrapper" align="center">
		<h2 style="font-size:20pt; font-family:arial">You are driving the Clear Porsche</h2>
		<div id="status" style="font-size:15pt; font-family:arial">Establishing connection</div>
		<div id="log"></div>
		<p style="font-size:15pt; font-family:arial">
		To drive hold your phone in landscape mode. <br/>Rotate left or right to control steering.
		<br/>Tilt top of phone away to accelerate forwards, and towards you to reverse.
		</p>
		<br/> 
		<div id="message" style="font-size:15pt; font-family:arial"></div>
		<br/>
		<input type="button" onClick="resetSteering();" value="Reset steering" style="font-size:15pt; font-family:arial"/>
	</div>
</body>
</html>
