var ArrayList = Java.type("java.util.ArrayList");
var x = load('jsTest/toload.js')
print(x)
testMap=function(httpMap){
	print("httpMap in js:" +httpMap);
	print(httpMap.Expires)
	var list = new ArrayList();
	list.add('added');
	httpMap.put("add", list);
	httpMap.remove("BDPAGETYPE");
	return httpMap;
};
