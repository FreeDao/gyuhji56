
var iterator = new java.util.Iterator({

  hasNext: function() {
    return i < 10;
  },
  next: function() {
	  i++;
	  print("next i="+i)
    return i;
  }
});
i =1
print(iterator instanceof Java.type("java.util.Iterator"));
while (iterator.hasNext()) {
  print("-> " + iterator.next());
} 