loadx("stock.day");
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
	//pause();

//	sDays.forEach(function(e){
//	arr.push(e);
//	})

	print("put " + cnt +"  into " + stock.code +", now has " + arr.length)
    }catch(exc){
	print(exc);

    }
}

var downloadAllDays=function(){
    loadVarExt('allStock')
    mdir = openMdir4Write("mdir/history.year/"+day());
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
    mdir = openMdir4Write("mdir/history.this.year/"+day());
    print("start crawl")

    try{

	loadVarExt('allStock')
	var year = day().substring(0, 4);
	allStock.data.forEach(function(stock){

	    print("year " + year);

	    var code = stock.code

	    print("crawl " + JSON.stringify(stock) + " for year " + year)
	    yearList = [];
	    crawlYear(year, stock, yearList) 

	    print("save" + stock + " for year " + year)
	    saveVar("days/" + code + "/" + year, yearList, true)
	})

    }finally{
	mdir.close()
    }
}
print("loaded");