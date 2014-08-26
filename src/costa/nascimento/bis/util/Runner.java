package costa.nascimento.bis.util;

public class Runner {
	private static boolean isGamePaused;

	public static Runner runner = null;

	private Runner() {
	}

	public static Runner check() {
		if (runner != null) {
			runner = new Runner();
		}
		return runner;
	}

	public static boolean isGamePaused() {
		return isGamePaused;
	}

	public static void setGamePaused(boolean isGamePaused) {
		Runner.isGamePaused = isGamePaused;
	}
	
	
}
