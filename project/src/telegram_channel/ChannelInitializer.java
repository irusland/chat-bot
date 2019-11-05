package telegram_channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public final class ChannelInitializer {

    private static final Logger LOG = LogManager.getLogger(ChannelInitializer.class);

    private static final String PROXY_HOST = "xx.xx.xxx.xxx";
    private static final int PROXY_PORT = 9999;

    public static Channel channel;

    public static void main(String[] args) {

        try {

            LOG.info("Initializing API context...");
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            LOG.info("Configuring bot options...");
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

//            botOptions.setProxyHost(PROXY_HOST);
//            botOptions.setProxyPort(PROXY_PORT);
//            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS4);

            LOG.info("Registering Anonymizer...");
            channel = new Channel(botOptions);
            botsApi.registerBot(channel);
            LOG.info("Anonymizer bot is ready for work!");

        } catch (TelegramApiRequestException e) {
            LOG.error("Error while initializing bot!", e);
        }
    }
}