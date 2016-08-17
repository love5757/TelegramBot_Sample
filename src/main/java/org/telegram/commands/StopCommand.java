package org.telegram.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

/**
 * This commands stops the conversation with the bot.
 * Bot won't respond to user until he sends a start command
 *
 * @author Timo Schulz (Mit0x2)
 */
public class StopCommand extends BotCommand {

    static final Logger logger = LoggerFactory.getLogger(StopCommand.class);

    /**
     * Construct
     */
    public StopCommand() {
        super("stop", "With this command you can stop the Bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String userName = user.getFirstName() + " " + user.getLastName();

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText("Good bye " + userName + "\n" + "Hope to see you soon!");

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }

    }
}
