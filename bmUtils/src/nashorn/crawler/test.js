var main=function(){
	urls = loadUrl();
	commitTask(urls, "ext")
}

var ext = function(crawlDatum){
	save(crawlDatum)
	obj = callTmplate(crawlDatum)
	trimObj(obj);
	toExt = getUrls(obj)
	saveTo("taskId")

}

function postToServer(postKey, postValue, toUrl){
	if(!toUrl){
		toUrl = "http://127.0.0.1/ajax/postToSave"
	}
	postData = {postKey:postValue};
	$.post(
			toUrl,
			postData,
			function(data){
			console(data);
		},
	"json");//这里返回的类型有：json,html,xml,text
}