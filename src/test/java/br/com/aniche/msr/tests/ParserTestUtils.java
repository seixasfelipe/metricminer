package br.com.aniche.msr.tests;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ParserTestUtils {

	public static String interfaceDeclaration(String body) {
		return "interface Foo { " + body + "}";
	}
	
	public static String classDeclaration(String body) {
		return "class Program { " + body + "}";
	}
	
	public static String imports(String imports, String body) {
		return imports + body;
	}
	
	public static CompilationUnit source(String sourceCode) {
		try {
			return JavaParser.parse(new ByteArrayInputStream(sourceCode
					.getBytes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static InputStream toInputStream(String text) {
		return new ByteArrayInputStream(text.getBytes());
	}
}
