loadx('fsql/initFilters')
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
	    var flt = filterFactoryMap.get(queryName, tokenType, queryValue)
	    if(flt){
		return flt
	    }else{
		throw  "no filter match! for '" +  
		queryName  + " "+tokenType + " "+queryValue + "'"

	    }
	}
    }

    this.collectQuery = function(headNode) {
	with(this.imported){
	    var ret = [];
	    var nodeToMake = headNode;
	    while (nodeToMake != null) {
		var sub;
		if (nodeToMake.isCompond()) {
		    var nodeToMake2 = nodeToMake.thisCond;
		    sub = this.collectQuery(nodeToMake2);
		} else {
		    sub = this.doSimpleNode( nodeToMake);
		}

		ret.push(
			{
			    "op" : nodeToMake.getConjunctType().toString() ,
			    "filter" : sub,
			    "_is_fsql_bool":true
			}
		)
		nodeToMake = nodeToMake.nextCondition;
	    }
	    return new FsqlCmbFilter(ret);
	}
    }
    this.toFilter = function(){
	var cond = this.ipt.toConditionLink();
	return this.collectQuery(cond);
    }
}

var fsql2Filter = function(sql) {
    var _fsql = new Fsql(sql);
    return _fsql.toFilter();
}