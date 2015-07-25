//x = 'daf'
//switch(x){
//case 'aa':
//break;
//case 'daf':
//print('aaaaaaaaaaa');
//break;
//default:
//print('not found');
//}

loadVarExt('allStock')
loadVarExt('10jqka.basic')
loadVarExt('10jqka.concept')

fMap = new HashMap();
allStock.data.forEach(function(stock){
    for(var pos in stock){
	cnt = fMap.get(pos)
	if(cnt == null){
	    cnt = new Counter();
	    fMap.put(pos, cnt);
	}
	cnt.count(stock[pos])
    }

})
var keys = fMap.keySet();
var lst = new ArrayList(keys)
Collections.sort(lst, function(s1, s2){
    return s1 < s2 ? -1 : 1;
})
var keyss = asJsArray(lst)

var nameMap={

}
var idx =0;
keyss.forEach(function(e){
    idx ++;
    nameMap[idx] = e
    print(idx + "\t" + e)

})
print("var: fMap")
topNum = 100;
print("topEntry:" +topNum)
var viewFields = function(){
    while(1){
	pos = std.readLine("q to quit>");
	if(!pos){
	    continue
	}
	if(isNaN(pos)){
	    if(pos== 'q'){
		print("quiting... use viewFields() to recall")
		break;
	    }
	    print(" error position")
	    continue;
	}
	pos = parseInt(Number(pos))
	name = nameMap[pos];
	if(name){
	    print(fMap[name].toEntry(topNum))
	}else{
	    print("invalid pos " + pos)
	}
    }
}

viewFields()
