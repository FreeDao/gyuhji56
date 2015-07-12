var averageWithCallback = function(arr,func){
    sum = 0;
    arr.forEach(function(stock){
	sum += func(arr);
    });
    return sum/arr.length;
}