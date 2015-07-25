/**
 * 格式化字符串
 * @param args args[0] pattern, args[1...]参数
 * @returns 格式化后的字符串
 */
format = function (args) {
    if (args && args.length > 1) {
        var fmtArg = args.slice(1, args.length);

        return java.lang.String.format(String(args[0]), fmtArg)
    } else {
        if (args.length == 1) {
            return args[0];
        }
        return "";
    }
}

/**
 * 返回当前毫秒数
 * @return {number}返回毫秒数
 */
var now = function () {
    return System.currentTimeMillis();
}
/**
 * @param  time  毫秒数, 默认为当前时间
 * @return {number}格式数字日期 如 20150717
 */
var intDay = function (time) {
    return Number(nowDay("yyyyMMdd", time))
}
/**
 * @param fmt 格式，默认 yyyy-MM-dd
 * @param  time  毫秒数, 默认为当前时间
 * @return 时间字符串
 */
var nowDay = function (fmt, time) {
    if (!fmt) {
        fmt = "yyyy-MM-dd"
    }
    if (!time) {
        time = now()
    }
    return new SimpleDateFormat(fmt).format(time);
}
/**
 * @param time 时间毫秒数， 默认为当前时间
 * @return 返回 指定时间的“年分”
 */
var nowYear = function (time) {
    return nowDay("yyyy", time)
}
/**
 * @param time 时间毫秒数，默认为当前时间
 * @return 返回指定时间的月份
 */
var nowMonth = function (time) {
    return nowDay("MM", time)
}
/**
 * @param time 时间毫秒数，默认为当前时间
 * @return 返回指定时间的在当月中的日期，如19
 */
var nowDayOfMonth = function (time) {
    return nowDay("dd", time)
}
/**
 * @param time 时间毫秒数，默认为当前时间
 * @return 返回指定时间的小时(24小时制)，如19
 */
var nowHour = function (time) {
    return nowDay("HH", time);
}
/**
 * @param time 时间毫秒数，默认为当前时间
 * @return 返回指定时间的分钟数，如35
 */
var nowMinute = function (time) {
    return nowDay("mm", time);
}
/**
 * @param time 时间毫秒数，默认为当前时间
 * @return 返回指定时间的秒数，如 53
 */
var nowSecond = function (time) {
    return nowDay("ss", time);
}

/**
 * 通过指定的对象排序值函数排序
 * @param arr 要排序的数组
 * @param func 对arr中各个对象生成排序值的回调函数
 */
var sortWithCallback = function (arr, func) {

    Collections.sort(arr, function (o1, o2) {
        return Math.floor(func(o1) - func(o2) + 0.5);
    })
}


/**
 * 对数组顶部的 num 个元素 执行 func 函数
 * @param arr 数组对象
 * @param func 执行的回调函数
 * @param num 数字，指定要执行的个数，默认值为 arr.length
 */
var forEachTop = function (arr, func, num) {
    var index = 0;
    if (!num) {
        num = arr.length
    }
    for (; index < num && index < arr.length; index++) {
        func(arr[index]);
    }
}
/**
 * 对数组底部的 num 个元素 执行 func 函数
 * @param arr 数组对象
 * @param func 执行的回调函数
 * @param num 数字，指定要执行的个数，默认值为 arr.length
 */
var forEachBottom = function (arr, func, num) {
    var index = 0;
    for (; index < num; index++) {
        var pos = arr.length - 1 - index;
        if (pos < 0)
            break;
        func(arr[pos]);
    }
}


/**
 * @class ArrayMap
 * 一个<key,[]>数据结构。
 */

var ArrayMap = function () {
    this.dict = {};
    /**
     * 以key为键向ArrayMap中对应的array中插入元素element
     * @param key 键
     * @param element 要插入的元素
     */
    this.put = function (key, element) {
        dictArr = this.dict[key];
        if (!dictArr) {
            dictArr = [];
            this.dict[key] = dictArr;
        }
        dictArr.push(element);
    }
    /**
     * 以object 形式返回此对象
     * @return 返回\{key:array1, key2:array2\}形式的数据结果
     */
    this.toDict = function () {
        return this.dict;
    }
    /**
     * @param key 查询键
     * @return 返回查询数组
     */
    this.get = function (key) {
        return this.dict[key];
    }
    /**
     * 判断是否包含键 key
     * @param key 键
     * @return 如果存在key，返回true，否则返回false
     */
    this.hasKey = function (key) {
        return !!this.dict[key]
    }
    /**
     * 遍历所有元素
     * @param callback 对元素遍历时使用的回调函数，参数应接受 (key, array)参数
     * @return void
     */
    this.visit = function (callback) {
        for (var key in this.dict) {
            callback(key, this.dict[key])
        }
    };
    /**
     * @see visit
     */
    this.forEach = function (callback) {
        visit(callback)
    }
}


/**
 * 将对象转换为js数组<br>建议使用forEach方法代替遍历
 * @param arr java 数组
 * @return js 数组 {array} 数组
 *
 */
var asJsArray = function (arr) {
    var ret = []
    if (arr.length) {
        for (var p = 0; p < arr.length; p++) {
            ret.push(arr[p]);
        }
    } else {
        if (arr.iterator) {
            var itr = arr.iterator();
            while (itr.hasNext()) {
                ret.push(itr.next())
            }
        }
    }
    return ret;
}

/**
 * 打印异常的堆栈
 * @param e 异常信息
 */
var exception = function (e) {
    new java.lang.Exception(e).printStackTrace();
}
/**
 * 打印当前堆栈
 */
var stack = function () {
    new java.lang.Exception("").printStackTrace();
}
/**
 * 以format 为基础的print 函数
 * @example jpr("a=%s,b=%d", 'nnnn',99)
 */
var jpr = function () {
    var args = Array.prototype.slice.call(arguments);
    try {
        var fmted = format(args);
        print(fmted);
    } catch (e) {
        log.error(e, "for jpr")
        print("ERROR　FMT:" + JSON.stringify(args))
    }
    return "";
};

/**
 * 打印指定对象的成员。 sessa终端下快捷键 //m obj
 * @param obj 要打印成员的对象
 * @return 无返回值，只向终端输出
 */
var members = function (obj) {
    try {
        var lst = Arrays.asList(obj.getClass().getMethods())
        Collections.sort(lst, function (o1, o2) {
            return o1.getName().compareTo(o2.getName())
        })
        print(lst.size())
        lst.forEach(function (met) {
            print(met.getName() + "\t" + met.getReturnType())
        })
    } catch (exc) {
        log.print(exc);
        for (i in obj) {
            print(i)
        }
    }
}

/**
 * 将对象 arg 以 json格式输出.
 * <br>sessa客户端快捷方式为//j arg 或者//arg（如果arg不是其他提示符的话）
 *
 * @param arg 要输出为json的对象
 * @return 无返回值
 */
var printJson = function (arg) {
    var json = JSON.stringify(arg)
    print(json)
}

/**
 * 判断 varName所代表的字符串是否为已定义变量
 * @param varName 要检测的变量名
 * @return {boolean} 返回true 如果已经定义
 */
var isDefined = function (varName) {
    for (var definedName in this) {
        if (varName.equals(definedName)) {
            return true;
        }
    }
    return false;
}