loadx('stock.funcs/StockDay')
loadx('stock.funcs/YearInfo')
loadx('stock.funcs/DayCursor')

/**
 * stock basic object
 */
var Stock = function (stkInfo) {
    /**股票代码**/
    this.code = stkInfo.code;
    /**股票名称**/
    this.name = stkInfo.name;

    this.yearMap = {};
    /**
     * 获得股票代码
     */
    this.getCode = function () {
        return this.code;
    };

    /**
     * 获得股票所在市场
     * @return {string}1:sh; 2:sz
     */
    this.getFrom = function () {
        var ret = this.code.substring(6);
        if (ret.length != 1) {
            throw "code length error! code = " + this.code;
        }
        return ret;
    }
    /**
     * 获得股票简短代码
     */
    this.getShortCode = function () {
        return allStock.trimCode(this.code);
    }

    this.loadYear = function (year) {
        var yearData = this.yearMap[year]
        if (!yearData) {
            var dir = new File(stockDayDir, this.getCode() + "/");
            if (dir.exists()) {
                yearData = loadVar(year, dir);//FIXME should inject StockDay's functions
                this.yearMap[year] = yearData
            } else {
                return null;
            }
        }
        return yearData;
    }
    this.position = function (intDay) {
        var intYear = Math.floor(intDay / 10000);
        while (true) {
            var yearArr = this.loadYear(intYear);
            if (!yearArr) {
                return false;
            }
            var pos = this.positionInYear(yearArr, intDay);
            printJson(pos)
            if (pos.type == -1) {
                intYear++;
            } else {
                return {
                    year: intYear,
                    yearArr: yearArr,
                    index: pos.index
                }
            }
        }
    }
    this.positionInYear = function (yearArr, intDay) {
        for (var idx = 0; idx < yearArr.length; idx++) {
            if (yearArr[idx].day == intDay) {
                return {
                    type: 0,
                    index: idx
                }
            } else if (yearArr[idx].day > intDay) {
                return {
                    type: 1,
                    index: idx
                }
            }
        }
        return {
            type: -1,
            index: yearArr.length
        }
    }
    this.dayIntFormat = function (intDayStr) {
        var intDay = Number(intDayStr);
        if (intDay <= nowDay('yyyy') && intDay >= 1994) {
            intDay = intDay * 10000 + 101
        } else if (intDay <= nowDay('yyyyMM') && intDay >= 199401) {
            intDay = intDay * 100 + 1
        } else {
            if (intDay <= nowDay('yyyyMMdd') && intDay >= 19940101) {
                //ok
            } else {
                throw new Exception("invalid cursor '" + intDayStr + "'(formated as " + intDay + ")")
            }
        }
        return intDay;
    }
    /**
     * 以 @param dayFrom 开始的位置（cursor 开始位置 >=dayFrom）
     * @param dayFrom {int} 开始时间
     * @param dayTo {number} 结束时间
     * @return {DayCursor} 以不小于 intDay 开始的迭代器 DayCursor， 如果找不到，则返回false
     */
    this.getDayCursor = function (dayFrom, dayTo) {
        var intDay = this.dayIntFormat(dayFrom);

        var dayToFmt = dayTo ? dayTo : nowDay("yyyyMMdd");
        var intDayTo = this.dayIntFormat(dayToFmt);

        var posit = this.position(intDay)
        if (!posit) {
            return null;
        }
        var ret = new DayCursor(this, intDay, intDayTo);

        ret.yearInfo =
            new YearInfo(posit.year, posit.yearArr, posit.index);
        return ret;
    }

    /***
     * 获得从from 到to期间的交易数据StockDay
     * @param from {number} 开始时间
     * @param to {number} 结束时间
     * @return {[StockDay]} 交易数据StockDay的数组
     *
     */
    this.loadDays = function (from, to) {
        from = from + "";
        to = to + ""
        var fromYear = from.substring(0, 4)
        var toYear = to.substring(0, 4);
        var datas = []
        for (var x = fromYear; x <= toYear; x++) {
            var yearData = this.loadYear(x);
            if (yearData) {
                yearData.forEach(function (e) {
                    datas.push(e)
                })
            }
        }

        var ret = []
        for (x = 0; x < datas.length; x++) {
            if (datas[x].day >= from && datas[x].day <= to) {
                ret.push(datas[x]);
            }
        }
        return ret;
    }
}

var loadAllStock = function () {
    loadVarExt('allStock')
}
