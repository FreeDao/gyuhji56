var hisCmd=function(file){
    if(!file){
	file = hisFile;
    }
    print("\n/**history commands for "+file+"**/\n");
    var lr = new LineReader(file);
    for(;lr.hasNext();){
	var line = lr.next();
	print(line)
    }
    lr.close();
    print("\n/**history command end**/\n");
}