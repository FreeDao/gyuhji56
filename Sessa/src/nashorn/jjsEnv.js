print("loading jjsEnv...");
load("nashorn:mozilla_compat.js")
var sysCharset = "gbk";
load('config/nashorn/loadFuncs.js');

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
loadVarExt('allStock');


jpr("jjsEnv.js loaded");



