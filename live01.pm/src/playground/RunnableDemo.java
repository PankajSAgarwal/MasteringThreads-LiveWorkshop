package playground;

import java.io.*;
import java.net.*;

public class RunnableDemo {
    public static void main(String... args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        ss.accept();
    }
}
