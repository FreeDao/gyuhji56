/**
 * 打印历史命令。简化命令为 his(), sessa快捷命令为//his
 * 
 * @param file{String} 默认为当前history文件
 */
var hisCmd=function(file){

    if(!file){
	file = hisFile;
    }
    file = new File(file)
    var lr = new LineReader(file);
    var count = 0;
    hisArr = []
    for(;lr.hasNext();){
	var line = lr.next();
	hisArr.push(line);
	count ++;
    }
    lr.close();


    var left = 100 - count;

    if(left > 0){
	files  = new File(file).getParentFile().listFiles();
	last = null;
	for(i in files){
	    e = files[i]

	    if(!e.equals(file)){

		if(last == null ||
			e.lastModified() > last.lastModified()){
		    last = e;
		}

	    }
	}
	
	if(last != null){
	    var lr = new LineReader(last);
	    hisArr2 = []
	    for(;lr.hasNext();){
		var line = lr.next();
		hisArr2.push(line);
	    }
	    lr.close();
	    var startIndex = hisArr2.length - left;
	    if(startIndex < 0){
		startIndex=0;
	    }
	    print("//# " + last);
	    for(i in hisArr2){
		if(i < startIndex){
		    continue;
		}
		print(hisArr2[i])
	    }
	}
    }
    print("//# " + file);
    hisArr.forEach(function(e){
	print(e);
    });


    print("");
}


var his=function(){
     return hisCmd();
}