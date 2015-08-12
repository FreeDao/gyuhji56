
var rewriter = new Rewriter("requestRewriter");
new Thread(function(){
	rewriter.run()
}).start()
