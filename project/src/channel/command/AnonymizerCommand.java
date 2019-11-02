<<<<<<< HEAD:project/src/telegram_channel/command/AnonymizerCommand 2.java
package telegram_channel.command;
=======
package channel.command;
>>>>>>> 0b09a81... Correct messaging + stats atarted:project/src/channel/command/AnonymizerCommand.java

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AnonymizerCommand extends BotCommand {

    final Logger log = LogManager.getLogger(getClass());

    AnonymizerCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    void execute(AbsSender sender, SendMessage message, User user) {
        try {
            sender.execute(message);
            log.log(Level.getLevel("LogLevel.SUCCESS.getValue()"), "LogTemplate.COMMAND_SUCCESS.getTemplate()", user.getId(), getCommandIdentifier());
        } catch (TelegramApiException e) {
            log.error("LogTemplate.COMMAND_EXCEPTION.getTemplate()", user.getId(), getCommandIdentifier(), e);
        }
    }
}