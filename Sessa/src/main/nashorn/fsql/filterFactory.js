var filterFactoryMap={
	factoryMap : new ArrayMap(),
	registerFactory:function(factory, alias){
	    for(var index in alias){
		var name = alias[index];
		name = name.toLowerCase().trim();
		if(name.length){
		    this.factoryMap.put(name, factory)
		}else{
		    print("Error: empty name for factory: " + factory)
		}
	    }
	},

	getFilter : function(nameOrg, op, opValue){
	    var name = nameOrg.trim().toLowerCase();
	    var factorys =  this.factoryMap.get(name)
	    if(factorys){
		for(var index in factorys){
		    var factory = factorys[index];
		    var filter = factory.createFilter(nameOrg, op, opValue);
		    if(filter){
			return filter;
		    }
		}
	    }

	},
	get : function(nameOrg, op, opValue){
	    return this.getFilter(nameOrg, op, opValue)
	}
}