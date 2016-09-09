<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport"
			content="width=device-width,maximum-scale=1,minimum-scale=1,user-scalable=no" />
		<title>Remote</title>

		<style type="text/css">
			@media all and (min-width: 800px) {
				html {
					background-color: #666666;
				}
			}

			body {
				background-color: white;
				width: 300px;
				margin: 0 auto;
			}

			#innertest {
				border: 1px solid white;
				width: 100px;
				heigh: 75px;
				background-color: white;
			}

			#gridDriver {
				/* for testing touch.move */
				width: 300px;
				height: 300px;
				xborder: 1px solid yellow;
				background-color: black;
				background-image: url(images/trackpad3.png);
				cursor: auto;
				cursor: url(images/white-circle-md.png) 4 12, auto;
			}

			#gridDriver:hover {
				background-image: url(images/trackpad3.png);
			}

			#wrapper {
				width: 300px;
				margin: 0 auto;
			}
		</style>
		<script type="text/javascript" src="./dojo-release-1.7.4/dojo/dojo.js"
			data-dojo-config="async: true"></script>
		<script>
			var acceleration = 0;
			var steering = 0;
			var webSocketOpen = false;
			var webSocket = new WebSocket('ws://caplonsgprd-3.integration.ibmcloud.com:15159/LibertyCar/control');
			
			var myId = "<%=request.getRemoteAddr()%>";
			
			require([
				"dojo/dom-geometry",
				"dojo/_base/array",
				"dojo/dom",
				"dojo/_base/lang",
				"dojo/touch",
				"dojo/mouse", 
				"dojo/on",
				"dojo/has",
				"dojo/ready",
				"dojo/domReady!",
				"dojo/_base/json",
				"dojo/_base/xhr"
				], function(domGeom, array, dom, lang, touch, mouse, on, has, ready){
					ready(function() {
						setInterval(sendData, 100);
				});

				var mspointer = navigator.msPointerEnabled; // IE10+ PSPointer events?
				var width = 300;
				var height = 300;

				if (window.innerWidth < 320){
					width = 200;
					height = 200;
					dom.byId("gridDriver").style.height = height + "px";
					dom.byId("gridDriver").style.width = width + "px";
					dom.byId("gridDriver").style.backgroundImage="url('images/trackpad2.png')"
					dom.byId("wrapper").style.width = "200px";
					var bodywidth = width + 15;
					document.body.style.width = bodywidth + "px";
				}

				function processTrackpadEvent(e){
					var x = e.pageX - domGeom.position(dom.byId("gridDriver")).x;
					var y = e.pageY - domGeom.position(dom.byId("gridDriver")).y;

					acceleration = Math.round(((y - height / 2) / (height/2)) * 100)* -1;
					steering = Math.round(((x - width / 2) / (width/2)) * 100);

					e.preventDefault();
					e.stopPropagation();
				}
				
				on(dom.byId("gridDriver"), touch.move, function(e){
					processTrackpadEvent(e);
				});
				
				on(dom.byId("gridDriver"), touch.press, function(e){
					processTrackpadEvent(e);
				});
				
				on(dom.byId("gridDriver"), touch.release, function(e){
					console.log("release");
					acceleration = 0;
					steering = 0;
				});
				
				on(dom.byId("gridDriver"), mouse.leave, function(e){
					console.log("release");
					acceleration = 0;
					steering = 0;
				});
			});
		      		
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
     					error: function(error){ //TODO error testing to identify when we stop 404ing
        					alert(error);
      					}
     				}
     				dojo.xhrPost(ipArgs);
    			} else {
    				document.getElementById("status").innerHTML = eventData;
    			}
   			};
			
			function sendData() {
				if(webSocketOpen){
					if(acceleration < -100 || acceleration > 100 || steering < -100 || steering > 100){
						acceleration = 0;
						steering = 0;
						console.log("Error: Acceleration/Steering out of bounds!!");
					}
					
					document.getElementById("log").innerHTML = "Steering: " + steering + " " + "Acceleration: " + acceleration;
				
					if(acceleration>=0){
						document.getElementById("gas").innerHTML = "Forward: " + acceleration + "%";
					} else {
						document.getElementById("gas").innerHTML = "Reverse: " + acceleration*-1 + "%";
					}
				
					document.getElementById("wheel").style.cssText = "display:block;margin:0 auto;-moz-transform: rotate(" + steering/2 + "deg);-webkit-transform: rotate(" + steering/2 + "deg);";

     				webSocket.send(dojo.toJson({throttle:acceleration,turning:steering, id:myId})); 
     			} 
			}
    	</script>
	</head>
	<body>
		<div id="wrapper">
			<h2 align="center">You are driving the Clear Ferrari</h2>
			<div id="status" align="center">Establishing connection</div>
			<div id="gridDriver"></div>
			<div id="log" align="center"></div>
			<br/> 
			<img id="wheel"	style="display: block; margin: auto; width: 100px; height: 100px"
				src="images/wheel.png" /> <br/>
			<div id="gas" align="center"></div>
		</div>
	</body>
</html>
