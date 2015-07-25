var DayCursor = function (stock, begin, end) {
    this.stock = stock;
    this.yearInfo = null;
    this.begin = begin ? begin : 20100101;
    this.end = end ? end : Number(nowDay("yyyyMMdd", now()));

    this.check = function (stockDay) {
        if (stockDay.day > this.end || stockDay.day < this.begin) {
            return false;
        } else {
            return stockDay;
        }
    };
    /**获得当前日期数据**/
    this.get = function () {
        var stockDay = this._get();
        return this.check(stockDay);
    };

    /***
     * 获取下上个日期数据
     * @return 上个交易日股票数据，如果没有，则返回 false
     */
    this.backward = function () {
        var stockDay = this._pre();
        if (!stockDay) {
            return null;
        }
        if (this.check(stockDay)) {
            return stockDay;
        } else {
            this.forward();
            return null;
        }

    };

    /***
     * 获取下一个日期数据
     * @return 下个交易日股票数据，如果没有，则返回 false
     */
    this.forward = function () {
        var stockDay = this._next();
        if (!stockDay) {
            return null;
        }
        if (this.check(stockDay)) {
            return stockDay;
        } else {
            this.backward();
            return null;
        }
    };
    this._next = function () {
        if (this.yearInfo.hasNext()) {
            return this.yearInfo.next();
        } else {
            if (this.loadSlibYear(true)) {
                return this._next();
            } else {
                return false;
            }
        }
    };

    this._pre = function () {
        //jpr("year info " + this.yearInfo.year);
        if (this.yearInfo.hasPre()) {
            return this.yearInfo.pre();
        } else {
            //jpr("loading slib false");
            if (this.loadSlibYear(false)) {
                //jpr("calling this._pre")
                return this._pre();
            } else {
                return false;
            }
        }
    };

    this._get = function () {
        return this.yearInfo.get();
    };

    this.loadSlibYear = function (next) {
        var _year;
        if (next) {
            _year = this.yearInfo.year + 1;
        } else {
            _year = this.yearInfo.year - 1;
        }

        var _yearArray = this.stock.loadYear(_year);
        if (_yearArray) {
            this.yearInfo =
                new YearInfo(_year, _yearArray, next ? -1 : _yearArray.length);
            return true;
        } else {
            return false
        }
    }
};