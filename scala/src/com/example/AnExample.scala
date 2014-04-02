package com.example

object AnExample {
	def main(args: Array[String]) {
	  println("test")
	  var c = new Complex(1.5, 2.3);
	  
	  println(c.im())
	  println(c.re())
	}
}