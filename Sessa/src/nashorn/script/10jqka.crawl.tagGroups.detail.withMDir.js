if(typeof(tagGroup) == "undefined" || !tagGroup){
    tagGroup = loadVar('10jqka.tagGroup')
  
}
for(var index in tagGroup.groups){
    var group = tagGroup.groups[index]
    jpr("load group %s",index )

    for(var index2 in group.tags){
	var tag = group.tags[index2]
	jpr("load tag %s", index2 )
	tag.codes = [];
	if(typeof(tag.success) == "undefined"){
	    tag.success = false;
	}
	if(tag.success){
	    continue;
	}
	jpr("crawling %s", tag.subUrl )
	try{
	    print(tag.subUrl)
	    var tagHtml = crawl(tag.subUrl)
	    
	    var doc = Jsoup.parse(tagHtml)	
	    var links = doc.select('.c_wrap .c_content a');
	    for(var index3 in links){
		
		var gu = links[index3].attr('href');
		code = gu.replaceAll("/", "").trim()
		print(tag.name + ":" + code);
		tag.codes.push(code);
	    }
	    tag.success = true;
	}catch(exp){
	    print(exp)
	    break
//	    if(pause()){
//		break;
//	    }
	}
    }
}