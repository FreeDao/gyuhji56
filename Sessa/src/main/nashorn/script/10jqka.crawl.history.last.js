updateLastDay = function () {

    var stockYear = []

    var urls = [];
    for (var index in allStock.data) {
        var stock = allStock.data[index]
        var stockFrom = allStock.stockFrom(stock.code);
        var prf = ""
        if (stockFrom == '1') {
            prf = "sh_"
        } else {
            prf = "sz_"
        }
        var url = 'http://d.10jqka.com.cn/v2/line/' + prf + allStock.trimCode(stock.code) + '/01/last.js'
        urls.push({"url": url, "code": stock.code});
    }
    var conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('10jka.history.last')

    conf.callback = function (urlInfo, datax) {

        var data = Misc.substring(datax, "(", ")")//js.substring(pos +1,js.length -1)//js.replaceAll(".+_last", "eval")
        data = JSON.parse(data);
        if (!data) {
            print("no data!" + data);
            return true;
        }
        print(data.name + " start at " + data.start);
        stockYear.push({
            code: urlInfo.code,
            year: data.start
        })

//	members(data)
        var days = data.data.split(";");
        var dayInThisYear = [];
        days.forEach(function (e) {
            var token = e.split(",");
            var stockDay = new StockDay(token);
            dayInThisYear.push(stockDay)
        })
        std.pause("not saved! use history.day")
        return true;
    }
    print("load urls " + urls.length)
    conf.crawl();
    saveStockYear('stock.Year', stockYear, true)
    // return [stockYear,dayInThisYear]
}

print("10jqka.crawl.history.last loaded");

