package masteringthreads.ch5_threading_problems.exercise_5_1;

// t1: print(String): PrinterClass
// t2: setPrintingEnabled(false): this, PrinterClass
public class PrinterClass {
    private static final boolean OUTPUT_TO_SCREEN = false;
    private volatile boolean printingEnabled = OUTPUT_TO_SCREEN;

    private static void print(PrinterClass pc, String s) {
        synchronized (PrinterClass.class) {
            if (pc.isPrintingEnabled()) {
                System.out.println("Printing: " + s);
            }
        }
    }

    public void print(String s) {
        print(this, s);
    }

    public boolean isPrintingEnabled() {
        return printingEnabled;
    }

    public void setPrintingEnabled(boolean printingEnabled) {
        synchronized (this) {
            if (!printingEnabled) {
                print(this, "Printing turned off!");
            }
            this.printingEnabled = printingEnabled;
        }
    }
}
