@call %bmTestDriver%
@call cd  %bmTestPath%\Sessa
set jjsEnv=config/nashorn/jjsEnv.js
echo load env: load("%jjsEnv%");
@call jjs-cp.bat
@java -Dfile.encoding=gbk -cp %_CP%  com.bmtech.util.nashorn.JjsEmulate "C:\Program Files\Java\jre1.8.0_45\bin\jjs.exe -Dfile.encoding=gbk -cp %_CP%"
#@jjs -cp %_CP% 

@call jjs-shell.bat