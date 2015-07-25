//var NaN = 'NaN'
var toNum = function(value){
    try{
	return Double.parseDouble(value)
    }catch(e){
        log.debug("error " + e);
	return NaN;
    }
}
var isNum = function(){
    
}
var notLessThan = function(left, rightNum){
    var leftNum = toNum(left)
    return leftNum >= toNum(rightNum)
}