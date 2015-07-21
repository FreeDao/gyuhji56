/**
 * @class SeStream
 * 一个支持stream 操作的实现
 */

var SeStream = function(array){
    this.oldArr=[]
    /**内置数组，是stream的输入，又是输出（上个arr会被压栈到 oldArr）**/
    this.arr = array;

    /**
     * callbakc.filter()(如果存在的话)函数或者通过callback()函数对arr中元素过滤。
     * @param callback  过滤object或回调函数. 
     * 如果callback.filter存在则调用callback.filter(ele),否则调用callback(ele). 接受ele参数，返回 true/false.
     * 
     */
    this.filter=function(callback){
	arrNew = [];
	this.arr.forEach(function(ele){
	    var ret;
	    if(callback.filter){
		ret = callback.filter(ele);
	    }else{
		ret=callback(ele)
	    }
	    if(ret){
		arrNew.push(ele);
	    }
	})
	return this.backupAndSet(arrNew);
    };
    /**
     * 根据callback.order(ele)/callback(ele)计算arr每个元素ele的排序值，然后根据排序值对arr排序.
     * @param callback 返回元素排序值的函数。
     * 如果callback存在order()函数，则使用call.order(ele)计算排序值，
     * 否则使用callback(ele)计算排序值
     */
    this.sort = function(callback){
	Collections.sort(arr, function(o1, o2){
	    if(callback.order){
		return Math.floor(callback.order(o1) - callback.order(o2) + 0.5);
	    }else{
		return Math.floor(callback(o1) - callback(o2) + 0.5);
	    }
	})
	return this.arr;
    };
    /**
     * 根据 callback {java.util.Comparator}对arr排序
     * @param callback java.util.Comparator实例
     */
    this.sortWithComparator =function(callback){
	Collections.sort(this.arr, callback)
	return this.arr;
    }
    /**
     * 元素遍历函数。
     * @param callback, 作用于ele的object/function.
     * 如果callback.visit()存在，则执行callback.visit(ele),否则调用callback(ele)
     */
    this.forEach = function(callback){
	this.arr.forEach(function(ele){
	    if(callbakc.visit){
		callback.visit(arr) 
	    }else{
		callback(arr) 
	    }
	});
    }
   

    /**
     * 将当前所有元素映射为新元素，并将新元素放入一个数组（如果非空的话）
     */
    this.map = function(mapper){
	arrNew = [];
	this.arr.forEach(function(ele){
	    if(mapper.map){
		newOne = mapper.map(ele);
	    }else{
		newOne = mapper(ele);
	    }

	    if(newOne){
		arrNew.push(newOne);
	    }
	})
	return this.backupAndSet(arrNew);
    }
    /**
     * 回撤上次操作，即将上次操作结果丢弃（对stream中元素的操作无法撤销）
     * @return 被回撤的arr
     */
    this.back = function(){
	var pos = this.oldArr.length -1;
	this.arr = this.oldArr[pos - 2]
	var ret = this.oldArr[pos - 1]
	this.oldArr = this.oldArr.splice(pos, 1)
	return ret;
    }

    /**
     * @private
     * 设置当前stream操作的数组
     * 
     * 
     * @param arr 数组
     */
    this.backupAndSet = function(arr){
	this.oldArr.push(this.arr);
	this.arr = arr;
	return this.arr;
    }

}


