loadScript('10jqka.crawl.newest')
mdir
loadVar('10jqka.newest')
x =loadVar('10jqka.newest')
head(x)
head(json.stringify(x))
head(JSON.stringify(x))
test()
test()
sdays
sdays[0]
printJson(sdays[0])
sdays.length
e
ex
toScan[0]
toScan[0].ave
sdays = stock.days(20150301, 20150331)
aver = averageWithCallback(sdays, function(ex){
	    //printJson(ex)
	    return ex.close;
})
//b
aver
aver = averageWithCallback(sdays, function(ex){
	    printJson(ex)
	    return ex.close;
})

aver = averageWithCallback(sdays, function(ex){
	    printJson(ex.close)
	    return ex.close;
})

averageWithCallback
averageWithCallback = function(arr,func){
    sum = 0;
    arr.forEach(function(ele){
		print(func(ele))
        sum += func(ele);
    });
    return sum/arr.length;
}
}aver = averageWithCallback(sdays, function(ex){return ex.close})
aver = averageWithCallback(sdays, function(ex){return ex.close})
aver = averageWithCallback(sdays, function(ex){print("value:" +ex.close);return ex.close})
averageWithCallback = function(arr,func){
    sum = 0;
    arr.forEach(function(ele){
		print("func()=" +func(ele))
        sum += func(ele);
    });
    return sum/arr.length;
}
aver = averageWithCallback(sdays, function(ex){print("value:" +ex.close);return ex.close})
averageWithCallback = function(arr,func){
    sum = 0;
    arr.forEach(function(ele){
		print("func()=" +func(ele))
        sum += func(ele);
		print("sum=" + sum)
    });
    return sum/arr.length;
}
aver = averageWithCallback(sdays, function(ex){print("value:" +ex.close);return ex.close})





averageWithCallback = function(arr,func){
    sum = 0;
    arr.forEach(function(ele){
		print("func()=" +func(ele))
        sum += double.parseDouble(func(ele));
		print("sum=" + sum)
    });
    return sum/arr.length;
}
aver = averageWithCallback(sdays, function(ex){print("value:" +ex.close);return ex.close})
averageWithCallback = function(arr,func){
    sum = 0.0;
    arr.forEach(function(ele){
		print("func()=" +func(ele))
        sum += Double.parseDouble(func(ele));
		print("sum=" + sum)
    });
    return sum/arr.length;
}
aver = averageWithCallback(sdays, function(ex){print("value:" +ex.close);return ex.close})

aver
0 + "1899"
0 + "1899.0"
add(0, "1899.0")
Math.add(0, "1899.0")
Maths.add(0, "1899.0")
0.0 + "1899.0"
0.03 + "1899.0"
Double.parseDouble(56789)
Double.parseDouble('56789')
