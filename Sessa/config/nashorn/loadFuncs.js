var jjsEnv = "jjsEnv";
var loadx= function(js){
    return load("config/nashorn/" + js + ".js")
}
var loadEnv = function(){
    loadx(jjsEnv);
}