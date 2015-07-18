var crawlHolders = function(){
    loadVarExt('allStock');

    urls = [];
    allStock.data.forEach(function(stock){
	urls.push({"url":
	    "http://basic.10jqka.com.cn/"+stock.getShortCode()+"/holder.html"
	});
    });
    print(urls.length)
    conf = new CrawlConfig();
    conf.urls = urls;
    
    conf.callback = function(urlInfo){
	return true;
    }
    
    conf.crawl();

}
crawlHolders();