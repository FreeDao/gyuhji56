var WekaRowTypeProto = function (rowTypeName) {
    this.rowTypeName = rowTypeName;
}
var wekaDataRow = {
    relation: new WekaRowTypeProto('relation'),
    attribute: new WekaRowTypeProto('attribute'),
    data: new WekaRowTypeProto('data')
}
var WekaAttrType = function (typeName) {
    this.typeName = typeName;
    this.toLine = function (attrName, enumsValues) {
        var sb = new StringBuilder();
        sb.append('@');
        sb.append(wekaDataRow.attribute.rowTypeName)
        sb.append(' ');
        sb.append(attrName)
        sb.append(' ');
        if (enumsValues instanceof Array) {
            sb.append('{')
            enumsValues.forEach(function (e) {
                sb.append(e);
                sb.append(', ')
            })
            sb.append('}')
        } else {
            sb.append(this.typeName);
        }

        return sb.toString();
    }
}

var wekaAttrTypes = {
    numberic: new WekaAttrType('numeric'),
    enums: new WekaAttrType('enums'),
    stri: new WekaAttrType('string')
}
var AttrDefine = function(attrName, wekaAttrType){
    this.attrName = attrName;
    this.wekaAttrType = wekaAttrType;

}
var FieldAttrDefine = function(fieldName, isNumberic, attrName){
    this.fieldName = fieldName;
    var wekaAttrType = isNumberic ? wekaAttrTypes.numberic : wekaAttrTypes.enums

    this.attrDefine = new AttrDefine(attrName ? attrName : fieldName, wekaAttrType);
    this.enumValues = isNumberic ? null:[];

    this.extractValue = function (stock) {
        var value = stock[this.fieldName];

        if(this.attrDefine.wekaAttrType == wekaAttrTypes.enums){
            this.enumValues.push(value);
        }
        return value;
    };
}
var stockFieldsAttrDefines = {
    "净利润":{
        isNumberic:true
    },
    "净利润-排名":{
        isNumberic:true
    },
    "净资产收益率":{
        isNumberic:true
    },
    "净资产收益率-排名":{
        isNumberic:true
    },
    "市净率":{
        isNumberic:true
    },
    "市盈率(动态)":{
        isNumberic:true
    },
    "市盈率(静态)":{
        isNumberic:true
    },
    "总股本":{
        isNumberic:true
    },

    "占总股本比例":{
        isNumberic:true
    },
    "最新解禁":{
        isNumberic:true
    },
    "解禁数量":{
        isNumberic:true
    },
    "每股净资产":{
        isNumberic:true
    },
    "每股净资产-排名":{
        isNumberic:true
    },
    "每股收益":{
        isNumberic:true
    },
    "每股收益-排名":{
        isNumberic:true
    },
    "每股现金流":{
        isNumberic:true
    },
    "每股现金流-排名":{
        isNumberic:true
    },
    "流通A股":{
        isNumberic:true
    },
    "营业收入":{
        isNumberic:true
    },
    "营业收入-排名":{
        isNumberic:true
    },
    "分类":{
        isNumberic:false
    },
    getFieldAttrDefine:function(name){
        var def = this[name];
        if(!def){
            return null;
        }
        return new FieldAttrDefine(name, def.isNumberic, name);
    }
}

