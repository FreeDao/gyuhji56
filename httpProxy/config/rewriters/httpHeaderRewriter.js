
var rewriter = new Rewriter("httpHeaderRewriter");
new Thread(function(){
	rewriter.run()
}).start()
