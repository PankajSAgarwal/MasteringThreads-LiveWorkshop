package masteringthreads.ch5_threading_problems.exercise_5_1;

public class PrinterClass {
    private static final boolean OUTPUT_TO_SCREEN = false;
    private volatile boolean printingEnabled = OUTPUT_TO_SCREEN;
    private static final Object lock1 = PrinterClass.class;
    private final Object lock2 = this;

    private static void print(PrinterClass pc, String s) {
        synchronized (lock1) {
            if (pc.isPrintingEnabled()) {
                System.out.println("Printing: " + s);
            }
        }
    }

    public void print(String s) { // t1 - lock1
        print(this, s);
    }

    public boolean isPrintingEnabled() {
        return printingEnabled;
    }

    public void setPrintingEnabled(boolean printingEnabled) { // t2(false) - lock2, lock1
        synchronized (lock2) {
            if (!printingEnabled) {
                print(this, "Printing turned off!");
            }
            this.printingEnabled = printingEnabled;
        }
    }
}
