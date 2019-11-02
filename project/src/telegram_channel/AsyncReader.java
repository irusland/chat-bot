package bot;

import bot.Bot;

import java.io.*;
import java.util.Queue;

public class AsyncReader {
    private static Thread th;
    public static void interrupt() {
        th.interrupt();
    }

    public static void asyncRead(Queue<String> list) {
        try {
            PipedOutputStream stagingPipe = new PipedOutputStream();
            PipedInputStream releasingPipe = new PipedInputStream(stagingPipe);
            var reader = new BufferedReader(new InputStreamReader(System.in));
            Thread stagingThread = new Thread(() -> {
                try {
                    list.add(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stagingThread.setDaemon(true);
            stagingThread.start();
            th = new Thread(() -> {
                try {
                    releasingPipe.read();
                } catch (InterruptedIOException e) {
                    // read interrupted
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            th.start();
//            System.out.println(th.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
