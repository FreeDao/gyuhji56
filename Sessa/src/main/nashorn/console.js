/**
 * @class STD
 * standard in/out
 * use console to call
 */
var STD = function () {
    /**
     * 从console读取一行输入
     * @return {string}读取的输入
     */
    this.readLine = function (prmpt) {
        if (prmpt) {
            System.out.print(prmpt)
        }
        var bytes = new ByteArray(4096)
        var len = java.lang.System.in.read(bytes);
        return new java.lang.String(bytes, 0, len, sysCharset).trim()
    }

    /**
     * sessa 客户端要求输入确认信息
     * @param prompt 提示字符串，默认为"ok?"
     * @return {boolean}确认信息，n/no 返回false， y/yes 返回true，其他输入下会重复要求输入
     */
    this.confirm = function (prompt) {
        if (!prompt) {
            prompt = "ok?"
        }
        return Consoler.confirm(prompt);
    }
    /**
     * sessa 客户端上暂停输出
     * @param arg 暂停时输出的提示符， 默认为"press enter to continue"
     */
    this.pause = function (arg) {
        if (!arg) {
            arg = "press enter to continue"
        }
        Consoler.readString(arg)
    }

    /**
     * 打印字符串的 末尾
     * @param str 要打印的字符串
     * @param num 打印做少个字符，默认32
     */

    this.tail = function (str, num) {
        if (!top) {
            num = 32;
        }
        var strx = str + ""
        if (num >= strx.length) {
            num = strx.length;
        }
        print(strx.substring(str.length - num, num));
    }

    /**
     * 打印字符串的 头部
     * @param str 要打印的字符串
     * @param top 打印做少个字符，默认32
     */
    this.head = function (str, top) {
        if (!top) {
            top = 32;
        }
        var strx = str + "";
        if (top >= strx.length) {
            top = strx.length;
        }
        print(strx.substring(0, top));
    }
    this.print = function(arg){
        System.out.println(arg);
    }
}

var std = new STD();
var print = function (arg) {
    std.print(arg)
}
