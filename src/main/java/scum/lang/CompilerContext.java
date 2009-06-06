package scum.lang;

public class CompilerContext {

    private static final ThreadLocal<Integer> LINE_NUMBER = new ThreadLocal<Integer>() {
	@Override
	protected Integer initialValue() {
	    return 0;
	}
    };

    static void setCurrentLineNumber(final int lineNumber) {
	LINE_NUMBER.set(lineNumber);
    }

    public static int getCurrentLineNumber() {
	return LINE_NUMBER.get();
    }

}
