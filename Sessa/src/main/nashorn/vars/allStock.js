var allStock={
	data:loadVar('allStock'),
	codeMap: new HashMap(),
	codeTrimedMap: new HashMap(),
	nameMap: new HashMap(),
	trimCode:function(code){
	    code = codeFmt(code);
	    if(code.length > 6){
		code = code.substring(0, code.length - 1);
	    }
	    return code;
	},
	stockFrom:function(code){
	    code = code + "";
	    if(code.length != 7){
		print("!!!!!!!!!!!1errorCode " + code);
		std.pause();
	    }
	    return code.substring(6);
	}
	,

	getByFullCode:function(code){
	    code = codeFmt(code);
	    return this.codeMap.get(code);
	},
	getByName:function(name){
	    return this.nameMap.get(name)
	},
	getByTrimedCode:function(code){
	    code = codeFmt(code);
	    code = this.trimCode(code);
	    return this.codeTrimedMap.get(code);
	},
	getByShortCode : function(code){
	   code = codeFmt(code);
	    return this.getByTrimedCode(code);
	},
	getByCode:function(code){
	    code = codeFmt(code);
	    if(code.length == 6){
		return this.getByShortCode(code);
	    }else{
		return this.getByFullCode(code);
	    }

	}
}
var codeFmt = function(x){
    x = x + "";
    if(x.length < 6){
	x = Integer.toOctalString(x)
	x = java.lang.String.format("%06d", Integer.parseInt(x))
	if(!x.startsWith("00")){
	    x = "0" +x
	}
    }
    return x;
}
var YearData=function(file){
    this.year = file.getName().replace(".jvar", "");
    this.file = file;
    this.loader = function(){
	return load(file.getPath());
    }
}
var bindData=function(stock){
    

    stock.scanYearFile = function(){
	stock.years = [];
	stkDir = new File(sessaPath('vars/days/' + stock.getCode()));
	stkFiles = Arrays.asList(stkDir.listFiles());
	Collections.sort(stkFiles, new Comparator(){
	    compare:function(f1, f2){
		return f1.getName().compareTo(f2.getName())
	    }
	})

	stkFiles.forEach(function(file){
	    stock.years.push(file.getName().replace(".jvar", ""))
	})
    }
    stock.getShortCode = function(){
	return stock.code.substring(0,6);
    }
    
    stock.loadYear = function(year){
	file = new File('days/' + this.getCode() +"/" + year);
	yearData = loadVar(file);
	return yearData;
    }
    
    stock.loadDays = function(from, to){
	from= from+"";
	to = to+""
	if(!stock.years || !stock.years.length){
	    stock.scanYearFile()
	}
	fromYear = from.substring(0,4)
	toYear = to.substring(0,4);
	datas = []
	for(var x=fromYear; x <=toYear; x ++){
	    yearData = this.loadYear(x);
	    if(yearData){
		yearData.forEach(function(e){
		    datas.push(e)
		})
	    }
	}

	var ret =[]
	for(var x=0; x < datas.length; x ++){
	    if(datas[x].day>= from && datas[x].day<=to){
		ret.push(datas[x]);
	    }
	}
	return ret;
    }
}

var loadAllStock = function(){
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
	bindData(stock);
	allStock.codeMap.put(stock.getCode(), stock);
	allStock.codeTrimedMap.put(stock.getShortCode(), stock);
	allStock.nameMap.put(stock.name, stock);
    }
}

//if(typeof allStock != "undefined"){
//print("allStock already loaded")
//}else{

loadAllStock();
//}