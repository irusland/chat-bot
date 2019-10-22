package TeleBot.command;

import TeleBot.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class MyNameCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public MyNameCommand(AnonymousService anonymouses) {
        super("my_name", "show your current name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.info("LogTemplate.COMMAND_PROCESSING.getTemplate()", user.getId(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {

            sb.append("You are not in bot users' list! Send /start command!");
            log.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to execute '{}' without starting the bot.", user.getId(), getCommandIdentifier());

        } else if(mAnonymouses.getDisplayedName(user) == null) {

            sb.append("Currently you don't have a name.\nSet it using command:\n'/set_name &lt;displayed_name&gt;'");
            log.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to execute '{}' without having a name.", user.getId(), getCommandIdentifier());

        } else {

            log.info("User {} is executing '{}'. Name is '{}'.", user.getId(), getCommandIdentifier(), mAnonymouses.getDisplayedName(user));
            sb.append("Your current name: ").append(mAnonymouses.getDisplayedName(user));
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}