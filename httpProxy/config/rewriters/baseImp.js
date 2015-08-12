load("nashorn:mozilla_compat.js")
importPackage(
	java.lang, java.io,java.nio,
	java.util,
	java.net,
	java.util.concurrent,
	java.sql,
	java.nio.charset,
	java.text,
	java.util.concurrent.atomic,
	java.util.regex,
	java.util.Map);

importPackage(com.bmtech,
	com.bmtech.utils,
	com.bmtech.utils.bmfs,
	com.bmtech.utils.bmfs.tool,
	com.bmtech.utils.bmfs.util,
	com.bmtech.utils.c2j,
	com.bmtech.utils.c2j.cTypes,
	com.bmtech.utils.charDet,
	com.bmtech.utils.counter,
	com.bmtech.utils.http,
	com.bmtech.utils.http.fileDump,
	com.bmtech.utils.http.itrCrawl,
	com.bmtech.utils.io,
	com.bmtech.utils.io.diskMerge,
	com.bmtech.utils.log,
	com.bmtech.utils.rds,
	com.bmtech.utils.rds.convert,
	com.bmtech.utils.ruledSegment,
	com.bmtech.utils.ruledSegment.affix,
	com.bmtech.utils.security,
	com.bmtech.utils.segment,
	com.bmtech.utils.superTrimer,
	com.bmtech.utils.systemWatcher,
	com.bmtech.utils.systemWatcher.innerAction,
	com.bmtech.utils.tcp);

var dftCharset = Charsets.UTF8_CS;

var error = function(x){
	new java.lang.Exception(x).printStackTrace();
}

var getFileStr = function(file){
	var bytes = FileGet.getBytes(file);
	return new java.lang.String(bytes, dftCharset);
}

var members = function(vari){
	for(var k in vari){
		print(k + ":" + vari[k])
	}
}


var Rewriter = function(name){
	this.name = name;
	this.log = new LogHelper(name)
	this.filtersDir = new File("config/rewriters/" + name);
	this.filters = [];

	this.filter = function(url, httpHeaders, html){
		var log = this.log;
		log.info(" this.filters is " +  this.filters);
		this.filters.forEach(function(flt){
			log.info("flt: "+ flt)
			try{
				var isMatch = flt.match(url, httpHeaders, html);
			}catch(e){
				log.error(e, "when match: %s", e);
			}
			log.info("isMatch:%s", isMatch)

			if(isMatch){
				try{
					var htmlNew = flt.rewrite(url, httpHeaders, html);
					if(htmlNew){
						html = htmlNew;
					}
				}catch(e){
					log.error(e, "when rewrite: %s", e);
				}
			}
		});

		return html;
	};
	this.log.info("filtersDir: %s" , this.filtersDir)
	this.run = function(){
		this.log.info("Rewriter  %s is watcher running", name)
		var map = new HashMap();
		

		while(true){
			try{
				var files = this.filtersDir.listFiles();
				var lst = new ArrayList();
				var chg = false;
				this.log.debug("map info %s", map)
				for(var index in files){
					var e = files[index];
					var old = map.get(e);

					if(!old || e.lastModified() != old.lastModified){
						if(!old){
							this.log.warn("new add script %s", e)
						}else{
							this.log.warn("modified script %s", e)
						}
						chg = true
						var js = getFileStr(e)
					}else{
						this.log.debug("not modified file " +e)
						var js = old.js
					}
					lst.add({
						"lastModified" :e.lastModified(),
						"js":js,
						"file":e
					})
				};

			
				if(chg){
					var tmpFilters = [];
					map.clear();
					lst.forEach(function(e){
						try{
							eval("var __tmp=" + e.js)
							if(__tmp.match || __tmp.rewrite){
								tmpFilters.push(__tmp)
							}else{
								this.log.error("error: has no match or rewrite for filter %s", e.file)
							}
							map.put(e.file, e)
						}catch(exx){
							error("when load js from file : " + e.file + ", e = " + exx)
							
						}
					})
					
					this.filters=tmpFilters;
					this.log.info("tmpFilters set to this.filters , its %s : %s, len %s", tmpFilters, this.filters, this.filters[0])
				}

			}catch(exc){
				error("watcher exception:" + exc)
			}finally{
				
				try{
					Misc.sleep(3000)
				}catch(e){
					error(e);
				}
			}
		}
	};
}
