var makeOpenTmpDirName= function(dirName){
    var base = new File("./mdirTmpHome");
    base.mkdirs();
    var file = new File(base, dirName);
    return file.getPath();
}
var openTmpMdir = function(dirName){
    return openMdir(makeOpenTmpDirName(dirName))
}
var openTmpMdir4Write = function(dirName){
    return openMdir4Write(makeOpenTmpDirName(dirName))
}
var openMdir = function(dirName){
    var file = new File(dirName);
    return MDir.open(file);
}

var openMdir4Write = function(dirName){
    var file = new File(dirName);
    return MDir.open4Write(file);
}
