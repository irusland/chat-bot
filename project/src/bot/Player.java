package bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public final class Player {

    private static final Logger LOG = LogManager.getLogger(Player.class);
    private static final String USER_CHAT_CANNOT_BE_NULL = "User or chat cannot be null!";

    private final User mUser;
    private final Chat mChat;
    private String mDisplayedName;

    public Player(User user, Chat chat) {
        if (user == null || chat == null) {
            LOG.error(USER_CHAT_CANNOT_BE_NULL);
            throw new IllegalStateException(USER_CHAT_CANNOT_BE_NULL);
        }
        mUser = user;
        mChat = chat;
    }

    @Override
    public int hashCode() {
        return mUser.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && ((Player) obj).getUser().equals(mUser);
    }

    public User getUser() {
        return mUser;
    }

    public Chat getChat() {
        return mChat;
    }

    public String getDisplayedName() {
        return mDisplayedName;
    }

    public void setDisplayedName(String displayedName) {
        mDisplayedName = displayedName;
    }
}