loadVarExt('allStock')
i=0

okNum = 0;
failNum = 0;
toScan = []
allStock.data.forEach(function(stock){
    i++;
    if(i< 111000){

	sdays = stock.days(20150707, 20150710)

//	printJson(sdays)
	if(sdays.length != 4){
	    failNum = failNum +1
	    jpr("???????/error %s for %s", sdays.length, stock)
	}else{
	    okNum ++
	    toScan.push({'stock':stock,'days' : sdays})
	}
    }
})

jpr("okNum %s failNum %s", okNum, failNum)

sdayValue = function(sday){
    if(sday.days[0].turnover == 0){
	sday.days[0] = 0.00001;
    }
    diff0 = sday.days[1].turnover / sday.days[0].turnover
    diff1 = sday.days[1].turnover / sday.days[2].turnover
    diff2 = sday.days[2].turnover / sday.days[3].turnover

    if(diff0 > 1 &&diff1 > 1 && diff2 > 1){
	var ret = (diff0 - 1) *(diff1 - 1) * (diff2 - 1);
	
	return Math.floor(ret + 0.5)
    }else{
	return -1
    }
}
sortWithCallback(toScan, sdayValue)

bottomWithCallback(toScan, 30, function(e){
    printJson(e.stock)
})


