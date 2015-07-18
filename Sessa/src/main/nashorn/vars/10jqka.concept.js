if(typeof allStock == undefined || !allStock){
    loadVarExt('allStock')
}else{
    print('all stock already loaded')
}
allStock.data.forEach(function(s){
    s.concepts = []
})
var _concepts = loadVar('10jqka.concept')
notFound = [];
_concepts.groups.forEach(function(e){
    prefix = e.name;
    e.tags.forEach(function(tag){
	if(tag.tcTitle){
	    name = prefix + "." + tag.tcTitle
	}else{
	    name = prefix + "." + tag.name
	}
//	print(name);
	tag.codes.forEach(function(code){
	    var stock = allStock.getByCode(code);
	    if(!stock){
		//print("stock not found for code " +  code);
		if(-1 == notFound.indexOf(code)){
		    notFound.push(code)
		}
	    }else{
		if(!stock.concepts){
		    stock.concepts = []
		}
		stock.concepts.push(name)
//		print("bind concept " + name + " for " + JSON.stringify(stock))
	    }

	})
    });
})
print(" total not found stock for concepts is " + notFound.length)
//print("not found code is " + JSON.stringify(notFound) + "\n total NOT FOUND NUMBER " + notFound.length)