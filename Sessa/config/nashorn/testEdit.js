

var SeTag = function(tagName){
		this.tagName = tagName;
		this.values = [];
}

SeTag.prototype.push = function(tagValue){
	this.values.push(tagValue);
};
SeTag.push = function(tagValue){
	this.values.push(tagValue + ".my");
};



