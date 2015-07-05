var allStock={
	data:loadVar('allStock'),
	codeMap: new HashMap(),
	nameMap: new HashMap(),
	trimCode:function(code){
	    if(code.length > 6){
		code = code.substring(0, code.length - 1);
	    }
	    return code;
	},
	getByCode:function(code){
	    code = this.trimCode(code);
	    return this.codeMap.get(code);
	},
	getByName:function(name){
	    return this.nameMap.get(name)
	}
}
for(var x in allStock.data){
    var code= allStock.data[x].code;
    code = allStock.trimCode(code);
    allStock.codeMap.put(code, allStock.data[x]);
    allStock.nameMap.put(allStock.data[x].name, allStock.data[x]);
}

