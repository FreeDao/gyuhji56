var FsqlFilters=function(itemName, filterType, filterValue){

}
var FsqlFilterMap = function(){
    loadx('fsql/filterMap')
    this.lookup = function(filterName){
	filterName = filterName.trim().toLowerCase();
	var filterWrapper = filterWrapperMap.get(filterName)
	if(!filterWrapper){
	    throw new Exception("filter Name not found '" + filterName + "'"); 
	}
	return filterWrapper;
    }
}
fsqlFilterMap = new FsqlFilterMap();
/**
 * @class Fsql 
 */
var Fsql = function(sql){
    this.imported = new JavaImporter(com.bmtech.fsql.expression, com.bmtech.fsql.expression.ast)
    with(this.imported){
	this.sql = sql;
	this.ipt = new FsqlInterpreter(sql);
    }
    this.doSimpleNode = function(node) {
	with(this.imported){
	    var queryName = node.getStandardFieldName();
	    var tokenType = node.getRelation().type.toString();
	    var queryValue = node.getFieldValue();

	    return "(" + queryName + " " + tokenType + " " + queryValue + ")";
	}
    }

    this.makeItemFilter = function(itemName, filterType, filterValue){
	filterWrapper = fsqlFilterMap.lookup(itemName)
	return filterWrapper.toFilter(filterType, filterValue);
    }

    this.collectQuery = function(headNode) {
	with(this.imported){
	    var ret = "";
	    var nodeToMake = headNode;
	    while (nodeToMake != null) {
		var sub;
		if (nodeToMake.isCompond()) {
		    var nodeToMake2 = nodeToMake.thisCond;
		    sub = this.collectQuery(nodeToMake2);
		    sub = " {" + sub + "}";
		} else {
		    sub = this.doSimpleNode( nodeToMake);
		}

		ret = ret + " " + nodeToMake.getConjunctType() + " " + sub;
		nodeToMake = nodeToMake.nextCondition;
	    }
	    return ret;
	}
    }
    this.testOut = function(){
	var cond = this.ipt.toConditionLink();
	return this.collectQuery(cond);
    }
}

var testFsql = function() {
    var sql = "category !=4 or 电子节目单> zzza && 驻地=湖北电信2期 and c > 24.19 && (x>'zzzzx' && y like 'dfa')";
    fsql = new Fsql(sql);
    print(fsql.testOut());
}