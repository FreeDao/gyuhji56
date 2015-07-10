loadVarExt('allStock')

mdir = openMdir4Write('mdir/doctor/'+ "2015-07-07");
urls=[]
try{
    for(var index in allStock.data){
	stock = allStock.data[index];  
	url = "http://doctor.300033.info/"+stock.getShortCode()+"/"   ;
	urls.push(url) ;
    }

    var callBack = function(html){
	print("size " + html.length + " for url " + url) ;
	return true;
    }
    crawlCallBack(urls, mdir, callBack);
}finally{
    mdir.close();
}









































