companiesInfo = {
    crlDate: now(),
    data: []
}

var crawlCompany = function () {
    loadVarExt('allStock');

    var urls = [];

    allStock.data.forEach(function (stock) {
        urls.push({
            "url": "http://basic.10jqka.com.cn/" + stock.getShortCode() + "/company.html",
            "code": stock.getCode()
        });
    });
    print("urls.length:" + urls.length)
    var conf = new CrawlConfig();
    conf.urls = urls;
    conf.savePath = conf.path('10jka.company')
    conf.callback = function (urlInfo) {
        try {
            doc = Jsoup.parse(urlInfo.html)

            var items = doc.select("#publish td");
            var info = {}
            items.forEach(function (e) {
                var txt = e.text().trim()

                var pos = txt.indexOf('：');
                var key = txt.substring(0, pos).trim();
                var value = txt.substring(pos + 1).trim()
                //print(key)
                //print("\t" +value)
                if(pos != -1){
                    var vNew = filterKV(key, value);
                    if(vNew){
                        info[key] = vNew;
                    }
                }

            });

            info.code = urlInfo.code;
            companiesInfo.data.push(info)
            printJson(info)
        } catch (exc) {
            exception(exc)
            return false
        }
        return true;
    }

    conf.crawl();

}
var trimDay = function(txt){
    var txtNew = txt.replace('-', '').replace('-', '');
    if(txtNew.length == 8){
        return Number(txtNew);
    }
}
var accFields = {
    "成立日期":trimDay,
    "上市日期":trimDay
}
var filterKV = function(key, value){
    var fc = accFields[key];
    if(fc) {
        return fc(value)
    }
}
crawlCompany();

saveVar('10jqka.company', companiesInfo, true)