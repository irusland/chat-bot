package bot;

import java.io.Serializable;

enum ChannelType implements Serializable {
    Console,
    Telegram;

    public ChannelType switchChannel() {
        if (this == Console)
            return ChannelType.Telegram;
        return ChannelType.Console;
    }
}
