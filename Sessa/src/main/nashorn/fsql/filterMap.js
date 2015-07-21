var filterWrapperMap={
	wrapperMap : new ArrayMap(),
	register:function(wrapper, alias){
	    for(var name : alias){
		name = name.toLowerCase.trim()
		if(name.length){
		    wrapperMap.put(name, wrapper)
		}
	    }
	},

	getFilter:function(nameOrg, op, opValue){
	    var name = nameOrg.trim().toLowerCase();
	    //standard name
	    var wrappers =  wrapperMap.get(name)
	    if(!wrappers){
		return null;
	    }else{

		for(var index in wrappers){
		    var retWrapper = wrappers[index];
		    var filter = retWrapper.createFilter(nameOrg, op, opValue);
		    if(filter){
			return filter;
		    }
		}
		return null;		
	    }

	}
}
//create socketWrapper

var NumberFilter = function(op, opValue){
    this.getElementValue = function(stock){
	throw new Excpetion("should override NumberFilter.getElementValue(stock)")
    }
    this.op = op;
    this.opValue = opValue
    this.filter = function(stock){
	var filedValue = this.getElementValue(stock);
	if(isNaN(fieldValue)){
	    return false;
	}
	switch(op){
	case '>':
	    return opValue >  this.opValue
	case '<':
	    return opValue <  this.opValue
	case  '=':
	    return opValue ==  this.opValue
	case '>=':
	    return opValue >=  this.opValue
	case '<=':
	    return opValue <=  this.opValue
	default:
	    throw new Exception("unkown op for NumberFilter " + this.op)
	}
    }
}
//create basic filed filter
(function(){
    
    var getFieldValueFuncDft = function(stock){
	return stock[this.fieldName];
    };
    var initWrapper = function(fields, key){
	wrapper = fields[key];
	wrapper.alias.push(key);
	if(!wrapper.getFieldValueFunc)
	    wrapper.getFieldValueFunc = getFieldValueFuncDft;
	
	filterWrapperMap.register(wrapper, wrapper.alias);
	return wrapper
    }
    
    //XXX create number fields filter
    var numberFields = {
	    "上市年"：{
		alias:["上市年份","上市年度"],
		getFieldValueFunc:function(stock){
		    return stock.上市日期 / 10000;
		}
	    },
	    "上市日期":{
		alias:["上市日期", "上市时间"],
	    },
	    "市盈率(静态)":{
		alias:["静态市盈率", "市盈率静态", "市盈率", "市盈率（静态）"]
	    },
	    "每股收益":{
		alias:["股收益"]
	    }
	    //, may others
    };
    for(var key in numberFields){
	wrapper = initWrapper(numberFileds,key);

	wrapper.createFilter = function(nameOrg, op, opValue){
	    var acceptOp = ['>','<','=','>=','<=', "!="]
	    if(this.acceptOp.indexOf(op) == -1){
		return null;
	    }
	    var num = Number(opValue);
	    if(isNaN(num)){
		return null;
	    }

	    filter = new NumberFilter(op, num);
	    filter.fieldName = key;
	    filter.getElementValue = wrapper.getFieldValueFunc;
	    return filter;
	}

    }
})

