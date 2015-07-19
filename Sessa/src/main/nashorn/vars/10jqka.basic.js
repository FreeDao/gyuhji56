if(!isDefined('allStock')){
    loadVarExt('allStock')
}
basic = loadVar('10jqka.basic')
notFound=[]
basic.data.forEach(function(info){
     stock = allStock.getByCode(info.code);
    if(!stock){
	//print("stock not found for code " +  code);
	if(-1 == notFound.indexOf(code)){
	    notFound.push(code)
	}
    }else{
//	print(info.code)
	for(var t in info){
	    if(t == 'code'){
		continue
	    }
//	    fakeCode = java.lang.String.format("stock.%s=%s", t, info[t])
//	    print(fakeCode)
//	    eval(fakeCode)
	    stock[t] = info[t]
	}
    }
})

if(notFound.length){
    print("not found code is " + JSON.stringify(notFound) + "\n total NOT FOUND NUMBER " + notFound.length)
}
//print("not found code is " + JSON.stringify(notFound) + "\n total NOT FOUND NUMBER " + notFound.length)