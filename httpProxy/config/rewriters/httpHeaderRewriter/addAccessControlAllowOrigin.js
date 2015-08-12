{
	match : function(url, httpHeaders, html){
		return true;
	},
	
	rewrite : function(url, httpHeaders, html){
		print("rewriting " + url)
		httpHeaders.add("Access-Control-Allow-Origin", "*")
		print("after rewrite" + httpHeaders.respHead)
	}

}