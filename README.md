


### 注册callback，得到条码后传递给data

	window.plugins.bqscanner.receiveMessageInAndroidCallback = function(data){
 		alert("扫描结果:"+data);
	};

### 注册接收码枪 参数暂时传递0

	window.plugins.bqscanner.registerReceiver("0",
  		function(succ){
   			alert(succ);
  		},
    	function(error){
      	alert(error);
  	});

### 停止接收码枪广播
	window.plugins.bqscanner.unRegisterReceiver("0",
    function(succ){
      alert(succ);
  	},
    function(error){
      	alert("error:"+error);
    });
    
### 开启码枪 0 广播，1键盘
	window.plugins.bqscanner.openScanner("0",
    function(succ){
      alert(succ);
  	},
    function(error){
      	alert("error:"+error);
    });
       
### 关闭码枪
	window.plugins.bqscanner.closeScanner(
    function(succ){
      alert(succ);
  	},
    function(error){
      	alert("error:"+error);
    });
