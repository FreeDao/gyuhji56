var proxyInfo={

		cookieName : "myprx-cookieid",//used by proxy to gen a unify cookie
		cookieKey: "",//used by js, please do not set
		startUrl : "",

		monitor : {
			heartBeatIntervalSeconds : 5,
		}
}

var bootUp = {

}

var bootUp_1 = {
		matcher:{
			//match url, head, content
			mField:["url"],
			matcher : function(url){
				urlStr = String(url);
				if(urlStr.endsWith("jsName")){
					return true;
				}else{
					return false;
				}
			}
		},
		filters:[
		         {
		        	 mField:["content"],
		        	 matcher:function(html){
		        		 html = injectScript(html, ScriptBrowserSider.sourceCode())
		        		 html = injectScript(html, heartBeat.sourceCode());
		        		 autoBoot = ['ScriptBrowserSider()', "heartBeat(10)"];
		        	 },
		        	 
		        	 	
		         	ScriptBrowserSider:function(){
		         		
		         	},
		         	

		         },
		         ]

}


