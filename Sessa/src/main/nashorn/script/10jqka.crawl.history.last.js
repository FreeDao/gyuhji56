updateLastDay=function(){
    var kkk = loadVarExt('allStock')
    loadx('stock.day')

    stockYear = []

    var urls = [];
    for(var index in allStock.data){
	var stock = allStock.data[index]
	stockFrom = allStock.stockFrom(stock.code);
	if(stockFrom == '1'){
	    prf = "sh_"
	}else{
	    prf = "sz_"
	}
	var url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/last.js'
	urls.push({"url":url, "code":stock.code});
    }
    conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('10jka.history.last')
    
    conf.callback = function(urlInfo, data){
	var pos = data.indexOf("(")
	data = Misc.substring(data , "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
	data = JSON.parse(data);
	if(!data){
	    print("no data!" + data);
	    return true;
	}
	print(data.name + " start at " + data.start);
	stockYear.push({
	    code:urlInfo.code,
	    year:data.start
	})

//	members(data)
	var days = data.data.split(";");
	dayInThisYear = [];
	days.forEach(function(e){
	    token = e.split(",");
	    stockDay = new StockDay(token);
	    dayInThisYear.push(stockDay)
	})
	pause("not saved! use history.day")
	return true;
    }
    print("load urls " + urls.length)
    conf.crawl();
    saveVar('stock.Year',stockYear)
    // return [stockYear,dayInThisYear]
}

print("10jqka.crawl.history.last loaded");


//updateLastDay=function(){
//    var kkk = loadVarExt('allStock')
//    loadx('stock.day')
//
//    stockYear = []
//
//    var urls = [];
//    for(var index in allStock.data){
//	var stock = allStock.data[index]
//	stockFrom = allStock.stockFrom(stock.code);
//	if(stockFrom == '1'){
//	    prf = "sh_"
//	}else{
//	    prf = "sz_"
//	}
//	var url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/last.js'
//	urls.push({"url":url, "code":stock.code});
//    }
//    var conf = new CrawlConfig();
//    conf.callback = function(urlInfo, data){
//	var pos = data.indexOf("(")
//	data = Misc.substring(data , "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
//	data = JSON.parse(data);
//	if(!data){
//	    print("no data!" + data);
//	    return true;
//	}
//	print(data.name + " start at " + data.start);
//	stockYear.push({
//	    code:urlInfo.code,
//	    year:data.start
//	})
//
////	members(data)
//	var days = data.data.split(";");
//	dayInThisYear = [];
//	days.forEach(function(e){
//	    token = e.split(",");
//	    stockDay = new StockDay(token);
//	    dayInThisYear.push(stockDay)
//	})
//	pause("not saved! use history.day")
//	return true;
//    }
//    print("load urls " + urls.length)
//    mdir = openMdir4Write("mdir/history.last/"+nowday());
//    try{
//	crawlIterator(urls, mdir, conf);
//    }finally{
//	mdir.close()
//    }
//    saveVar('stock.Year',stockYear)
//    // return [stockYear,dayInThisYear]
//}
//
//print("10jqka.crawl.history.last loaded");
