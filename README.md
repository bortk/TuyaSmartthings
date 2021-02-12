# Tuya Smartthings Integration 💡 🔌

The setup alllows to connect Tuya Switches to Smartthings HUB using local integration  ⚙️ 🎚️

This will work with devices supported by tuyapi. These devices use the [Tuya](http://tuya.com) cloud network. These devices are branded under many different names, but if your device works with the TuyaSmart app or port 6668 is open on your device chances are this solution will work.

## Setup Instructions

### Get device key and id for tuya devices

See the [setup instructions](docs/SETUP.md) for how to find the device id and device key parameters.
Or watch [this](https://www.youtube.com/watch?v=oq0JL_wicKg) video and follow only the steps where he gets the device id and key.  

You need to have device key and device id for each tuya device.

### Setup TuyaSmartthings API server 

The API server must run on the same network as the tuya devices. 
You will need the local IP of the server for setup of Smartthing devices.
To run the server, you need to have [Node.js](https://nodejs.org/) framework.

Download the file TuyaSmartthingsAPIServer.js

Run the server
  `node TuyaSmartthingsAPIServer.js`

### Install the device handler code using the SmartThings IDE

1. Within the SmartThings IDE, click on '*My Device Handlers*'.
2. Click the '*+ Create New Device Handler*' button. 
3. Select the '*From Code*' tab and paste in the contents of the device handler groovy file:
* [Tuya Switch](https://github.com/bortk/TuyaSmartthings/blob/master/devicetypes/tuyasmartthings/tuya-switch.src/tuya-switch.groovy)
4. Click '*Create*'.
5. Click '*Publish*' *(For Me)*.


### Install the device handler code using the SmartThings IDE


https://docs.smartthings.com/en/latest/device-type-developers-guide/quick-start.html#create-a-new-device-handler

1. Within the SmartThings IDE, click on '*My Devices*'.
2. Click the '*+ Create New Device*' button. 
3. Select the '*From Code*' tab and paste in the contents of the device handler groovy file:
* [Tuya Switch](https://github.com/bortk/TuyaSmartthings/blob/master/devicetypes/tuyasmartthings/tuya-switch.src/tuya-switch.groovy)
4. Click '*Create*'.
5. Click '*Publish*' *(For Me)*.

## 📝 Notes
- Only one TCP connection can be in use with a device at once. If using this, do not have the app on your phone open.
- Some devices ship with older firmware that may not work with `tuyapi`.  If you're experiencing issues, please try updating the device's firmware in the official app.
- TuyAPI does not support sensors due to the fact that they only connect to the network when their state changes. There are no plans to add support as it's out of scope to intercept network requests.
- The key parameter for devices changes every time a device is removed and re-added to the TuyaSmart app.  If you're getting decrypt errors, try getting the key again - it might have changed.

## 📓 Documentation


## Installation 
To use this plugin, follow the steps below:


## Issues

If you spot any errors with the plugin, or have a question, feel free to open a issue.

## Credits

Tuya API - https://github.com/codetheweb/tuyapi
TuyaSmartthings - https://github.com/blawson327/TuyaSmartthings
