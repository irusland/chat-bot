package bot.command;

import bot.service.PlayerService;
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