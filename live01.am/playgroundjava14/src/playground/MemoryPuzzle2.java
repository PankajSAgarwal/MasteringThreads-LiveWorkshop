package playground;

public class MemoryPuzzle2 {
    public static void main(String[] args) {
        {
            long[] test = new long[
                (int)(Runtime.getRuntime().maxMemory() * 0.7 / 8)];
        }


        long[] test = new long[
            (int)(Runtime.getRuntime().maxMemory() * 0.7 / 8)];
    }
}
