
var rewriter = new Rewriter("responseRewriter");
new Thread(function(){
	rewriter.run()
}).start()
