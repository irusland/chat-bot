<<<<<<< HEAD:project/src/channel/command/StopCommand.java
<<<<<<< HEAD:project/src/telegram_channel/command/StopCommand 2.java
package telegram_channel.command;

import telegram_channel.service.PlayerService;
=======
package channel.command;

import channel.service.PlayerService;
>>>>>>> 0b09a81... Correct messaging + stats atarted:project/src/channel/command/StopCommand.java
=======
package telegram_channel.command;

import telegram_channel.service.PlayerService;
>>>>>>> fec1c84... Channels switch:project/src/telegram_channel/command/StopCommand.java
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StopCommand extends AnonymizerCommand {

    private final PlayerService mAnonymouses;

    public StopCommand(PlayerService anonymouses) {
        super("stop", "remove yourself from bot users' list\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info("LogTemplate.COMMAND_PROCESSING.getTemplate()", user.getId(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.removeAnonymous(user)) {
            log.info("User {} has been removed from users list!", user.getId());
            sb.append("You've been removed from bot's users list! Bye!");
        } else {
            log.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to execute '{}' without having executed 'start' before!", user.getId(), getCommandIdentifier());
            sb.append("You were not in bot users' list. Bye!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}