/**
 *  Tuya API Switch
 *
 *  Copyright 2021 Boris Tsirulnik
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 * 
 *  Supported models and functions:  This device supports smart plugs and switches that use the Tuya Smart Life app
 */
metadata {
	definition (name: "Tuya API Switch", namespace: "bortk", author: "Boris Tsirulnik") {

		capability "Switch"
		capability "refresh"
		capability "polling"
		capability "Sensor"
		capability "Actuator"
		command "setCurrentDate"
	}
	tiles(scale: 2) {
		standardTile("switch", "device.switch", width: 6, height: 4, canChangeIcon: true) {
        	state "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#00a0dc",nextState:"turningOff"
			state "off", label:'${name}', action:"switch.on", icon:"st.switch.off", backgroundColor:"#ffffff",nextState:"waiting"
			state "turningOff", label:'waiting', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#15EE10",nextState:"waiting"
			state "waiting", label:'${name}', action:"switch.on", icon:"st.switches.switch.on", backgroundColor:"#15EE10",nextState:"on"
			state "offline", label:'Comms Error', action:"switch.on", icon:"st.switch.off", backgroundColor:"#e86d13",nextState:"waiting"
		}
		standardTile("refresh", "capability.refresh", width: 3, height: 2,  decoration: "flat") {
			state ("default", label:"Refresh", action:"refresh.refresh", icon:"st.secondary.refresh")
		}		 
		main("switch")
		details("switch", "power", "refresh")
	}
}
preferences {	
	input(name: "gatewayIP", type: "text", title: "Gateway Server IP", required: true, displayDuringSetup: true)
    input(name: "deviceIP", type: "text", title: "Tuya Device IP", required: true, displayDuringSetup: true)
	input(name: "deviceID", type: "text", title: "Tuya Device ID", required: true, displayDuringSetup: true)
	input(name: "localKey", type: "text", title: "Tuya Device Key", required: true, displayDuringSetup: true)
}

// parse events into attributes
// def parse(String description) {
// 	log.debug "Parsing '${description}'"
// 	// TODO: handle 'switch' attribute
// }

// handle on/off commands
def on() {
	log.debug "Executing 'on'"
	sendCmdtoServer("on", "deviceCommand", "parseResponse")
    sendEvent(name: "switch", value: "on")
}

def off() {
	log.debug "Executing 'off'"
	sendCmdtoServer("off", "deviceCommand", "parseResponse")
    sendEvent(name: "switch", value: "off")
}

def installed() {
	updated()
}

def updated() {
    refresh()
	runEvery1Minute(refresh)
}

def parseResponse(response){
	log.debug "parseResponse response: ${response}"
    
    def cmd_response = response.headers["cmd-response"]
   	def tuyapi_onoff = response.headers["tuyapi-onoff"]    
    
    log.debug "response.headers[cmd-response]: ${cmd_response}"
    log.debug "response.headers[tuyapi-onoff]: ${tuyapi_onoff}"
    
	if (response.headers["cmd-response"] == "TcpTimeout") {
		log.error "$device.name $device.label: Communications Error"
		sendEvent(name: "switch", value: "offline", descriptionText: "ERROR - OffLine - mod onOffResponse")
	} else { 
		log.debug "${device.name} ${device.label}: Power: ${tuyapi_onoff}"
        if(tuyapi_onoff == "true"){
         	log.debug "sendEvent switch on"
        	sendEvent(name: "switch", value: "on") //,isStateChange: true)
        }
        else {
        	sendEvent(name: "switch", value: "off") //,isStateChange: true)
            log.debug "sendEvent switch off"
        }
	}
}

//	----- REFRESH ------------------------------------------------
def refresh(){
	log.info "refresh"
	//sendEvent(name: "switch", value: "waiting", isStateChange: true)
	sendCmdtoServer("status", "deviceCommand", "parseResponse")
}

//	----- SEND COMMAND DATA TO THE SERVER -------------------------------------
private sendCmdtoServer(command, hubCommand, action){
	log.info "sendCmdtoServer command: ${command} hubCommand: ${hubCommand} action: ${action}"
	def headers = [:] 
	headers.put("HOST", "$gatewayIP:8083")	//	SET TO VALUE IN JAVA SCRIPT PKG.
	headers.put("tuyapi-ip", deviceIP)
	headers.put("tuyapi-devid", deviceID)
	headers.put("tuyapi-localkey", localKey)
	headers.put("tuyapi-command", command)
	headers.put("command", hubCommand)
	sendHubCommand(new physicalgraph.device.HubAction([
		headers: headers],
		device.deviceNetworkId
		 ,[callback: "${action}"]
	))
}