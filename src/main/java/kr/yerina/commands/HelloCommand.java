package kr.yerina.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

/**
 * This command simply replies with a hello to the users command and
 * sends them the 'kind' words back, which they send via command parameters
 *
 * @author Timo Schulz (Mit0x2)
 */
public class HelloCommand extends BotCommand {

    static final Logger logger = LoggerFactory.getLogger(HelloCommand.class);

    public HelloCommand() {
        super("hello", "Say hallo to this bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        logger.debug("[{}][{}][{}][{}]",absSender, user, chat, arguments);

        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);
        if (arguments != null && arguments.length > 0) {
            messageTextBuilder.append("\n");
            messageTextBuilder.append("Thank you so much for your kind words:\n");
            messageTextBuilder.append(String.join(" ", arguments));
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageTextBuilder.toString());

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }
}