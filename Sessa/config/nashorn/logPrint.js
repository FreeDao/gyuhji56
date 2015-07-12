var readLine = function(){
    var bytes = new ByteArray(4096)
    var len = java.lang.System.in.read(bytes);
    return new java.lang.String(bytes,0, len, sysCharset).trim()
}
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
