var allStock={
	data:loadVar('allStock'),
	codeMap: new HashMap(),
	codeTrimedMap: new HashMap(),
	nameMap: new HashMap(),
	trimCode:function(code){
	    if(code.length > 6){
		code = code.substring(0, code.length - 1);
	    }
	    return code;
	},
	stockFrom:function(code){

	    if(code.length != 7){
		print("!!!!!!!!!!!1errorCode " + code);
		pause();
	    }
	    return code.substring(6);
	}
	,
	getByCode:function(code){
	    return this.codeMap.get(code);
	},
	getByName:function(name){
	    return this.nameMap.get(name)
	},
	getByTrimedCode:function(code){
	    code = this.trimCode(code);
	    return this.codeTrimedMap.get(code);
	}

}
for(var x in allStock.data){
    var code= allStock.data[x].code;
    allStock.codeMap.put(code, allStock.data[x]);

    code = allStock.trimCode(code);
    allStock.codeTrimedMap.put(code, allStock.data[x]);

    allStock.nameMap.put(allStock.data[x].name, allStock.data[x]);
}

