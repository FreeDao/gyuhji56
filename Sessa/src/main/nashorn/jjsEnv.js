print("loading jjsEnv...");
load("nashorn:mozilla_compat.js")
//简便的'jjsEnv'字符串设置
var jjsEnv = "jjsEnv";
var sysCharset = "gbk";
var sessaPathProperty=java.lang.System.getProperty('sessaPath')
/**
 * 获得相对于sessa工作目录的相对目录。sessa工作目录通过命令行-D参数传入。
 * @param relPath {String}相对目录名，获得$sessaHome/relPath
 * @return {string} 相对于sessaHome的路径
 */
var sessaPath  = function(relPath){
    var base = sessaPathProperty;
    if(relPath){
	return base + relPath
    }else{
	return base;
    }
}
var stockDayDir = new java.io.File(new java.io.File(sessaPath(null)), "../sessaStockDays/days/");
/**
 * 以sessa目录为相对目录的js加载函数
 * @param js <string>  文件名（无.js后缀，后缀自动添加）
 */
var loadx= function(js){
    return load(sessaPath(js + ".js"))
}
/**
 * @see  loadx(jjsEnv)
 */
var loadEnv = function(){
    loadx(jjsEnv);
}

loadx('javaTypes')
loadx('logPrint')
loadx('sessaEnv');

loadx('crawler')
loadx('hisCmd');
loadx('jsoup');
loadx('mdir');

loadx('utils')
loadx('console')
loadx('maths');
loadx('seStream');
loadx("stock.funcs/Stock");



var test = function(){
    return loadx('~~~~~~Edit');
}

/**********init globals*******************/
var globalVar = [];
var globalThis = this;
for(var i in globalThis){
    globalVar.push(i)
}

//load allStock
if(typeof allStock == 'undefined'){
    loadVarExt('allStock');
}


jpr("jjsEnv.js loaded");



