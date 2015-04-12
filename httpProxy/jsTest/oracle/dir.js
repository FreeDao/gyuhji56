var lines = 
'ls -lsa'.split(" ");
for (var line in lines) {
  print("|> " + line);
} 