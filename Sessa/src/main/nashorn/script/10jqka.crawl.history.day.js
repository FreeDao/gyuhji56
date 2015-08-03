var toHistoryUrl = function (stock, year) {
    var stockFrom = allStock.stockFrom(stock.code);
    var prf;
    if (stockFrom == '1') {
        prf = "sh_"
    } else {
        prf = "sz_"
    }
    return 'http://d.10jqka.com.cn/v2/line/' + prf + allStock.trimCode(stock.code) + '/01/' + year + '.js'

}

var parseHistoryData = function (html, code) {

    var txt = Misc.substring(html, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
    var list = []
    if (txt) {
        var data = JSON.parse(txt);
        if (!data.data)return list;
        var days = data.data.split(";");
        var cnt = 0

        days.forEach(function (day) {
            cnt++
            var tokens = day.split(",");
            var oneDay = new StockDay(tokens);
            oneDay.code = code;
            list.push(oneDay);
        });
        return list;
    } else {
        return []
    }
}


downloadThisYear = function () {
    var urls = [];
    var yearNum = nowDay('yyyy', now());
    allStock.data.forEach(function (stock) {

        var url = toHistoryUrl(stock, yearNum);
        urls.push({'url': url, "stock": stock})
    });

    downloadYear(yearNum, urls)
}
downloadYear = function (yearNum, urls) {
    loadVarExt('allStock')
    print("start crawl")
    var conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('history.this.year')
    var updated = 0;
    var sim = 0;
    var misMatch = 0;
    conf.callback = function (urlInfo) {
        try {
            var yearList = parseHistoryData(urlInfo.html)
            var oldYear = urlInfo.stock.loadYear(yearNum)

            if (!oldYear || oldYear.length < yearList.length) {
                jpr("old size %s, new size %s", oldYear ? oldYear.length : "0", yearList.length);
                urlInfo.stock.saveYearData(yearNum, yearList, true)
                updated++
            } else if (oldYear.length == yearList.length) {
                jpr("no change for %s, has item %s", urlInfo.stock.code, oldYear.length);
                sim++
            } else {
                misMatch++
                var errInfo = "?????????ERROR! data size mismatch " + oldYear.length + " > " + yearList.length + ", for " + JSON.stringify(urlInfo.stock);
                print(errInfo)
                conf.addWarn(errInfo)
                return true
            }
        } catch (exc) {
            exception(exc)
            if (!std.confirm("continue")) {
                return false;
            }
        }
        return true;
    }
    conf.crawl()
    jpr("updated = %s,    sim = %s    misMatch = %s", updated, sim, misMatch)
}
print("loaded");