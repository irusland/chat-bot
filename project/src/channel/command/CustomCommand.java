<<<<<<< HEAD:project/src/telegram_channel/command/CustomCommand 2.java
package telegram_channel.command;

import telegram_channel.service.PlayerService;
=======
package channel.command;

import channel.service.PlayerService;
>>>>>>> 0b09a81... Correct messaging + stats atarted:project/src/channel/command/CustomCommand.java
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class CustomCommand extends AnonymizerCommand {
    private final PlayerService mAnonymouses;

    public CustomCommand(PlayerService anonymouses) {
        super("register", "start using bot\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}