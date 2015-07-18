@call %bmTestDriver%
@call cd  %bmTestPath%\Sessa
set jjsEnv=src/main/nashorn/jjsEnv.js
echo load env: load("%jjsEnv%");
@call jjs-cp.bat
@set sessaPathSetting=-DsessaPath=src/main/nashorn/
@java %sessaPathSetting% -Dfile.encoding=gbk -Xmx2g -cp %_CP%  com.bmtech.util.nashorn.JjsEmulate "C:\Program Files\Java\jre1.8.0_45\bin\jjs.exe %sessaPathSetting% -Dfile.encoding=gbk -cp %_CP%"


@call jjs-shell.bat