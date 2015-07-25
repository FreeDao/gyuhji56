var YearInfo = function(year, yearArray, pos){
    this.year = year;
    this.yearArray = yearArray;
    this.pos = pos? pos : 0;

    this.hasNext = function(){
	return this.pos + 1 < this.yearArray.length
    }
    this.hasPre = function(){
	return this.pos > 0;
    }
    this.next = function(){
	this.pos ++;
	return this.yearArray[this.pos];
    }
    this.pre = function(){
	this.pos --;
	return this.yearArray[this.pos]
    }
    this.get = function(){
	return this.yearArray[this.pos]
    }
}