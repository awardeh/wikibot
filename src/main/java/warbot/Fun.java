package warbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Fun {
    static String eightBall() {
        final String[] ANSWERS = {"no", "yes", "bruh no way", "yes obviously", "maybe", "not sure tbh", "shut up", "I don't think so", "If you believe", "sure"};
        int x = (int) (10.0 * Math.random());
        return ANSWERS[x];
    }

    static MessageEmbed profile(Message message) {
        return new EmbedBuilder()
                .setTitle(message.getAuthor().getAsTag())
                .addField("username", message.getAuthor().getName(), false)
                .setColor(new Color(3764513))
                .setImage(message.getAuthor().getAvatarUrl())
                .addField("ID", message.getAuthor().getId(), false)
                .build();
    }
}
