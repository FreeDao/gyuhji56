 averageWithCallback = function(arr,func){
    sum = 0.0;
    arr.forEach(function(ele){
        sum += Double.parseDouble(func(ele));
    });
    return sum/arr.length;
}