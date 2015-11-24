var exec = require('cordova/exec');

var ScanService = function(){
};

ScanService.prototype.init = function(arg0, success, error) {
    exec(success, error, "BQscanner", "coolMethod", [arg0]);
};

ScanService.prototype.receiveMessageInAndroidCallback = function(data){
	receiveMessageCallback(data);
}

//添加callback
ScanService.prototype.receiveMessageCallback = function(data){
	try{
		console.log("BQscanner:receiveMessageIniOSCallback--data:"+data);
	}
	catch(exception){
		console.log("BQscanner:receiveMessageIniOSCallback"+exception);
	}
}

ScanService.prototype.registerReceiver = function(arg0,success, error){
	exec(success, error, "BQscanner", "registerReceiver", [arg0]);
}

ScanService.prototype.unRegisterReceiver = function(arg0,success, error){
	exec(success, error, "BQscanner", "unRegisterReceiver", [arg0]);
}

ScanService.prototype.openScanner = function(arg0,success, error){
	exec(success, error, "BQscanner", "openScanner", [arg0]);
}

ScanService.prototype.closeScanner = function(success, error){
	exec(success, error, "BQscanner", "closeScanner", []);
}

ScanService.prototype.startDecode = function(arg0,success, error){
	exec(success, error, "BQscanner", "startDecode", []);
}
ScanService.prototype.stopDecode = function(success, error){
	exec(success, error, "BQscanner", "stopDecode", []);
}


if(!window.plugins){
	window.plugins = {};
}

if(!window.plugins.bqscanner){
	window.plugins.bqscanner = new ScanService();
}  

module.exports = new ScanService(); 