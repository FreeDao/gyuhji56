package com.bmtech.thsParser.deviate;

public class Ln {

	public static void main(String[] args) {
		for(int x = 0; x < 20; x ++){
			System.out.println(x * 0.05 + "\t" + Math.log1p(x * 0.05));
		}
	}
}
