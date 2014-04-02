package com.example

object Timer {
	def oncePerSecond(callback: () => Unit) {
	  while(true) {
		  callback();
		  Thread sleep 1000
	  }
	}

	def timeFlies() {
	  println("time filies like .....");
	}
	
	def main(args: Array[String]) {
		oncePerSecond(() => {
		  println("time filies like .....1");
		  println("time filies like .....2");
		});
	}	
}