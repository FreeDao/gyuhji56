loadVarExt('10jqka.basic')
var ex = stockFieldsAttrDefines.getFieldAttrDefine('净利润-排名')
var x = ex.extractValue(allStock.data[100])
print(x)
//stream = new SeStream(allStock.data);
//filter1 = filterFactoryMap.getFilter("规模", "=", '中');
//filter2 = filterFactoryMap.getFilter("静态市盈率", "<", '40');
////filter0 = fsql2Filter("规模=中  && 静态市盈率<40");
//printJson(filter);
//stream.filter(filter1).filter(filter2);
//stream.filter(filter0)
////stream.filter(filter2);
//print(stream.arr.length)

////x = 'daf'
////switch(x){
////case 'aa':
////break;
////case 'daf':
////print('aaaaaaaaaaa');
////break;
////default:
////print('not found');
////}

//loadVarExt('allStock')
//loadVarExt('10jqka.basic')
//loadVarExt('10jqka.concept')

//var map = new HashMap();
//allStock.data.forEach(function(stock){
//for(var pos in stock){
//cnt = map.get(pos)
//if(cnt == null){
//cnt = new Counter();
//map.put(pos, cnt);
//}
//cnt.count(stock[pos])
//}

//})
//var keys = map.keySet();
//var lst = new ArrayList(keys)
//Collections.sort(lst, function(s1, s2){
//return s1 < s2 ? -1 : 1;
//})
//keyss = asJsArray(lst)

//keyss.forEach(function(e){
//print(e)
//})