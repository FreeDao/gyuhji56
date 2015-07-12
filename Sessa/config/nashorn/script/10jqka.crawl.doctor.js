loadVarExt('allStock')

mdir = openMdir4Write('mdir/doctor/'+ day());
urls=[]
try{
    for(var index in allStock.data){
	stock = allStock.data[index];  
	url = "http://doctor.300033.info/"+stock.getShortCode()+"/"   ;
	urls.push({"url":url}) ;
    }

    var callBack = function(html){
	print("size " + html.length + " for url " + url) ;
	return true;
    }
    conf=new CrawlConfig();
    conf.callback = callBack
    crawlIterator(urls, mdir, conf);
}finally{
    mdir.close();
}









































