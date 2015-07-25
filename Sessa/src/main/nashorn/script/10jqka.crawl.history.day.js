
var crawlYear= function(year,stock, list){

    stockFrom = allStock.stockFrom(stock.code);
    if(stockFrom == '1'){
	prf = "sh_"
    }else{
	prf = "sz_"
    }
    url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/'+year+'.js'

    try{

	js = crawlWithMdir(url, mdir).trim()
//	print("got length " + js.length);
	var pos = js.indexOf("(")
	var js = Misc.substring(js, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
	data = JSON.parse(js);
	days = data.data.split(";");
	var cnt  =0
	days.forEach(function(day){
	    cnt ++
	    tokens = day.split(","); 
//	    print(tokens)
	    oneDay = new StockDay(tokens);
	    oneDay.code = stock.code;
	    list.push(oneDay);
//	    printJson(oneDay)
//	    printJson(oneDay.high)
	});
//	startYear = data.start.substring(0,4);
//	print(data.name + " start at " + startYear);
//	stockYear.push({
//	code:stock.code,
//	year:data.start
//	});
	//std.pause();

//	sDays.forEach(function(e){
//	arr.push(e);
//	})

	print("put " + cnt +"  into " + stock.code +", now has " + arr.length)
    }catch(exc){
	print(exc);

    }
}

var toHistoryUrl = function(stock, year){
    stockFrom = allStock.stockFrom(stock.code);
    if(stockFrom == '1'){
	prf = "sh_"
    }else{
	prf = "sz_"
    }
    url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/'+year+'.js'
    return url;
}

var parseHistoryData = function(html, code){
    var pos = html.indexOf("(")
    txt = Misc.substring(html, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
    list = []
    if(txt){
	data = JSON.parse(txt);
	if(!data.data)return list;
	days = data.data.split(";");
	var cnt  =0

	days.forEach(function(day){
	    cnt ++
	    tokens = day.split(","); 
	    oneDay = new StockDay(tokens);
	    oneDay.code = code;
	    list.push(oneDay);
	});
	return list;
    }else{
	return []
    }
}
var downloadAllDays=function(){
    loadVarExt('allStock')
    mdir = openMdir4Write("mdir/history.year/"+nowday());
    print("start crawl")
    {
	try{

	    var sy = loadVar('stock.year')
	    for(var index in sy){
		list = {}
		for(var year = 2015; year > 1990; year --){
		    print("year " + year);

		    var code = sy[index].code
		    var stock = allStock.getByCode(code);
		    minYear = sy[index].year.substring(0,4);
		    print("min year " + minYear + " for " + JSON.stringify(stock) )
		    print("check " + JSON.stringify(stock) + "for year " + year)
		    if(minYear <= year){
			print("crawl " + JSON.stringify(stock) + " for year " + year)
			yearList = [];
			crawlYear(year, stock, yearList) 
			list[year] = yearList;
		    }else{
			break;
		    }

		}

//		Collections.sort(list, new Comparator(){
//		compare:function(v1, v2){
//		return v1.day - v2.day
//		}
//		})
		for(k in list){
		    print("save" + sy[index].code + " for year " + k)
//		    print(list[k]);
		    saveVar("days/" + sy[index].code + "/" + k, list[k], true)
		}


	    }

	}finally{
	    mdir.close()
	}
    }
}


downloadThisYear=function(){
    loadVarExt('allStock')
    print("start crawl")
    var yearNum = nowYear();
    urls= [];
    allStock.data.forEach(function(stock){

	url = toHistoryUrl(stock, yearNum);
	urls.push({'url':url, "stock":stock})
//	print("crawl " + JSON.stringify(stock) + " for year " + year)
//	yearList = [];
//	crawlYear(year, stock, yearList) 
//	saveVar("days/" + code + "/" + year, yearList, true)
    })

    conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('history.this.year')
    updated = 0;
    sim = 0;
    misMatch = 0;
    conf.callback=function(urlInfo){
	try{
	    yearList = parseHistoryData(urlInfo.html)
	    oldYear = urlInfo.stock.loadYear(yearNum)

	    if(!oldYear || oldYear.length < yearList.length){
		jpr("old size %s, new size %s",oldYear? oldYear.length :"0", yearList.length);
		saveVar("days/" + urlInfo.stock.code + "/" + yearNum, yearList, true)
		updated++
	    }else if(oldYear.length == yearList.length){
		jpr("no change for %s", JSON.stringify(urlInfo.stock));
		sim++
	    }else{
		misMatch ++
		errInfo = "?????????ERROR! data size mismatch " + oldYear.length +" > "+ yearList.length + ", for " + JSON.stringify(urlInfo.stock);
		print (errInfo)
		conf.addWarn( errInfo)
		return true
	    }
	}catch(exc){
	    exception(exc)
	    if(!std.confirm("continue")){
		return false;
	    }
	}
	return true;
    }
    conf.crawl()
    jpr("updated = %s,    sim = %s    misMatch = %s",updated,sim,misMatch)
}
print("loaded");