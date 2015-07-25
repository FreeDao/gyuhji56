/**
 * 抓取一个页面
 * @param {string} urlStr url to crawl
 */
var crawl = function (urlStr) {
    print("crawling " + urlStr)
    var url = new URL(url);
    var crl = HttpCrawler.makeCrawler(url);
    return crl.getString();
}
/**
 * 抓取文件，并将文件存放到mdir中. 先在mdir中寻找是否已经抓取并缓存，如果缓存了则直接返回（避免多次抓取）
 * @param urlStr {string} -the url to crawl
 * @param mdir the mdir to store the
 *
 */
var crawlWithMdir = function (urlStr, mdir) {
    if (!mdir) {
        throw "mdir not set";
    }
    var mfile = mdir.getMFileByName(urlStr);
    var ret;
    print("crawling " + urlStr)
    if (mfile) {
        log.debug("hit in mdir for url %s", urlStr)
        var bytes = mfile.getBytes();
        return new java.lang.String(bytes, "utf-8");
    } else {
        var url = new URL(urlStr);
        var crl = HttpCrawler.makeCrawler(url);
        ret = crl.getString();
        var ips = new ByteArrayInputStream(ret.getBytes("utf-8"));
        mdir.addFile(urlStr, ips, false);
    }
    return ret;
}

/**
 * @class CrawlConfig
 * the most usefull crawl tool<br>
 * members can set:
 * <br><b>sleepWhenCrawlSuccess</b> default 0
 * <br><b>sleepWhenCrawlError</b> default 0
 * <br><b>stopWhenCrawlError</b> default false
 * <br><b>savePath</b> usually config as: conf.savePath = conf.path(saveName)
 * <br><b>urls</b> the urls to crawl, data type as: [{'url':urlStr}, ...]
 * <br><b></b>
 */
var CrawlConfig = function () {
    /**default 0**/
    this.sleepWhenCrawlSuccess = 0;
    /**default 0**/
    this.sleepWhenCrawlError = 0;
    /**default false**/
    this.stopWhenCrawlError = false;
    /**usually config as: conf.savePath = conf.path(saveName)**/
    this.savePath = "";
    /**the urls to crawl, data type as: [{'url':urlStr}, ...]**/
    this.urls = [];
    /**
     * the parse function, if not set use default  interactive function<br>
     * PLEASE set this function if need parse,
     * if not need parse, please set it as function(){return true}<br>
     *
     * @return true/false, if false return the crawl loop will stop
     * @throws if throw exception, the crawl loop will stop, so please be sure the exception be catched
     */
    this.callback = function (url, data) {
        print(data)
        print("crawl url ok, in callback now")
        return std.confirm("continue?");
    };
    /**
     * make a mdir name as mdir/CrawlerTmpHome/$typeName/$nowday()
     * @param typeName the crawlType, you need only set a name,without a full path
     * @return the path {string}
     */
    this.path = function (typeName) {
        return "mdir/CrawlerTmpHome/" + Misc.formatFileName(typeName)
            + "/" + nowday()
    }
    /**
     * the crawl caculate info
     */
    this.crawlRoundInfo = {
        checkCount: 0,
        okCrawl: 0,
        crawlCount: 0,
        failCount: 0,
        urlCount: this.urls.length,
        parseFail: 0
    }
    /**
     * the report function reporting the excute result
     */
    this.report = function () {
        print("crawlReport:");
        printJson(this.crawlRoundInfo);
        if (this.crawlRoundInfo.failCount == 0 ||
            this.crawlRoundInfo.parseFail ||
            urlCount != checkCount) {
            print("Error!!! maybe not all success")
        }
        if (this.err) {
            print("errors:" + this.err)
        }
        if (this.warns.length) {
            print("warning:")
            this.warns.forEach(function (w) {
                print("[warn] " + w)
            })
        }
    }
    /**
     * the function start crawling.<br>
     *
     * you can see report use report(), or just view the member crawlRoundInfo
     *
     */
    this.crawl = function () {
        if (this.urls.length == 0 || !this.urls[0].url) {
            print("urls not sets, or has not 'url' field in urls items")
            return;
        }

        if (this.savePath.length == 0) {
            this.savePath = "mdir/CrawlerTmpHome/" + Misc.formatFileName(urls[0].url)
                + "/" + nowDay()
            var mdirDir = new File(this.savePath);
            if (mdirDir.exists()) {
                print("savePath not sets, and create tmp file fail : " + mdirDir)
                return;
            }
        }

        print("saving to " + this.savePath)
        mdir = openMdir4Write(this.savePath);
        try {
            crawlUrls(this.urls, mdir, this);
        } finally {
            mdir.close();
        }
        this.report()
    }
    this.warns = [];
    this.addWarn = function (warn) {
        this.warns.push(warn)
    }
    //sprint("crawlConfig init")
}
/**
 * crawl urls. usually please use CrawlConfig instance, if mdir is not shared
 * @param urls urls to crawl
 * @param mdir the mdir to store
 * @param conf {CrawlConfig} the config
 */
var crawlUrls = function (urls, mdir, conf) {
    return crawlIterator(urls, mdir, conf)
}
/**
 * @private
 *
 * @param urls
 * @param mdir
 * @param conf {CrawlConfig}
 */
var crawlIterator = function (urls, mdir, conf) {
    if (typeof(conf) == "undefined") {
        throw "conf not set";
    }

    for (var k in urls) {
        urls[k].isCrawl = false;
    }
    //print(callback)
    try {
        var crawlRoundInfo = conf.crawlRoundInfo;
        var st = now();
        for (var index in urls) {
            var shouldBreak = false;
            var urlInfo = urls[index];
            crawlRoundInfo.checkCount++;
            if (urlInfo.isCrawl) {
                continue;
            }
            crawlRoundInfo.crawlCount++;
            try {
                urlInfo.html = crawlWithMdir(urlInfo.url, mdir);
                urlInfo.isCrawl = true;
                print("got " + urlInfo.html.length / 1000.0 + " KB for " + urlInfo.url)
                crawlRoundInfo.okCrawl++;

                if (conf.sleepWhenCrawlSuccess) {
                    Misc.sleep(conf.sleepWhenCrawlSuccess)
                }
            } catch (e) {
                crawlRoundInfo.failCount++
                exception(e)
                if (conf.stopWhenCrawlError) {
                    break;
                }
                if (conf.sleepWhenCrawlError) {
                    Misc.sleep(conf.sleepWhenCrawlError)
                }
            } finally {
                if (crawlRoundInfo.checkCount % 10 == 0) {
                    jpr("!!!!!total %s, crawl %s use %s seconds, average %s page per second",
                        urls.length,
                        crawlRoundInfo.checkCount, (now() - st) / 1000,
                        Math.floor(crawlRoundInfo.checkCount * 1000 * 10 / (now() - st)) / 10)
                }
                try {
                    if (!conf.callback(urlInfo, urlInfo.html)) {
                        crawlRoundInfo.parseFail++
                        shouldBreak = true;
                    }
                } catch (exc) {
                    crawlRoundInfo.parseFail++
                    conf.err = exc
                    exception(exc);

//		    if(!confirm("continue?")){
//		    break
//		    }
                }
            }
            if(shouldBreak){
                break
            }
        }

        print("crawl info " + JSON.stringify(crawlRoundInfo))

    } finally {
        mdir.fsyn();
    }
}
