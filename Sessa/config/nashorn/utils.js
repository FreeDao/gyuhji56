var day = function(){
    return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
}

var year = function(){
    return day().substring(0,4)
}
var month = function(){
    return day.substring(5,7)
}

var members=function(obj){
    try{
	var lst = Arrays.asList(obj.getClass().getMethods())
	Collections.sort(lst, function(o1, o2){
	    return o1.getName().compareTo(o2.getName())
	})
	print(lst.size())
	lst.forEach(function(met){
	    print(met.getName() + "\t" + met.getReturnType())
	})
    }catch(exc){
	for(i in obj){
	    print(i)
	}
    }
}

var tail = function(str, num){
    if(!top){
	num = 32;
    }
    var strx = str + "";
    if(num >= strx.length){
	num = strx.length;
    }
    print(strx.substring(str.length - num, num));
}
var head = function(str, top){
    if(!top){
	top = 32;
    }
    var strx = str + "";
    if(top >= strx.length){
	top = strx.length;
    }
    print(strx.substring(0, top));
}

var sortWithCallback = function(arr, func){

    Collections.sort(arr, function(o1, o2){
	return Math.floor(func(o1) - func(o2) + 0.5);
    })

}

var topWithCallback = function(arr, top, func){
    var index = 0;
    for(; index < top && index < arr.length; index ++){
	func(arr[index]);
    }
}

var bottomWithCallback = function(arr, num, func){
    var index = 0;
    for(; index < num; index ++){
	var pos = arr.length - 1 - index;
	if(pos < 0)
	    break;
	func(arr[pos]);
    }
}


var printJson=function(arg){
    print(JSON.stringify(arg))
}

var now = function(){
    return System.currentTimeMillis();
}