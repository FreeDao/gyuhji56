var crawlYear= function(year,stock){

    stockFrom = allStock.stockFrom(stock.code);
    if(stockFrom == '1'){
	prf = "sh_"
    }else{
	prf = "sz_"
    }
    var url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/'+year+'.js'

    try{

	js = crawlWithMdir(url, mdir).trim()
	print("got length " + js.length);
	var pos = js.indexOf("(")
	js = Misc.substring(js, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
	data = JSON.parse(js);
	//printJson(data);
//	startYear = data.start.substring(0,4);
//	print(data.name + " start at " + startYear);
//	stockYear.push({
//	code:stock.code,
//	year:data.start
//	});
	//pause();
    }catch(exc){
	print(exc);
	
    }
}

/////codes
loadVarExt('allStock')
mdir = openMdir4Write("mdir/history.year/"+day());
try{

    sy = loadVar('stock.year')

    for(var year = 2015; year > 1990; year --){
	print("year " + year);
	for(var index in sy){	
	    var code = sy[index].code
	    var stock = allStock.getByCode(code);
	    minYear = sy[index].year.substring(0,4);
	    print("min year " + minYear + " for " + JSON.stringify(stock) )
	    print("check " + JSON.stringify(stock) + "for year " + year)
	    if(minYear <= year){
		print("crawl " + JSON.stringify(stock) + " for year " + year)
		crawlYear(year, stock) 
	    }
	}
    }

}finally{
    mdir.close()
}

