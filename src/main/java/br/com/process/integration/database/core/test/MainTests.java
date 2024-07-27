package br.com.process.integration.database.core.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String where = "AND p.discout BETWEEN (:discoutStart) AND (:discoutEnd)";

		//String where = "[120.21, 180.34]";
		
		
		System.out.println(where.replaceAll("[\\(\\)]", ""));

	}

}
