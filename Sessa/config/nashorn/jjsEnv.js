print("loading jjsEnv...");
load("nashorn:mozilla_compat.js")
var sysCharset = "gbk";

importPackage(
	java.lang, java.io,java.nio,
	java.util,
	java.net,
	java.util.concurrent,
	java.sql,
	java.nio.charset,
	java.text,
	java.util.concurrent.atomic,
	java.util.regex,
	java.util.Map);

importPackage(com.bmtech,
	com.bmtech.htmls,
	com.bmtech.htmls.parser,
	com.bmtech.htmls.parser.filters,
	com.bmtech.htmls.parser.lexer,
	com.bmtech.htmls.parser.nodes,
	com.bmtech.htmls.parser.sax,
	com.bmtech.htmls.parser.scanners,
	com.bmtech.htmls.parser.tags,
	com.bmtech.htmls.parser.util,
	com.bmtech.htmls.parser.util.sort,
	com.bmtech.htmls.parser.visitors,
	com.bmtech.utils,
	com.bmtech.utils.bmfs,
	com.bmtech.utils.bmfs.tool,
	com.bmtech.utils.bmfs.util,
	com.bmtech.utils.c2j,
	com.bmtech.utils.c2j.cTypes,
	com.bmtech.utils.charDet,
	com.bmtech.utils.counter,
	com.bmtech.utils.http,
	com.bmtech.utils.http.fileDump,
	com.bmtech.utils.http.itrCrawl,
	com.bmtech.utils.io,
	com.bmtech.utils.io.diskMerge,
	com.bmtech.utils.log,
	com.bmtech.utils.rds,
	com.bmtech.utils.rds.convert,
	com.bmtech.utils.ruledSegment,
	com.bmtech.utils.ruledSegment.affix,
	com.bmtech.utils.security,
	com.bmtech.utils.segment,
	com.bmtech.utils.superTrimer,
	com.bmtech.utils.systemWatcher,
	com.bmtech.utils.systemWatcher.innerAction,
	com.bmtech.utils.tcp);

var ByteArray = Java.type("byte[]");
var IntArray = Java.type("int[]");



format=function(args){

    if(args && args.length > 1){
	var fmtArg = args.slice(1, args.length);

	var fmted= java.lang.String.format(args[0], fmtArg)
	return fmted;
    }else{
	if(args.length == 1){
	    return args[0];
	}
	return "";
    }
};
var exception = function(e){
    new java.lang.Exception(e).printStackTrace();;
}
jpr = function(){
    var args = Array.prototype.slice.call(arguments); 

    var fmted= format(args);
    print(fmted);

    return "";
};
var error = function(exc){
    jpr("error: %s", exc)
}
var warn = function(exc){
    jpr("warn: %s", exc)
}

var info = function(exc){
    jpr("info: %s", exc)
}
var debug = function(exc){
    jpr("debug: %s", exc)
}


//var bi=function(){
//print("/** in blockInput  **/");
//var ret = "";
//var imports = new JavaImporter(java.lang,java.io, java.lang);
//with(imports){
//while(true){
//var input = System.console().readLine();
//var end = false;
//if(input.trim() == "//q"){
//break;
//}
//if(ret){
//ret = ret + "\n" + input;
//}else{
//ret = input;
//}
//}
//}
//print("/** out blockInput  **/");
//return ret.trim();
//}

var members=function(obj){
    for(i in obj){
	print(i)
    }
}
var varBaseDir = "./config/nashorn/vars/";
var baseVarDir = new File(varBaseDir);
var __baseVarIsCreate = baseVarDir.mkdirs();


var scriptBaseDir = "./config/nashorn/script/";
var baseScriptDir = new File(scriptBaseDir);
var __baseScriptIsCreate = baseScriptDir.mkdirs();
var loadScript = function(fileName){
    var file = new File(baseScriptDir, fileName + ".js");
    if(!file.exists()){
	print("file not exits:" +file);
	return;
    }
    print("loading " + file)
    var txt= FileGet.getStr(file);
    return eval(txt);
}
var saveVar = function(fileName, jsVar){

    var file = new File(baseVarDir, fileName + ".jvar");
    print("save to " + file);
    if(file.exists()){
	if(!Consoler.confirm("already exits, over write?")){
	    return;
	}
    }
    print("saving to "+ file)
    save(file, JSON.stringify(jsVar))
}

var loadVar=function(fileName){
    var file = new File(baseVarDir, fileName + ".jvar");
    if(!file.exists()){
	print("file not exits:" +file);
	return;
    }
    print("loading " + file)
    var txt= FileGet.getStr(file);
    return JSON.parse(txt);
}

var loadVarExt=function(fileName){
    var file = new File(baseVarDir, fileName + ".js");
    if(!file.exists()){
	print("file not exits:" +file);
	return;
    }
    load(file);
}

var save = function(file, txt){
    var f = new File(file.toString());
    f.getParentFile().mkdirs();
    var out = new FileOutputStream(f)
    out.write(txt.getBytes());
    out.close();
}

var readLine = function(){
    var bytes = new ByteArray(4096)
    var len = java.lang.System.in.read(bytes);
    return new java.lang.String(bytes,0, len, sysCharset).trim()
}
var jjsEnv = "jjsEnv";
var loadx= function(js){
    return load("config/nashorn/" + js + ".js")
}
var loadEnv = function(){
    loadx(jjsEnv);
}

