//var NaN = 'NaN'
var toNum = function(value){
    try{
	return Double.parseDouble(value)
    }catch(e){
	return NaN;
    }
}
var isNum = function(){
    
}
var notLessThan = function(left, rightNum){
    leftNum = toNum(left)
    return left >= rightNum
}