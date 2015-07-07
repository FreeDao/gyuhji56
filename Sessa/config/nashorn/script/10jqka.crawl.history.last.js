
loadVarExt('allStock')
mdir = openMdir4Write("mdir/history.last/"+day());
stockYear = []
for(var index in allStock.data){
    var stock = allStock.data[index]
    printJson(stock);
    stockFrom = allStock.stockFrom(stock.code);
    if(stockFrom == '1'){
	prf = "sh_"
    }else{
	prf = "sz_"
    }
    var url='http://d.10jqka.com.cn/v2/line/'+prf+allStock.trimCode(stock.code)+'/01/last.js'
    try{
	st = System
	js = crawlWithMdir(url, mdir).trim()
	//print(js);
	var pos = js.indexOf("(")
	js = Misc.substring(js, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
	data = JSON.parse(js);
	//printJson(data);
	startYear = data.start.substring(0,4);
	print(data.name + " start at " + startYear);
	stockYear.push({
	    code:stock.code,
	    year:data.start
	});
	//pause();
    }catch(exc){
	print(exc);
	pause()
    }
}
mdir.close()