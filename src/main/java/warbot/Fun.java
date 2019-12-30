package warbot;

public class Fun {
    static String eightBall() {
        final String[] ANSWERS = {"no", "yes", "bruh no way", "yes obviously", "maybe", "not sure tbh",
                "shut up", "I don't think so", "If you believe", "sure"};
        int x = (int) (10.0 * Math.random());
        return ANSWERS[x];
    }
}