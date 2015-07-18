var Lucky = function(){
    print('in test')
    loadVarExt('10jqka.basic')
    loadVarExt('10jqka.concept');
    this.oldArr=[]
    this.arr = []
    allStock.data.forEach(function(stock){
	arr.push(stock);
    });

    this.filter=function(callback){
	arrNew = [];
	this.arr.forEach(function(stock){
	    if(callBack(stock)){
		arrNew.push(stock);
	    }
	})
	this.oldArr.push(this.arr);
	this.arr = arrNew;
    };
    this.sort = function(callBack){
	sortWithCallback(this.arr, callBak)
    };
    this.sortWith
    
    /**
     * 将当前数组转换，并将转换后的元素放入新数组（如果非空的话）
     */
    this.map = function(mapper){
	arrNew = [];
	this.arr.forEach(function(stock){
	    newOne = mapper(stock);
	    if(newOne){
		arrNew.push(newOne);
	    }
	})
	this.oldArr.push(this.arr);
	this.arr = arrNew;
    }
}

var luck = new Lucky();
