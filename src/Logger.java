
public class Logger {

	public static final boolean isDebug = true;
	public static void print(String message) {
		if(isDebug)
			System.out.print(message);
	}
	public static void println(String message) {
		if(isDebug)
			System.out.println(message);
	}
}
