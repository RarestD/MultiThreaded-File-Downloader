package rare.prod.util;

public class ProgressBar {
    public static String renderBar(long current, long total, int length) {
        double percent = (double) current / total;
        int filled = (int) (length * percent);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            if (i < filled) bar.append("=");
            else bar.append(" ");
        }
        bar.append("] ");
        bar.append(String.format("%.2f%%", percent * 100));
        return bar.toString();
    }
}
