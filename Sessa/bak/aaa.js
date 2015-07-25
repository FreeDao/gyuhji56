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


//  this.scanYearFile = function(){

//  var stkDir = new File(sessaPath('vars/days/' + this.getCode()));
//  var stkFiles = Arrays.asList(stkDir.listFiles());
//  Collections.sort(stkFiles, new Comparator(){
//  compare:function(f1, f2){
//  return f1.getName().compareTo(f2.getName())
//  }
//  })

//  stkFiles.forEach(function(file){
//  this.years.push(file.getName().replace(".jvar", ""))
//  })
//  }