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
	
	getByFullCode:function(code){
	    return this.codeMap.get(code);
	},
	getByName:function(name){
	    return this.nameMap.get(name)
	},
	getByTrimedCode:function(code){
	    code = this.trimCode(code);
	    return this.codeTrimedMap.get(code);
	},
	getByShortCode : function(code){
	    return this.getByTrimedCode(code);
	},
	getByCode:function(code){
	    if(code.length == 6){
		return this.getByShortCode(code);
	    }else{
		return this.getByFullCode(code);
	    }
	    
	}

}
for(var x in allStock.data){
    var stock = allStock.data[x];
    
    stock.getCode =function(){
	return this.code;
    }
    stock.getShortCode =function(){
	return allStock.trimCode(this.code);
    }
    stock.getFrom =function(){
	var ret = this.code.substring(6);
	if(ret.length != 1){
	    throw "code length error! code = " + this.code;
	}
	return ret;
    }
    

    allStock.codeMap.put(stock.getCode(), stock);

    allStock.codeTrimedMap.put(stock.getShortCode(), stock);

    allStock.nameMap.put(stock.name, stock);
    
   
   
    
}

