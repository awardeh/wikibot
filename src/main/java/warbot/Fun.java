package warbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Fun {
    static String eightBall() {
        final String[] ANSWERS = {"no", "yes", "bruh no way", "yes obviously", "maybe", "not sure tbh",
                "shut up", "I don't think so", "If you believe", "sure"};
        int x = (int) (10.0 * Math.random());
        return ANSWERS[x];
    }

    static MessageEmbed profile(Message message) {
        if (Profiles.inDatabase(message.getAuthor().getId())) {
            return new EmbedBuilder()
                    .setTitle(message.getAuthor().getAsTag())
                    .addField("Username:", message.getAuthor().getName(), true)
                    .addField("profile:", Profiles.getProfile(message.getAuthor().getId()), false)
                    .setColor(new Color(3764513))
                    .setImage(message.getAuthor().getAvatarUrl())
                    .build();
        } else
            return new EmbedBuilder().setTitle("please use ~register first").build();
    }

    static String register(Message message) {
        try {
            Profiles.insert(message.getAuthor().getId());
            return "you've been registered";
        } catch (AlreadyRegisteredException e) {
            return "already registered";
        }
    }

    static String giveCoins(String id, int coins) {
        Profiles.giveMoney(id, coins);
        return (coins + " coins given");
    }
}
