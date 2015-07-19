var intDay=function(time){
    return nowDay("yyyyMMdd", time)
}
var nowDay = function(fmt, time){
    if(!fmt){
	fmt = "yyyy-MM-dd"
    }
    if(!time){
	time = now()
    }
    return new SimpleDateFormat(fmt).format(time);
}

var nowYear = function(time){
    return nowDay("yyyy",time)
}
var nowMonth = function(time){
    return nowDay("MM",time)
}
var nowDayOfMonth = function(time){
    return nowDay("dd",time)
}
var nowHour = function(time){
    return nowDay("HH",time);
}
var nowMinute = function(time){
    return nowDay("mm",time);
}
var nowSecond = function(time){
    return nowDay("ss",time);
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
    return arg
    
}

var now = function(){
    return System.currentTimeMillis();
}

var confirm = function(prompt){
    if(!prompt){
	prompt = "continue?"
    }
    return  Consoler.confirm(prompt);
}

var pause = function(arg){
    if(!arg){
	arg = ""
    }
    Consoler.readString(arg)
}

var ArrayMap = function(){
    this.dict = {};
    this.put = function(key, element){
	dictArr = this.dict[key];
	if(!dictArr){
	    dictArr = [];
	    this.dict[key] = dictArr;
	}
	dictArr.push(element);
    }
    this.toDict = function(){
	return this.dict;
    }
    this.get = function(key){
	return this.dict[key];
    }
    this.hasKey = function(key){
	return !!this.dict[key]
    }
    this.visit = function(callback){
	for(var key in this.dict){
	    callback(key, this.dict[key])
	}
    };
    this.forEach = function(callback){
	visit(callback)
    }
}

var asJsArray = function(arr){
    ret =[]
    for(var p in arr){
	ret.push(arr[p]);
    }
    return ret;
}