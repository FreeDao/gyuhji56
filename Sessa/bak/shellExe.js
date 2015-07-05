var shell = {
	prompt : "nashorn> "
}
shell.denoteCmdArray=[
                      {
                	  cmdName:"b",
                	  cmdFunc:"blockExe"
                      },
                      {
                	cmdName:"s",
                	cmdFunc:"stringSetting"  
                      }
                      ];
shell.denoteCmdExecute=function(denoteCmd, paras){
    var cmd = "shell." + denoteCmd.cmdFunc;

    var func = eval(cmd);
    //print("function " + func)
    return func(paras)
};
shell.cmdExecute=function(cmdName, paras){
    // print("search for " +cmdName + " with paras " + paras);
    for(var index = 0; index < shell.denoteCmdArray.length; index++){
	var denoteCmd = shell.denoteCmdArray[index];
	//print(index + " " + denoteCmd)
	if(denoteCmd.cmdName == cmdName){
	    // print("found denoteCmd " + JSON.stringify(denoteCmd))
	    return shell.denoteCmdExecute(denoteCmd, paras);
	}
    }
}

shell.evalInput = function (prompt) {
    var line = "";

    line = java.lang.System.console().readLine();
    if(line.trim().startsWith("//")){
	var denote = line.trim().substring(2).trim().toLowerCase();
	var denoteTokens = denote.replaceAll("\\s{1,}", " ").split(" ");

	var cmdName = denoteTokens[0];
	var paras = denoteTokens.slice(1, denoteTokens.length);

//	print("denoteTokens " + denoteTokens)
//	print("cmdName " +cmdName);
//	print("paras " +paras);

	var ret = shell.cmdExecute(cmdName, paras);

    }else{
	line = line.trim();
	if(line.length){
	    //print("evaling '" + line + "'");
	    var ret = eval(line);
	    //print("evaling '" + line + "' return " + ret);
	    print(ret);
	}
    }
}



shell.blockInput=function(){
    print("/** in blockInput  **/");
    var ret = "";
    var imports = new JavaImporter(java.lang,java.io, java.lang);
    with(imports){
	while(true){
	    var input = System.console().readLine();
	    var end = false;
	    if(input.trim() == "//q"){
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
    return ret.trim();

}
shell.blockExe=function(args){
    var block = shell.blockInput(args);
    block = block.trim();
    if(block.length){
	var ret = eval(block);
	print(ret);
    }
}

shell.stringSetting = function(args){
    if(args.length != 1){
	print("setting string need argument value");
	return;
    }
    eval("args[0]" )
    var __tmp = java.lang.System.console().readLine();
    eval(args[0] + "= __tmp" );
    print(args[0])
}
shell.execute=function(){
    while(true){
	try{
	    java.lang.System.out.print(shell.prompt);
	    shell.evalInput()
	}catch(exp){
	    print(exp)
	}
    }
}

load("config/nashorn/jjsEnv.js");
shell.execute();