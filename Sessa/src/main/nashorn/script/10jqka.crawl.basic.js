var text = function (doc, selector) {
    ele = doc.select(selector);
    if (ele && ele.length) {
        ele = ele[0].parent().select(".tip");
        txt = ele.text()
        return txt
    }
    return "";
}
newestInfo = {
    crlDate: now(),
    data: []
}

var crawlNewest = function () {
    loadVarExt('allStock');

    urls = [];

    allStock.data.forEach(function (stock) {
        urls.push({
            "url": "http://basic.10jqka.com.cn/" + stock.getShortCode() + "/",
            "code": stock.getShortCode()
        });
    });
    print("urls.length:" + urls.length)
    conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('10jka.newest')
    conf.callback = function () {
        try {
            doc = Jsoup.parse(urlInfo.html)
//	    ele = doc.select('#profile .mt10 td :contains(分类：)')[0].parent().select(".tip");

            info = {}

            items = doc.select('#profile .mt10 td')
            items.forEach(function (e) {
                spans = e.select("span");
                name = spans[0].text().trim()
                value = spans[1].text().trim();
                if (name.endsWith(":") || name.endsWith("：")) {
                    name = name.substring(0, name.length - 1)
                }
                info[name] = value;
                if (spans.length > 2) {
                    text = spans.text();
                    pm = Misc.substring(text, "同行业排名第", "位")
                    print("\n--> " + spans.text())
                    print("\n---> " + pm)
                    if (pm != null && pm.length > 0) {
                        info[name + "-排名"] = pm
                    }
                }
            })

//	    info.fenlei = text(doc, '#profile .mt10 td :contains(分类：)')
//	    info.shiyinglvdongtai = text(doc, '#profile .mt10 td :contains(市盈率\(动态\)：)')
//	    info.shiyinglvjingtai = text(doc, '#profile .mt10 td :contains(市盈率\(静态\)：)')
//	    info.meigushouyi = text(doc, '#profile .mt10 td :contains(每股收益：)')
//	    info.zuixinjiejin = text(doc, '#profile .mt10 td :contains(最新解禁：)')

//	    printJson(info);
            info.code = urlInfo.code;
            newestInfo.data.push(info)
        } catch (exc) {
            exception(exc)
            return false
        }
        return true;
    }

    conf.crawl();

}
crawlNewest();

saveVar('10jqka.basic', newestInfo)