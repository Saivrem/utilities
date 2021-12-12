package general;

/**
 * Progress bar class, receives full value and counts delta
 */
public class ProgressBar {

    /**
     * Full value.
     */
    private final long fullValue;
    /**
     * Current progress.
     */
    private long currentValue = 0;
    /**
     * Passed milestone in progress.
     */
    private long passedPoint = -1;

    /**
     * Indicator "graphics".
     */
    private final StringBuilder progress = new StringBuilder();

    /**
     * Constructor.
     *
     * @param fullValue full value;
     */
    public ProgressBar(long fullValue) {
        this.fullValue = fullValue;
    }

    /**
     * Getter for the full value.
     *
     * @return long fullValue;
     */
    public long getFullValue() {
        return fullValue;
    }

    /**
     * Draws a progress bar
     *
     * @param delta change in value;
     */
    public void draw(long delta, String name) {

        currentValue += delta;

        if (fullValue != 0) {
            long currentPercent = countPercent();
            if (currentPercent % 5 == 0 && currentPercent != passedPoint) {
                progress.append("=");
                passedPoint = currentPercent;
            }

            if (currentPercent == 100) {
                System.out.printf("\r[====================>] %d%%, %s\nDone\n", currentPercent, name);
            } else {
                System.out.printf("\r[%-" + 21 + "s] %d%%, %s", progress + ">", currentPercent, name);
            }
        }
    }

    /**
     * Counts current percent.
     *
     * @return percent;
     */
    private long countPercent() {
        return (currentValue * 100) / fullValue;
    }
}