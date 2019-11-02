package channel;

import channel.service.PlayerService;
import bot.Bot;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;



public final class Channel extends TelegramLongPollingBot {

    private static final Logger LOG = LogManager.getLogger(Channel.class);

    private static final String BOT_NAME = "Jovajbot";
    private static final String BOT_TOKEN = "843494970:AAGmf3kRRpEGnyOkyils3f9xbsmBmlK7eEw";

    private final PlayerService players;

    public Channel(DefaultBotOptions botOptions) {
        players = new PlayerService();
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.info("Processing non-command update...");

        if (!update.hasMessage()) {
            LOG.error("Update doesn't have a body!");
            throw new IllegalStateException("Update doesn't have a body!");
        }

        Message msg = update.getMessage();
        User user = msg.getFrom();


        LOG.info("LogTemplate.MESSAGE_PROCESSING.getTemplate()", user.getId());

        if (!canSendMessage(user, msg)) {
            return;
        }

        Bot.gateIn.add(msg.getText()); // SEND UPDATE

        String clearMessage = msg.getText();
        String messageForUsers = String.format("%s:\n%s", players.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();

        // отправка ответа отправителю о том, что его сообщение получено

        var ans = Bot.getAnswer();

        answer.setText(ans);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user, ans);

        // отправка сообщения всем остальным пользователям бота
        answer.setText(messageForUsers);
        Stream<Player> anonymouses = players.anonymouses();
        anonymouses.filter(a -> !a.getUser().equals(user))
                .forEach(a -> {
                    answer.setChatId(a.getChat().getId());
                    sendMessageToUser(answer, a.getUser(), user);
                });
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

        if(!players.hasAnonymous(user)) {
            players.addAnonymous(new Player(user, new Chat()));
            answer.setText(Bot.getAnswer());
            replyToUser(answer, user, msg.getText());
            return false;
        }

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