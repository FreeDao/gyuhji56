var jjsEnv = "jjsEnv";
var loadx= function(js){
    return load(sessaPath(js + ".js"))
}
var loadEnv = function(){
    loadx(jjsEnv);
}