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
    load(file)
//    print("loading " + file)
//    var txt= FileGet.getStr(file);
//    return eval(txt);
}
var saveVar = function(fileName, jsVar, forcedSave){

    var file = new File(baseVarDir, fileName + ".jvar");
    print("save to " + file);
    if(file.exists()){
	if(typeof forcedSave != "undefined" && forcedSave){
	    print("override " + file);
	}else{
	    if(!Consoler.confirm("already exits, over write?")){
		return;
	    }
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
