var makeOpenTmpDirName= function(dirName){
    var base = new File("./mdirTmpHome");
    base.mkdirs();
    var file = new File(base, dirName);
    return file.getPath();
}
var openTmpMDir = function(dirName){
    return openTmpMdir(dirName);
}
var openTmpMdir = function(dirName){
    return openMdir(makeOpenTmpDirName(dirName))
}
var openTmpMDir4Write = function(dirName){
    return openTmpMDir4Write();
}
var openTmpMdir4Write = function(dirName){
    return openMdir4Write(makeOpenTmpDirName(dirName))
}

var openMDir = function(dirName){
    return openMdir(dirName)
}
var openMdir = function(dirName){
    var file = new File(dirName);
    return MDir.open(file);
}

var openMDir4Write = function(dirName){
    return openMdir4Write(dirName)
}
var openMdir4Write = function(dirName){
    var file = new File(dirName);
    return MDir.open4Write(file);
}
