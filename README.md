 ### 注册callback
  window.plugins.bqscanner.receiveMessageInAndroidCallback = function(data){
    alert("扫描结果:"+data);
  };

 ### 注册接收码枪 0广播 1键盘
  window.plugins.bqscanner.registerReceiver("0",
    function(succ){
      alert(succ);
  },
    function(error){
      alert(error);
  });

 ### 停止接收码枪
  /**
  window.plugins.bqscanner.unRegisterReceiver("0",
    function(succ){
      alert(succ);
  },
    function(error){
      alert("error:"+error);
    });
*/