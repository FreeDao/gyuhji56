var ObjectType = Java.type("java.lang.Object");
var Comparable = Java.type("java.lang.Comparable");
var Serializable = Java.type("java.io.Serializable");

var MyExtender = Java.extend(
		ObjectType, Comparable, Serializable);
var instance = new MyExtender({
	someInt: 0,
	compareTo: function(other) {
		var value = other["someInt"];
		if (value === undefined) {
			return 1;
		}
		if (this.someInt < value) {
			return -1;
		} else if (this.someInt == value) {
			return 0;
		} else {
			return 1;
		}
	}
});

print(instance instanceof Comparable);
print(instance instanceof Serializable);
print(instance.compareTo({ someInt: 10 }));
print(instance.compareTo({ someInt: 0 }));
print(instance.compareTo({ someInt: -10 })); 

var anotherInstance = new MyExtender({
	compareTo: function(other) {
		return -1;
	}
});

// Prints 'true'!
print(instance.getClass() === anotherInstance.getClass()); 