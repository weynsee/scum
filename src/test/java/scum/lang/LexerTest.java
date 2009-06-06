package scum.lang;

import junit.framework.TestCase;

import org.junit.Test;

public class LexerTest extends TestCase {

    @Test
    public void testLineNumbers() {
	int lines = 5;
	String code = "";
	for (int i = 0; i < lines; i++) {
	    code += "\n";
	}
	Lexer lex = new Lexer(code);
	lex.scan();
	lex.scan();
	assertEquals(lines, CompilerContext.getCurrentLineNumber());
    }

}
