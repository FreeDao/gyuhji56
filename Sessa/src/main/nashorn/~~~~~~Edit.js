//x = 'daf'
//    switch(x){
//    case 'aa':
//	break;
//    case 'daf':
//    print('aaaaaaaaaaa');
//    break;
//    default:
//	print('not found');
//    }

loadVarExt('allStock')

allStock.data.forEach(function(stock){
    for(var pos in stock){
	print(pos);
    }
})