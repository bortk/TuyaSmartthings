# 💡 🔌 Tuya Smartthings Integration 

The setup alllows to connect Tuya Switches to Smartthings HUB using local integration

This will work with devices supported by tuyapi. These devices use the [Tuya](http://tuya.com) cloud network. These devices are branded under many different names, but if your device works with the TuyaSmart app or port 6668 is open on your device chances are this solution will work.

## ⚙️ Setup Instructions

### Get device key and id for tuya devices

See the [setup instructions](docs/SETUP.md) for how to find the device id and device key parameters.
Or watch [this](https://www.youtube.com/watch?v=oq0JL_wicKg) video and follow only the steps where he gets the device id and key.  

You need to have device key and device id for each tuya device.

### Setup TuyaSmartthings Gateway server 

The API server must run on the same network as the tuya devices. 
You will need the local IP of the server for setup of Smartthings devices.
To run the server, you need to have [Node.js](https://nodejs.org/) framework.

1. Download the contents of git repo
  `git clone https://github.com/bortk/TuyaSmartthings` or using Download ZIP from github
2. Install dependencies
  `npm install`
3. Run the server
  `npm start`

### Install the device handler code using the SmartThings IDE

1. Within the SmartThings IDE, click on '*My Device Handlers*'.
2. Click the '*+ Create New Device Handler*' button. 
3. Select the '*From Code*' tab and paste in the contents of the [Tuya Switch](https://github.com/bortk/TuyaSmartthings/blob/master/devicetypes/tuyasmartthings/tuya-switch.src/tuya-switch.groovy) device handler groovy file.
4. Click '*Create*'.
5. Click '*Publish*' *(For Me)*.

### Create new device code using the SmartThings IDE

This step need to be repeated for each device.

1. Within the SmartThings IDE, click on '*My Devices*'.
2. Click the '*+ New Device*' button. 
3. Enter a “Name” for the device, this can be whatever you want.
4. Enter a “Label” for the device, this is optional and can be whatever you want.
5. Enter a “Device Network Id” This can be anything you want. It cannot duplicate other device ID’s. 
6. Select a “Type” from the dropdown, this should be Tuya API Switch
7. “Version” should be published
8. “Location” should be your hub location, probably “Home”
9. “Hub” should be your hub name.
10. “Group” you won’t be able to select when creating, but these are Groups you’ve created in the Things page in the SmartThings app.
11. Click Create

### Configure new device
This can be done from the SmartThings app or from the IDE

From the IDE <br>
_Go to device -> Preferences -> Edit_

From Smarttthings app <br>
_Go to device -> Settings_

Fill the fields:
* Gateway Server IP 
* Tuya Device ID 
* Tuya Device Key 
* Tuya Device IP - the IP of the Tuya device. Not used for now. Can be left empty.


## Optional Configuration

* Server port <br>
  By default the server runs on port 8083. This can be changed from the server code and from the device handler. SAme value must be used in both places.
* Refresh interval <br>
  Defualt refresh interval is 1 minute. This can be changed from the device handler code. 

## 📝 Notes
- Some devices ship with older firmware that may not work with `tuyapi`.  If you're experiencing issues, please try updating the device's firmware in the official app.
- TuyAPI does not support sensors due to the fact that they only connect to the network when their state changes. There are no plans to add support as it's out of scope to intercept network requests.
- The key parameter for devices changes every time a device is removed and re-added to the TuyaSmart app.  If you're getting decrypt errors, try getting the key again - it might have changed.

## Issues
No garantee for anything to work. Use at your own risk.
If you spot any errors with the plugin, feel free to modify the code.

## Credits
Tuya API - https://github.com/codetheweb/tuyapi <br>
TuyaSmartthings - https://github.com/blawson327/TuyaSmartthings
