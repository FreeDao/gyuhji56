var sysCharset = "gbk";
var readLine = function(){
    ByteArray = Java.type("byte[]");
    var bytes = new ByteArray(4096)
    var len = java.lang.System.in.read(bytes);
    return new java.lang.String(bytes,0, len, sysCharset).trim()
}
var jjsInput = { }
jjsInput.defaultCmd = {
	cmdName:"j",
	cmdFunc:"printJson"  
}

jjsInput.denoteCmdArray=[
                          {
                             cmdName:"t",
                             cmdFunc:"test"  
                         },
                         {
                             cmdName:"test",
                             cmdFunc:"test"  
                         },
                         {
                             cmdName:"j",
                             cmdFunc:"printJson"  
                         },
                         {
                             cmdName:"his",
                             cmdFunc:"hisCmd"  
                         },
                         {
                             cmdName:"hisCmd",
                             cmdFunc:"hisCmd"  
                         },
                         {
                             cmdName:"m",
                             cmdFunc:"members"  
                         },
                         {
                             cmdName:"b",
                             cmdFunc:"blockExe"
                         },
                         {
                             cmdName:"/",
                             cmdFunc:"blockExe"
                         },
                         {
                             cmdName:"//",
                             cmdFunc:"blockExe"
                         },
                         {
                             cmdName:"s",
                             cmdFunc:"stringSetting"  
                         }

                         ];

jjsInput.input = function (line) {
    var denote = line.trim().substring(2).trim();
    var denoteTokens = denote.replaceAll("\\s{1,}", " ").split(" ");
    var cmdName = denoteTokens[0];
    var paras = denoteTokens.slice(1, denoteTokens.length);

    return jjsInput.cmdExecute(cmdName, paras);
}
jjsInput.denoteCmdExecute=function(denoteCmd, paras){
    var cmd = "jjsInput." + denoteCmd.cmdFunc;
    var func = eval(cmd);
    return func(paras)
};
jjsInput.cmdExecute=function(cmdName, paras){
    for(var index = 0; index < jjsInput.denoteCmdArray.length; index++){
	var denoteCmd = jjsInput.denoteCmdArray[index];
	if(denoteCmd.cmdName == cmdName){
	    return jjsInput.denoteCmdExecute(denoteCmd, paras);
	}
    }
    paras.unshift(cmdName);
//  print(paras)
    return jjsInput.denoteCmdExecute(jjsInput.defaultCmd, paras);
}

jjsInput.printJson=function(arg){
    return "printJson("+arg.join(" ")+")"
}
jjsInput.test=function(arg){
    return "test()"
}
jjsInput.hisCmd=function(arg){
    return "hisCmd()"
}
jjsInput.members=function(arg){
    return "members("+arg.join(" ")+")"
}
jjsInput.blockInput=function(){
    print("/** in blockInput  **/");
    var ret = "";
    var imports = new JavaImporter(java.lang,java.io, java.lang);
    with(imports){
	while(true){
	    var input = readLine();
//	    print("#'" + input +"' " );
//	    print(", " + input.length);
	    var end = false;

	    if(String(input.trim())==("//q")){
		break;
	    }
	    if(ret){
		ret = ret + "\n" + input;
	    }else{
		ret = input;
	    }
	}
    }
    print("/** out blockInput  **/");
    return ret;
}
jjsInput.blockExe=function(args){
    var block = jjsInput.blockInput(args);
    return block;
}

jjsInput.stringSetting = function(args){
    if(args.length != 1){
	print("setting string need argument value");
	return "";
    }
    var __tmp = readLine();
    return args[0] + " = java.net.URLDecoder.decode(\"" + java.net.URLEncoder.encode(__tmp, "utf-8") + "\", 'utf-8')";
}




