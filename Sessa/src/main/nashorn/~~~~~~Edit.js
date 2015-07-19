// 
loadVarExt('10jqka.basic')
loadVarExt('10jqka.concept')
var stream = new SeStream(allStock.data)

//pe < 40
stream.filter(function(ele){
    rule = "市盈率(动态) >= 40";
    return ele.is(rule);
    
    
})