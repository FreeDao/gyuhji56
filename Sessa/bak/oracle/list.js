var list = java.util.Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
var odd = list.stream().filter(function(i) {
	return i % 2 == 0;
});
odd.forEach(function(i) {
	print(">>> " + i);
}); 

var even = list.stream().filter(
		function(i) {
			return i % 2 == 1;
		}); 
var xx = even.getClass().getMethods()

for(var m in xx){
	print(xx[m]);
}
print(java.util.Arrays.asList(even.toArray()));