var crawl = function(url){
    print("crawling " + url)
    var url = new URL(url);
    var crl = HttpCrawler.makeCrawler(url);
    return crl.getString();
}

var crawlWithMdir = function(urlStr, mdir){
    if(!mdir){
	throw "mdir not set";
    }
    var mfile = mdir.getMFileByName(urlStr);
    var ret;
    if(mfile){
	debug("hit in mdir for url " + urlStr)
	var bytes = mfile.getBytes();
	return new java.lang.String(bytes, "utf-8");
    }else{
	var url = new URL(urlStr);
	var crl = HttpCrawler.makeCrawler(url);
	ret = crl.getString();
	var ips = new ByteArrayInputStream(ret.getBytes("utf-8"));
	mdir.addFile(urlStr, ips, false);
    }
    return ret;
}
var pause = function(){
    Consoler.readString("")
}
var crawlCallBack = function(urls, mdir, callback){

    if(typeof(callback) == "undefined"){
	callback = function(data){
	    print(data)
	    return Consoler.confirm("continue?");

	}
    }
    //print(callback)
    try{
	for(var index in urls){

	    try{
		url = urls[index];
		__crawlTmp = crawlWithMdir(url, mdir);
	    }catch( e){
		exception(e)
	    }finally{
		if(!callback(__crawlTmp)){
		    break;
		}
	    }
	}

    }finally{
	mdir.fsyn();
    }
}
var crawlConfig = function(){
    sleepWhenSuccess=0;
    sleepWhenError=0;
    stopWhenError=false;

    callback :function(data){
	print(data)
	return Consoler.confirm("continue?");
    }
}
var crawlIterator = function(urls, mdir, conf){
    if(typeof(conf) == "undefined"){
	throw "conf not set";
    }
    //print(callback)
    try{
	for(var index in urls){

	    try{
		url = urls[index];
		__crawlTmp = crawlWithMdir(url, mdir);

		if(conf.sleepWhenSuccess){
		    Misc.sleep(conf.sleepWhenSuccess)
		}
	    }catch( e){
		exception(e)
		if(conf.stopWhenError){
		    break;
		}
		if(conf.sleepWhenError){
		    Misc.sleep(conf.sleepWhenError)
		}
	    }finally{
		if(!conf.callback(__crawlTmp)){
		    break;
		}
	    }
	}

    }finally{
	mdir.fsyn();
    }
}

var tail = function(str, num){
    if(!top){
	num = 32;
    }
    var strx = str + "";
    if(num >= strx.length){
	num = strx.length;
    }
    print(strx.substring(str.length - num, num));
}
var head = function(str, top){
    if(!top){
	top = 32;
    }
    var strx = str + "";
    if(top >= strx.length){
	top = strx.length;
    }
    print(strx.substring(0, top));
}


var test = function(){
    return loadx('testEdit');
}

var printJson=function(arg){
    print(JSON.stringify(arg))
}


loadx('history');
loadx('jsoup');
loadx('mdir');
var globalVar = [];
var localVars=function(){
    print("/***local****/");
    for(var e in this){
	if(globalVar.indexOf(e)!=-1){
	    continue;
	}else{
	    var type = (typeof eval("this." +e));
	    print(" " + type  + "\t" +  e);
	}
    }
}
var globalVars = function(){
    print("/* global variables */");
    globalVar.forEach(function(e){
	var type=(typeof eval("this." +e))
	print(" " + type  + "\t" +  e);
    })
}
var vars = function(){
    globalVars();
    localVars();    
}

for(var i in this){
    globalVar.push(i)
}


var day = function(){
    return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
}

var fileNameWithNoSuffix = function(fileName){
    var ret = [2];
    var pos = fileName.lastIndexOf(".");

    if(pos != -1){
	ret[0]= fileName.substring(0, pos);
	ret[1] = fileName.substring(pos +1);
    }else{
	ret = [fileName, ""];
    }
    return ret;
}
var seScript = function(){
    return seJs();
}
var seJs = function(){
     var files = new File('config/nashorn/script').listFiles();
     files = Arrays.asList(files);
     Collections.sort(files, new Comparator(){
	 compare:function(o1,o2){
	     return o1.getName().compareTo(o2.getName())
	 }
     });
     files.forEach(function(f){
	 name = f.getName()
	 if(name.endsWith(".js")){
	     print(" " + name.substring(0, name.length - 3))
	 }else{
	     print("?? " + f)
	 }
     })
}
var seVars = function(){
    var files = new File('config/nashorn/vars').listFiles();
    files = Arrays.asList(files);
    Collections.sort(files, new Comparator(){
	compare:function(o1,o2){
	   s1 = fileNameWithNoSuffix(o1.getName());
	   s2 = fileNameWithNoSuffix(o2.getName())
	   
	   ret =  s1[0].compareTo(s2[0]);
	   if(ret == 0){
	       return s2[1].compareTo(s1[1])
	   }
	   return ret;
	}
    });
   
    for(var fIndex in files){
	var f = files[fIndex];
	
	var name = f.getName();
	if(name.endsWith(".js")){
	    print(" " + name.substring(0, name.length - 3) + " ------> loadVarExt()")
	}else if(name.endsWith(".jvar")){
	   print(" " + name.substring(0, name.length - 5));
	}else{
	    print("?? " + name)
	}
	
    }
}


jpr("jjsEnv.js loaded");



