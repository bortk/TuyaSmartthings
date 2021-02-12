/*
Tuya SmartThings Gateway Server
*/

//##### Options for this program ###################################
var logFile = "no"	//	Set to no to disable error.log file.
var hubPort = 8083	//	Synched with Device Handlers.
//##################################################################

//---- Program set up and global variables -------------------------
var http = require('http')
var net = require('net')
var fs = require('fs')
const TuyaDevice = require('tuyapi')

var server = http.createServer(onRequest)

//---- Start the HTTP Server Listening to SmartThings --------------
server.listen(hubPort)
console.log("TuyAPI Server Console Log")
logResponse("\n\r" + new Date() + "\rTuyAPI Server Error Log")

//---- Command interface to Smart Things ---------------------------
function onRequest(request, response){
	var command = 	request.headers["command"]
//	var deviceIP = 	request.headers["tuyapi-ip"]
	
	// var cmdRcvd = "\n\r" + new Date() + "\r\nIP: " + deviceIP + " sent command " + command
	var cmdRcvd = "\n\r" + new Date() + "\r\nI sent command " + command
	console.log(" ")
	console.log(cmdRcvd)
		
	switch(command) {
		//---- (BridgeDH - Poll for Server APP ------------------
		case "pollServer":
			response.setHeader("cmd-response", "ok")
			response.end()
			var respMsg = "Server Poll response sent to SmartThings"
			console.log(respMsg)
		break

		//---- Device Command ---------------------------
		case "deviceCommand":
			processDeviceCommand(request, response)
			break
	
		default:
			response.setHeader("cmd-response", "InvalidHubCmd")
			response.end("no command")
			var respMsg = "#### Invalid Command ####"
		//	var respMsg = new Date() + "\n\r#### Invalid Command from IP" + deviceIP + " ####\n\r"
			console.log(respMsg)
			logResponse(respMsg)
	}
}

//---- Send deviceCommand and send response to SmartThings ---------
function processDeviceCommand(request, response) {
	
	var deviceIP = request.headers["tuyapi-ip"]
	var deviceID = request.headers["tuyapi-devid"]
	var localKey = request.headers["tuyapi-localkey"]
	var command =  request.headers["tuyapi-command"] 

	var respMsg = "deviceCommand sending to IP: " + deviceIP + " Command: " + command;
	console.log(respMsg);

	var device = new TuyaDevice({
	  //ip: deviceIP,
	  id: deviceID,
	  key: localKey
	  , issueGetOnConnect: false
	});

	device.on('error', error => {
		console.log('Error!', error);
	});

	(async () => {
		await device.find();	  
		await device.connect();	  
		console.log('Connected to device!');
		let status = await device.get();	  
		console.log(`Current status: ${status}.`);	
		switch(command) {
			case "off":	
				console.log('Setting to false!');
				await device.set({set: false});
			break
			case "on":
				console.log('Setting to true!');
				await device.set({set: true});
			break
			case "status":	
				console.log('Received status request!');
			break
		}
		status = await device.get();   		 
		console.log(`New status: ${status}.`);	  
		device.disconnect();

		try{
			console.log("Sending Status to SmartThings:" +status);	
			response.setHeader("tuyapi-onoff", status);
			response.setHeader("cmd-response", status);
			response.end();
			console.log("Status sent to SmartThings:" +status);	
		}catch (err){
			console.log("Error:" +err);	
		}
	})();	
}

//----- Utility - Response Logging Function ------------------------
function logResponse(respMsg) {
	if (logFile == "yes") {
		fs.appendFileSync("error.log", "\r" + respMsg)
	}
	console.log(respMsg)
}