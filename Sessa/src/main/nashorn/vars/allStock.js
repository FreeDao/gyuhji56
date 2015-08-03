var allStock = {
    data: [],
    codeMap: new HashMap(),
    codeTrimedMap: new HashMap(),
    nameMap: new HashMap(),
    trimCode: function (code) {
        code = codeFmt(code);
        if (code.length > 6) {
            code = code.substring(0, code.length - 1);
        }
        return code;
    },
    stockFrom: function (code) {
        code = code + "";
        if (code.length != 7) {
            print("!!!!!!!!!!!1errorCode " + code);
            std.pause();
        }
        return code.substring(6);
    },

    getByFullCode: function (code) {
        code = codeFmt(code);
        return this.codeMap.get(code);
    },
    getByName: function (name) {
        return this.nameMap.get(name)
    },
    getByTrimedCode: function (code) {
        code = codeFmt(code);
        code = this.trimCode(code);
        return this.codeTrimedMap.get(code);
    },
    getByShortCode: function (code) {
        code = codeFmt(code);
        return this.getByTrimedCode(code);
    },
    getByCode: function (code) {
        code = codeFmt(code);
        if (code.length == 6) {
            return this.getByShortCode(code);
        } else {
            return this.getByFullCode(code);
        }

    }
}
var codeFmt = function (x) {
    x = x + "";
    if (x.length < 6) {
        x = Integer.toOctalString(x)
        x = java.lang.String.format("%06d", Integer.parseInt(x))
        if (!x.startsWith("00")) {
            x = "0" + x
        }
    }
    return x;
}


var loadAllStock = function () {
    var datas = loadVar('allStock');

    for (var x in datas) {
        var stkInfo = datas[x];
        var stock = new Stock(stkInfo);
        allStock.data.push(stock);
        allStock.codeMap.put(stock.getCode(), stock);
        allStock.codeTrimedMap.put(stock.getShortCode(), stock);
        allStock.nameMap.put(stock.name, stock);
    }
    var stkCompany = loadVar('10jqka.company')
    stkCompany.data.forEach(function(e){
        var stk = allStock.getByCode(e.code);
        if(stk){
            for(var x in e){
                stk[x] = e[x];
            }
        }
    })
}


loadAllStock();

