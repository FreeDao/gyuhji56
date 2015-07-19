@call %bmTestDriver%
@call cd  %bmTestPath%\Sessa
@set jjsEnv=src/main/nashorn/jjsEnv.js

@call jjs-cp.bat


@call D:\javas\jsdoc-master\jsdoc --verbose -r  -d jsDocs   R:\github\gyuhji56\Sessa\src\main\nashorn\

@pause

