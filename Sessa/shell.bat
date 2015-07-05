@call %bmTestDriver%
@call cd  %bmTestPath%\Sessa

@echo call shell exe : jjs config/nashorn/shellExe.js

@call jjs-cp.bat
@echo call jjs use : jjs -cp %%_CP%%
@set jjs=jjs -cp %_CP%
@echo or call: %%jjs%%
@echo load env : load('config/nashorn/jjsEnv.js')
@cmd

