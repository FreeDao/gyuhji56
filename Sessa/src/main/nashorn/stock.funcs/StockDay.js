/**
 * @class StockDay
 *
 * @param vars 一个数组
 */
var StockDay = function (vars) {
    /**day in yyyyMMdd**/
    this.day = Number(vars[0]);
    /**开盘价**/
    this.open = Number(vars[1]);
    /**最高价**/
    this.high = Number(vars[2]);
    /**最低价**/
    this.low = Number(vars[3]);
    /**收盘价**/
    this.close = Number(vars[4]);
    /**成交量**/
    this.amount = Number(vars[5]);
    /**成交金额**/
    this.volumn = Number(vars[6]);
    /**换手率**/
    this.turnover = Number(vars[7]);

    if (isNaN(this.turnover) || !this.turnover) {
        this.turnover = 0.00001
    }
}