package botele;

import botele.command.*;
import botele.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;


public final class AnonymizerBot extends TelegramLongPollingBot {

    private static final Logger LOG = LogManager.getLogger(AnonymizerBot.class);

    // имя бота, которое мы указали при создании аккаунта у BotFather
    // и токен, который получили в результате
    private static final String BOT_NAME = "Jovajbot";
    private static final String BOT_TOKEN = "843494970:AAGmf3kRRpEGnyOkyils3f9xbsmBmlK7eEw";

    private final AnonymousService mAnonymouses;
    private Stage stage;

    public AnonymizerBot(DefaultBotOptions botOptions) {

//        super(botOptions, BOT_NAME);
        mAnonymouses = new AnonymousService();
        stage = Stage.Login;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            throw new IllegalStateException("Update doesn't have a body!");
        }

        Message msg = update.getMessage();
        User user = msg.getFrom();

        if (!canSendMessage(user, msg)) {
            return;
        }

        String clearMessage = msg.getText();
        String messageForUsers = String.format("%s:\n%s", mAnonymouses.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();

        // отправка ответа отправителю о том, что его сообщение получено
        answer.setText(clearMessage);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user, clearMessage);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }


    // несколько проверок, чтобы можно было отправлять сообщения другим пользователям
    private boolean canSendMessage(User user, Message msg) {

        SendMessage answer = new SendMessage();
        answer.setChatId(msg.getChatId());

        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            LOG.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to send empty message!", user.getId());
            answer.setText("You shouldn't send empty messages!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

//        if(!mAnonymouses.hasAnonymous(user)) {
//            LOG.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to send message without starting the bot!", user.getId());
//            answer.setText("Firstly you should start bot! Use /start command!");
//            replyToUser(answer, user, msg.getText());
//            return false;
//        }

//        if (mAnonymouses.getDisplayedName(user) == null) {
//            LOG.log(Level.getLevel("LogLevel.STRANGE.getValue()"), "User {} is trying to send message without setting a name!", user.getId());
//            answer.setText("You must set a name before sending messages.\nUse '/set_name <displayed_name>' command.");
//            replyToUser(answer, user, msg.getText());
//            return false;
//        }

        return true;
    }

    private void sendMessageToUser(SendMessage message, User receiver, User sender) {
        try {
            execute(message);
            LOG.log(Level.getLevel("LogLevel.SUCCESS.getValue()"), "LogTemplate.MESSAGE_RECEIVED.getTemplate()", receiver.getId(), sender.getId());
        } catch (TelegramApiException e) {
            LOG.error("LogTemplate.MESSAGE_LOST.getTemplate()", receiver.getId(), sender.getId(), e);
        }
    }

    private void replyToUser(SendMessage message, User user, String messageText) {
        try {
            execute(message);
            LOG.log(Level.getLevel("LogLevel.SUCCESS.getValue()"), "LogTemplate.MESSAGE_SENT.getTemplate()", user.getId(), messageText);
        } catch (TelegramApiException e) {
            LOG.error("LogTemplate.MESSAGE_EXCEPTION.getTemplate()", user.getId(), e);
        }
    }
}

