
var SeStream = function(array){
    this.oldArr=[]
    this.arr = array;
    
    /**
     * get boolean value for elements in arr, using function callback.
     * if value is true, accept, orelse drop
     */
    this.filter=function(callback){
	arrNew = [];
	this.arr.forEach(function(stock){
	    if(callback(stock)){
		arrNew.push(stock);
	    }
	})
	return this.backupAndSet(arrNew);
    };
    /**
     * sort arr using callback to generate a int value
     */
    this.sort = function(callback){
	sortWithCallback(this.arr, callback)
	return this.arr;
    };
    /**
     * sort arr using comparator function callback(o1, o2),return a value indicate 
     * compare value for o1/o2
     */
    this.sortWithComparator =function(callback){
	Collections.sort(this.arr, callback)
	return this.arr;
    }
    /**
     * for every element, generate
     */
    this.classfy = function(callback){
	dict = new ArrayMap();
	this.arr.forEach(function(ele){
	    classId = callback(ele);
	    dict.put(classId, ele)
	})
	arrNew = []
	dict.visit(function(key, arr){
	    obj = {key:key, value:arr}
	    arrNew.push(obj);
	});
	
	return this.backupAndSet(arrNew);
    }

    /**
     * 将当前所有元素映射为新元素，并将新元素放入一个数组（如果非空的话）
     */
    this.map = function(mapper){
	arrNew = [];
	this.arr.forEach(function(ele){
	    newOne = mapper(ele);
	    if(newOne){
		arrNew.push(newOne);
	    }
	})
	return this.backupAndSet(arrNew);
    }
    this.back = function(){
	var pos = this.oldArr.length -1;
	this.arr = this.oldArr[pos]
	this.oldArr = this.oldArr.splice(pos, 1)
	return this.arr;
    }
    
    this.backupAndSet = function(arr){
	this.oldArr.push(this.arr);
	this.arr = arr;
	return this.arr;
    }
}


