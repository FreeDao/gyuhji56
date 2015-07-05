
loadx('jsoup')
var enterUrl=new URL('http://basic.10jqka.com.cn/');
var html = crawl(enterUrl);

var doc = Jsoup.parse(html)

var sections = doc.select('.c_wrap > div');
var tagGroups = {
	from:"10jqka",
	groups:[]
}
for(index in sections){
    var section = sections[index];
    var tagGroup = section.select('h2').text();
    var tags = section.select('.clearfix a');
    jpr("tagGroup %s", tagGroup);
    var tagGroupData = {
	    name : tagGroup,
	    tags : []
    }
    tagGroups.groups.push(tagGroupData);
    
    for(tid in tags){
	var tagSec = tags[tid];
	var href = tagSec.attr('href');
	
	var tagInfo={
	   
	    tcTitle : tagSec.attr('title'),
	    subUrl : new URL(enterUrl, href) + ""
	}
	jpr("name: %s, href %s, subUrl %s ", tagInfo.tcTitle, tagInfo.href, tagInfo.subUrl);
	tagGroupData.tags.push(tagInfo);
    }
}
saveVar('10jqka.tagGroup', tagGroups)

