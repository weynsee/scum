package scum.lang;

import java.io.IOException;
import java.io.Reader;

public class Lexer {

    private final LexerState lexer;

    public Lexer(final Reader reader) {
	this.lexer = new LexerState(reader);
    }

    public String scan() {
	StringBuilder token = new StringBuilder();
	// for (StateMachine state = StateMachine.START; state !=
	// StateMachine.END
	// && !lexer.isEof(); state = state.next(lexer, token)) {
	// }
	StateMachine state = StateMachine.START;
	while (state != StateMachine.END && !lexer.isEof()) {
	    state = state.next(lexer, token);
	}
	CompilerContext.setCurrentLineNumber(lexer.currentLineNumber());
	return token.toString();
    }

    private enum StateMachine {
	START {
	    @Override
	    StateMachine next(final LexerState lexer, final StringBuilder token) {
		char current = lexer.pop();
		StateMachine nextState = null;
		switch (current) {
		case '\n':
		    nextState = StateMachine.START;
		    lexer.incrementLineNumber();
		    break;
		case ' ':
		case '\t':
		case 13:
		    nextState = StateMachine.START;
		    break;
		case '(':
		case ')':
		case '{':
		case '}':
		case ';':
		case ',':
		case '[':
		case ']':
		case ':':
		    nextState = StateMachine.END;
		    token.append(current);
		    break;
		default:
		    if (Character.isDigit(current)) {
			nextState = StateMachine.NUMBER;
			token.append(current);
		    }
		}
		return nextState;
	    }
	},
	END {
	    @Override
	    StateMachine next(final LexerState lexer, final StringBuilder token) {
		return END;
	    }
	},
	NUMBER {
	    @Override
	    StateMachine next(final LexerState lexer, final StringBuilder token) {
		return null;
	    }
	};

	abstract StateMachine next(LexerState lexer, StringBuilder token);
    }

    private static class LexerState {
	private final Reader input;
	private int lineNumber;
	private char nextChar;

	private LexerState(final Reader characters) {
	    this.input = characters;
	    nextChar = readChar();
	}

	private char readChar() {
	    try {
		return (char) input.read();
	    } catch (IOException e) {
		// TODO: replace this with custom exception
		throw new IllegalStateException(e);
	    }
	}

	private char pop() {
	    char current = nextChar;
	    nextChar = readChar();
	    return current;
	}

	private void incrementLineNumber() {
	    lineNumber++;
	}

	private int currentLineNumber() {
	    return lineNumber;
	}

	private boolean isEof() {
	    return nextChar == (char) -1;
	}
    }

}
