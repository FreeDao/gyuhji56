


var crawl = function(url){
    print("crawling " + url)
    var url = new URL(url);
    var crl = HttpCrawler.makeCrawler(url);
    return crl.getString();
}

var crawlWithMdir = function(urlStr, mdir){
    if(!mdir){
	throw "mdir not set";
    }
    var mfile = mdir.getMFileByName(urlStr);
    var ret;
    print("crawling " + urlStr)
    if(mfile){
	debug("hit in mdir for url " + urlStr)
	var bytes = mfile.getBytes();
	return new java.lang.String(bytes, "utf-8");
    }else{
	var url = new URL(urlStr);
	var crl = HttpCrawler.makeCrawler(url);
	ret = crl.getString();
	var ips = new ByteArrayInputStream(ret.getBytes("utf-8"));
	mdir.addFile(urlStr, ips, false);
    }
    return ret;
}


var CrawlConfig = function(){
    this.sleepWhenSuccess=0;
    this.sleepWhenError=0;
    this.stopWhenError=false;
    this.savePath="";
    this.urls=[];
    this.callback =function(url, data){
	print(data)
	print("crawl url ok, in callback now")
	return Consoler.confirm("continue?");
    };
    this.path = function(typeName){
	return "mdir/CrawlerTmpHome/" + Misc.formatFileName(typeName)
	+ "/" +day()
    }
    this.crawlRoundInfo = {
	    checkCount : 0,
	    okCrawl : 0,
	    crawlCount : 0,
	    failCount : 0,
	    urlCount : this.urls.length,
	    parseFail : 0
    }
    this.report = function(){
	print("crawlReport:");
	printJson(this.crawlRoundInfo);
	if(this.crawlRoundInfo.failCount == 0 ||
		this.crawlRoundInfo.parseFail ||
		urlCount != checkCount){
	    print("Error!!! maybe not all success")   
	}
	if(this.err){
	    print("errors:" + this.err)
	}
	if(this.warns.length){
	    print("warning:")
	    this.warns.forEach(function(w){
		print("[warn] " + w)
	    })
	}
    }

    this.crawl=function(){
	if(this.urls.length == 0 || !this.urls[0].url){
	    print("urls not sets, or has not 'url' field in urls items")
	    return;
	}

	if(this.savePath.length==0){
	    this.savePath = "mdir/CrawlerTmpHome/" + Misc.formatFileName(urls[0].url)
	    + "/" +day()
	    var mdirDir = new File(this.savePath);
	    if(mdirDir.exists()){
		print("savePath not sets, and create tmp file fail : " + mdirDir)
		return;
	    }
	}

	print("saving to " + this.savePath)
	mdir = openMdir4Write(this.savePath);
	try{
	    crawlUrls(this.urls, mdir, this);
	}finally{
	    mdir.close();
	}
	this.report()
    }
    this.warns = []
    this.addWarn = function(warn){
	this.warns.push(warn)
    }

    //sprint("crawlConfig init")
}
var crawlUrls = function(urls, mdir, conf){
    return crawlIterator(urls, mdir, conf)
}
var crawlIterator = function(urls, mdir, conf){
    if(typeof(conf) == "undefined"){
	throw "conf not set";
    }

    for(var k in urls){
	urls[k].isCrawl = false;
    }
    //print(callback)
    try{
	crawlRoundInfo = conf.crawlRoundInfo;
	var st = now();
	for(var index in urls){
	    urlInfo = urls[index];
	    crawlRoundInfo.checkCount ++;
	    if(urlInfo.isCrawl){
		continue;
	    }
	    crawlRoundInfo.crawlCount ++;
	    try{
		urlInfo.html = crawlWithMdir(urlInfo.url, mdir);
		urlInfo.isCrawl = true;
		print("got " + urlInfo.html.length /1000.0 + " KB for " + urlInfo.url)
		crawlRoundInfo.okCrawl ++;

		if(conf.sleepWhenSuccess){
		    Misc.sleep(conf.sleepWhenSuccess)
		}
	    }catch(e){
		crawlRoundInfo.failCount++
		exception(e)
		if(conf.stopWhenError){
		    break;
		}
		if(conf.sleepWhenError){
		    Misc.sleep(conf.sleepWhenError)
		}
	    }finally{
		if(crawlRoundInfo.checkCount % 10 == 0){
		    jpr("!!!!!total %s, crawl %s use %s seconds, average %s page per second",
			    urls.length,
			    crawlRoundInfo.checkCount, (now() - st)/1000,
			    Math.floor(crawlRoundInfo.checkCount * 1000 * 10 / (now() - st)) / 10 )
		}
		try{
		    if(!conf.callback(urlInfo, urlInfo.html)){
			crawlRoundInfo.parseFail ++
			break;
		    }
		}catch(exc){
		    crawlRoundInfo.parseFail ++
		    conf.err = exc
		    exception(exc);

//		    if(!confirm("continue?")){
//			break
//		    }
		}
	    }
	}

	print("crawl info " + JSON.stringify(crawlRoundInfo))

    }finally{
	mdir.fsyn();
    }
}
