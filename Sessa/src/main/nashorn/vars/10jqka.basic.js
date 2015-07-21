if(!isDefined('allStock')){
    loadVarExt('allStock')
}
var basic = loadVar('10jqka.basic')
var _numberFunc = function(raw, notErrorPrint){
    var ret = Number(raw)
    if(!notErrorPrint && isNaN(ret)){
	print("error:" + raw)
    }
    return ret;
}
var _trimSuffix = function(raw, suffix, notErrorPrint){
    if(raw != '--'){
	if(raw.endsWith(suffix)){
	    raw = raw.substring(0, raw.length - suffix.length);
	}else{
	    if(raw  && !notErrorPrint){
		print("error fmt: no suffix " + suffix +":" +raw )
	    }
	}
	return _numberFunc(raw, notErrorPrint);
    }
}
var dataCleaner = 
{
	"净利润":function(raw){
	    return _trimSuffix(raw, "亿元",true);
	},
	"净利润-排名":function(raw){
	    return _numberFunc(raw)
	},
	"净资产收益率":function(raw){
	    return _trimSuffix(raw, "%");
	},
	"净资产收益率-排名":function(raw){
	    return _numberFunc(raw)
	},
	"市净率":function(raw){
	    return _numberFunc(raw)
	},
	"市盈率(动态)":function(raw){
	    return _numberFunc(raw, true);
	},
	"市盈率(静态)":function(raw){
	    return _numberFunc(raw, true);
	},
	"总股本":function(raw){
	    return _trimSuffix(raw, "亿股",false);
	},
	
	"占总股本比例":function(raw){
	    return _trimSuffix(raw, "%");
	},
	"最新解禁":function(raw){
	    raw=raw.replace("-", "").replace("-", "")
	    var ret = _numberFunc(raw, false);
	    if(ret){
		return raw;
	    }
	},
	"解禁数量":function(raw){
	    return _trimSuffix(raw, "万股",false);
	},
	"每股净资产":function(raw){
	    return _trimSuffix(raw, "元", false);
	},
	"每股净资产-排名":function(raw){
	    return _numberFunc(raw)
	},
	"每股收益":function(raw){
	    return _trimSuffix(raw, "元", false);
	},
	"每股收益-排名":function(raw){
	    return _numberFunc(raw)
	},
	"每股现金流":function(raw){
	    return _trimSuffix(raw, "元", false);
	},
	"每股现金流-排名":function(raw){
	    return _numberFunc(raw)
	},
	"流通A股":function(raw){
	    return _trimSuffix(raw, "亿股",false);
	},
	"营业收入":function(raw){
	    return _trimSuffix(raw, "亿元",false);
	},
	"营业收入-排名":function(raw){
	    return _numberFunc(raw)
	},
	
	
	
	"分类":function(raw){
	    if(raw.endsWith("盘股")){
		return raw.substring(0,1)
	    }else{
		print("分类（规模）error:" + raw)
	    }
	},
	
}

alias = {
	"占总股本比例":"最新解禁占总股本比例",
	"最新解禁":"最新解禁时间",
	"占总股本比例":"最新解禁占总股本比例",
	"解禁数量":"最新解禁数量",
	"解禁股份类型":"最新解禁股份类型",
	"分类":"规模"
}
notFound=[]
basic.data.forEach(function(info){
    stock = allStock.getByCode(info.code);
    if(!stock){
	//print("stock not found for code " +  code);
	if(-1 == notFound.indexOf(code)){
	    notFound.push(code)
	}
    }else{
	for(var t in info){
	    var alia = alias[t];
	    if(!alia){
		alia = t;
	    }
	    if(t == 'code'){
		continue
	    }
	    var func = dataCleaner[t];
	    if(func){
		var rValue = func(info[t]);
		if(rValue){
		    stock[alia] =rValue
		}
	    }else{
		stock[alia] = info[t]
	    }
	}
    }
})

if(notFound.length){
    print("not found code is " + JSON.stringify(notFound) + "\n total NOT FOUND NUMBER " + notFound.length)
}
//print("not found code is " + JSON.stringify(notFound) + "\n total NOT FOUND NUMBER " + notFound.length)