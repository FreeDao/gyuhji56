
//create socketWrapper

//create basic filed filter
(function(){
    loadx("fsql/basicFilters")
    loadx("fsql/filterFactory")
    var getFieldValueFuncDft = function(stock){
	return stock[this.fieldName];
    };
    var initFactory = function(factory){

	if(!factory.alias){
	    factory.alias = []
	}
	factory.alias.push(factory.dftKey);
	if(!factory.getFieldValueFunc)
	    factory.getFieldValueFunc = getFieldValueFuncDft;
	filterFactoryMap.registerFactory(factory, factory.alias);
    }

    //XXX create number fields filter
    var numberFields = {
	    "上市年":{
		alias:["上市年份","上市年度"],
		getFieldValueFunc:function(stock){
		    return stock.上市日期 / 10000;
		}
	    },
	    "净利润":{alias:[]},
	    "净利润-排名":{alias:["净利润排名"]},
	    "净资产收益率":{alias:["净收益率"]},
	    "净资产收益率-排名":{alias:["净资产收益率排名", "净收益率排名"]},
	    "市净率":{alias:[]},
	    "市盈率(动态)":{alias:["动态市盈率"]},
	    "市盈率(静态)":{alias:["静态市盈率"]},
	    "总股本":{alias:[]},
	    "最新解禁占总股本比例":{alias:["最新解禁比例"]},
	    "最新解禁数量":{alias:[]},
	    "最新解禁时间":{alias:[]},
	    "最新解禁股份类型":{alias:["最新解禁类型"]},
	    "每股净资产":{alias:[]},
	    "每股净资产-排名":{alias:["每股净资产排名"]},
	    "每股收益":{alias:[]},
	    "每股收益-排名":{alias:["每股收益排名"]},
	    "每股现金流":{alias:[]},
	    "每股现金流-排名":{alias:["每股现金流排名"]},
	    "流通A股":{alias:[]},
	    "营业收入":{alias:[]},
	    "营业收入-排名":{alias:["营业收入排名"]},
	    //, may others
    };
    for(var key in numberFields){
	var factory = numberFields[key];
	factory.dftKey = key;
	initFactory(factory);

	factory.createFilter = function(nameOrg, op, opValue){
	    var acceptOp = ['>','<','=','==','>=','<=', "!="]
	    if(acceptOp.indexOf(op) == -1){
		return null;
	    }
	    var num = Number(opValue);
	    if(isNaN(num)){
		return null;
	    }

	    var filter = new NumberFilter(op, num);
	    filter.fieldName = this.dftKey;
	    filter.getElementValue = this.getFieldValueFunc;
//	    printJsonf(filter)
	    return filter;
	}
    }
    print("init number filters ok")
    //enum fields
    var enumFields = {
	"规模":{alias:[]}
    };
    for(var key in enumFields){
	var factory = enumFields[key];
	factory.dftKey = key;
	initFactory(factory);

	factory.createFilter = function(nameOrg, op, opValue){
	    var acceptOp = ['=','==','in','!=']
	    if(acceptOp.indexOf(op) == -1){
		return null;
	    }

	    var filter = new EnumFilter(op, opValue);
	    filter.fieldName = this.dftKey;
	    filter.getElementValue = this.getFieldValueFunc;
	    return filter;
	}
    }
    print("init enum filters ok")
}())

