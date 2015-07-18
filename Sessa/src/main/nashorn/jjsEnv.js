print("loading jjsEnv...");
load("nashorn:mozilla_compat.js")
var sysCharset = "gbk";
var sessaPathProperty=java.lang.System.getProperty('sessaPath')
var sessaPath  = function(relPath){
    var base = sessaPathProperty;
    if(relPath){
	return base + relPath
    }else{
	return base;
    }
    
}
load(sessaPath('loadFuncs.js'));

loadx('javaTypes')
loadx('logPrint')
loadx('sessaEnv');

loadx('crawler')
loadx('hisCmd');
loadx('jsoup');
loadx('mdir');

loadx('utils')
loadx('maths');



var test = function(){
    return loadx('~~~~~~Edit');
}

/**********init globals*******************/
var globalVar = [];
for(var i in this){
    globalVar.push(i)
}

//load allStock
if(typeof allStock == undefined){
    loadVarExt('allStock');
}


jpr("jjsEnv.js loaded");



