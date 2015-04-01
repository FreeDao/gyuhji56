@r:
@cd R:\github\commentparse\bmt_base

call ant -buildfile build_infr.xml

cd build\dist
copy /y bmtHtmlps.jar C:\Users\Administrator\.m2\repository\bmtech\bmtHtmlps\1.0\bmtHtmlps-1.0.jar
copy /y bmtUtil.jar C:\Users\Administrator\.m2\repository\bmtech\bmtUtil\1.0\bmtUtil-1.0.jar

pause
