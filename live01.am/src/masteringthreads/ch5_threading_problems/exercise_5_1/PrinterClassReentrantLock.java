package masteringthreads.ch5_threading_problems.exercise_5_1;

import java.util.concurrent.locks.*;

// t1: print(String): PrinterClass, this
// t2: setPrintingEnabled(false): this, PrinterClass
public class PrinterClassReentrantLock {
    private final static Lock classLock = new ReentrantLock();
    private final Lock objectLock = new ReentrantLock();

    private static final boolean OUTPUT_TO_SCREEN = false;
    private boolean printingEnabled = OUTPUT_TO_SCREEN;

    private static void print(PrinterClassReentrantLock pc, String s) {
        classLock.lock();
        try {
            if (pc.isPrintingEnabled()) {
                System.out.println("Printing: " + s);
            }
        } finally {
            classLock.unlock();
        }
    }

    public void print(String s) {
        print(this, s);
    }

    public boolean isPrintingEnabled() {
        objectLock.lock();
        try {
            return printingEnabled;
        } finally {
            objectLock.unlock();
        }
    }

    public void setPrintingEnabled(boolean printingEnabled) {
        objectLock.lock();
        try {
            if (!printingEnabled) {
                print(this, "Printing turned off!");
            }
            this.printingEnabled = printingEnabled;
        } finally {
            objectLock.unlock();
        }
    }
}
