package com.example

class Complex(real: Double, imaginary: Double) {
	var num : Double = 0;
  
	def re() = real
	def im() : Double = imaginary;
	
	def setReal(real: Double) {
	  this.num = real;
	  println(this.real);
	}
	
	override def toString() = "" + re + (if(im < 0) "" else "+") + im + "i"	
}