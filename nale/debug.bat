@call %bmTestDriver%
@call cd  %bmTestPath%\nale
java -cp .;target/classes;../bmUtils/target/classes com.bmtech.nale.tools.cmdLine.CmdLine


cmd