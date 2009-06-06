package scum.lang;

public class Lexer {

    private final LexerState lexer;

    public Lexer(final String characters) {
	this.lexer = new LexerState(characters);
    }

    public String scan() {
	StringBuilder token = new StringBuilder();
	for (StateMachine state = StateMachine.START; state != StateMachine.END
		&& !lexer.isEof(); state = state.next(lexer, token)) {
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
	private final String characters;
	private int pointer;
	private int lineNumber;

	private LexerState(final String characters) {
	    this.characters = characters;
	}

	private char pop() {
	    return characters.charAt(pointer++);
	}

	private void incrementLineNumber() {
	    lineNumber++;
	}

	private int currentLineNumber() {
	    return lineNumber;
	}

	private boolean isEof() {
	    return pointer >= characters.length();
	}
    }

}
