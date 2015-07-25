/**
 * vars 的 根目录。所有内置变量存放于此目录下
 */
baseVarDir = new File(sessaPath("vars/"));
baseVarDir.mkdirs();

/**
 * script 的 根目录。所有内置js脚本存放于此目录
 * @variable
 */
baseScriptDir = new File(sessaPath("script/"));
baseScriptDir.mkdirs();

/**
 * 加载（执行）fileName指定的script目录下的js文件，具体加载的文件为: script/fileName.js
 * @param fileName 要加载的文件名（相对应 baseScriptDir目录， 自动添加后缀.js）
 */
var loadScript = function (fileName) {
    var file = new File(baseScriptDir, fileName + ".js");
    if (!file.exists()) {
        print("file not exits:" + file);
        return;
    }
    load(file)
}
/**
 * 保存对象到vars目录
 * @param fileName 要保存到文件
 * @param jsVar js变量
 * @param forcedSave {boolean} 默认false, 如果此值为false，且对应文件已经存在，
 * 将通过console提示是否覆盖
 */
var saveVar = function (fileName, jsVar, forcedSave) {

    var file = new File(baseVarDir, fileName + ".jvar");
    print("save to " + file);
    if (file.exists()) {
        if (typeof forcedSave != "undefined" && forcedSave) {
            print("override " + file);
        } else {
            if (!std.confirm("already exits, over write?")) {
                return;
            }
        }
    }
    print("saving to " + file)
    save(file, JSON.stringify(jsVar))
}

/**
 * 加载varName所指代的变量的文件名。
 * 加载过程为：先将文件载入内存，然后将通过 JSON.parse解析对应json文件，获得内存对象<br>
 * 注：本方法仅返回内存对象，不负责声明对象名
 * @param varName {string} 要加载的变量文件名
 * @param fromDir {File} 从指定文件夹加载
 */
var loadVar = function (varName, fromDir) {
    if (!fromDir) {
        fromDir = baseVarDir
    }
    var file = new File(fromDir, varName + ".jvar");
    if (!file.exists()) {
        print("file not exits:" + file);
        return;
    }
    print("loading " + file)
    var txt = FileGet.getStr(file);
    return JSON.parse(txt);
}

/**
 * 加载$hone/vars 目录下 varName变量。与 loadVar不同的是，此方法寻找对应变量的同名js,
 * 如 allStock，本方法会寻求加载 vars/allStock.js,而非vars/allStock.jvar<br>
 * 本方法仅执行对应的js，变量的加载过程，通过执行对应的同名js实现
 * @para varName 要加载的var的文件名
 * @return 本方法不反悔任何值
 */
var loadVarExt = function (varName) {
    var file = new File(baseVarDir, varName + ".js");
    if (!file.exists()) {
        print("file not exits:" + file);
        return;
    }
    return load(file);
}

/**
 * 将字符串保存到指定文件
 * @param file {File} 要保存到文件
 * @param txt{string}要保存在字符串
 */
var save = function (file, txt) {
    var f = new File(file.toString());
    f.getParentFile().mkdirs();
    var out = new FileOutputStream(f)
    out.write(txt.getBytes());
    out.close();
}

/**
 * 列出运行环境下，所有非 sessa环境的变量（运行脚本自定义的变量）
 */

var localVars = function () {
    print("/***local****/");
    for (var e in this) {
        if (globalVar.indexOf(e) == -1) {
            var type = (typeof eval("this." + e));
            print(" " + type + "\t" + e);
        }
    }
}
/**
 * 列出运行环境下，所有Sessa 环境使用的变量
 */
var globalVars = function () {
    print("/* global variables */");
    globalVar.forEach(function (e) {
        var type = (typeof eval("this." + e))
        print(" " + type + "\t" + e);
    })
}
/**
 * 列出运行环境下所有已声明的变量
 */
var vars = function () {
    globalVars();
    localVars();
}
/**
 * 去掉文件后缀
 * @param fileName {string}
 * @return 返回去掉了最后 '.xxx'的新字符串
 */
var fileNameWithNoSuffix = function (fileName) {
    var ret = [2];
    var pos = fileName.lastIndexOf(".");

    if (pos != -1) {
        ret[0] = fileName.substring(0, pos);
        ret[1] = fileName.substring(pos + 1);
    } else {
        ret = [fileName, ""];
    }
    return ret;
}
/**
 * @see {seJs}
 */
var seScript = function () {
    return seJs();
}
/**
 * 打印所有$home/script 目录下是所有脚本
 */
var seJs = function () {
    var files = baseScriptDir.listFiles();
    files = Arrays.asList(files);
    var compareFunc = function (o1, o2) {
        return o1.getName().compareTo(o2.getName())
    }
    Collections.sort(files, compareFunc)

    files.forEach(function (f) {
        name = f.getName()
        if (name.endsWith(".js")) {
            print(" " + name.substring(0, name.length - 3))
        } else {
            print("?? " + f)
        }
    })
}
/**
 * 打印所有$home/vars 目录下所有可加载的变量
 */
var seVars = function () {
    var files = baseVarDir.listFiles();
    files = Arrays.asList(files);

    var compareFunc = function (o1, o2) {
        var s1 = fileNameWithNoSuffix(o1.getName());
        var s2 = fileNameWithNoSuffix(o2.getName())

        ret = s1[0].compareTo(s2[0]);
        if (ret == 0) {
            return s2[1].compareTo(s1[1])
        }
        return ret;
    }

    Collections.sort(files, compareFunc)

    for (var fIndex in files) {
        var f = files[fIndex];

        var name = f.getName();
        if (name.endsWith(".js")) {
            print(" " + name.substring(0, name.length - 3) + " ------> loadVarExt()")
        } else if (name.endsWith(".jvar")) {
            print(" " + name.substring(0, name.length - 5));
        } else {
            if (f.isDirectory()) {
                print(" " + name + " ---Dir")
            } else {
                print("?? " + name)
            }
        }

    }
}

