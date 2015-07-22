var FilterTemplate = function(op, opValue){
    this.op = op;
    this.opValue = opValue;
    this.acceptOps= null;

//    this.getElementValue = function(stock){
//	throw new Exception("should override FilterTemplate.getElementValue(stock)")
//    }
    this.checkAcceptOp = function(op){
	var acc = false;
	this.acceptOps.forEach(function(x){
	    if(x == op){
		acc = true;
	    }
	})
	if(!acc){
	    throw new Exception("un-support op " +  this.tmplt.op)
	}
    }
}
var EnumFilter = function(op, opValue){
    this.tmplt = new FilterTemplate(op, opValue);
    this.tmplt.acceptOps = ['=' ,  '==' , 'in', "!="]
    this.tmplt.checkAcceptOp(op);
    this.contains = function(stock){
	var fieldValue = this.getElementValue(stock);
	if(fieldValue instanceof Array){
	    return fieldValue.indexOf(this.tmplt.opValue) != -1
	}else{
	    return fieldValue == this.tmplt.opValue
	}
    }
    this.filter = function(stock){
	var has  = this.contains(stock);
	if(this.tmplt.op == '!='){
	    return !has
	}else{
	    return has;
	}
    }
}
var NumberFilter = function(op, opValue){
    this.tmplt = new FilterTemplate(op, opValue);
    this.tmplt.acceptOps = ['>' ,  '<' , '=', "==", '>=', "<=", "!="]
    this.tmplt.checkAcceptOp(op);

    this.filter = function(stock){
	fieldValue = this.getElementValue(stock);
	
	if(isNaN(fieldValue)){
	    return false;
	}
	//print(fieldValue +" "+  this.tmplt.opValue)
	switch(op){
	case '>':
	    return fieldValue >  this.tmplt.opValue
	case '<':
	    return fieldValue <  this.tmplt.opValue
	case  '=':
	case '==':
	    return fieldValue ==  this.tmplt.opValue
	case '>=':
	    return fieldValue >=   this.tmplt.opValue
	case '<=':
	    return fieldValue <=   this.tmplt.opValue
	case '!=':
	    return fieldValue !=   this.tmplt.opValue
	default:
	    throw new Exception("unkown op for NumberFilter " + this.op)
	}
    }
}
