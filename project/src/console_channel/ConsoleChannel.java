package console_channel;

import bot.AsyncReader;
import bot.Channel;

import java.util.LinkedList;
import java.util.Queue;

public final class ConsoleChannel implements Channel {
    public static Queue<String> gate = new LinkedList<>();
    @Override
    public String readQuery()
    {
        AsyncReader.asyncRead(gate);
        while (gate.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        AsyncReader.interrupt();
        return gate.remove();
    }
}
