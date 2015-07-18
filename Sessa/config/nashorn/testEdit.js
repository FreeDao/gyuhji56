if(false){
    loadVarExt('allStock')
    i=0
    okNum = 0;
    failNum = 0;
    toScan = []
    allStock.data.forEach(function(stock){
	i++;
	if(i< 111000){
	    sdays = stock.days(20150707, 20150710)
//	    printJson(sdays)
	    if(sdays.length != 4){
		failNum = failNum +1
		jpr("???????/error %s for %s", sdays.length, stock)
	    }else{
		okNum ++
		toScan.push({'stock':stock,'days' : sdays})
	    }
	}
    })
}
toScan.forEach(function(e){
    stock = e.stock
    sdays = stock.days(20150301, 20150331)
    if(sdays.length > 5){
	aver = averageWithCallback(sdays, function(ex){
//	    printJson(ex.close)
	    return ex.close;
	})
	e['ave'] = aver
    }

})


//q