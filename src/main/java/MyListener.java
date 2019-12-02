import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.openqa.selenium.NoSuchElementException;

import java.io.File;
import java.util.Random;

public class MyListener extends ListenerAdapter {
    private static final String path = "C:\\Users\\aw\\IdeaProjects\\warbot\\test.jpg";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        //meme responses
        if (content.startsWith("tch") || content.startsWith("Tch")) {
            channel.sendMessage("tch yourself").queue();
        }
        if (content.startsWith("based") || content.startsWith("Based") || content.startsWith("BASED")) {
            channel.sendMessage("based").queue();
        }
        if (author.getId().equals("327404184720769034")) {
            channel.sendMessage("SHUT THE FUCK UP <@327404184720769034>").queue();
        }
        //commands
        if ((content.startsWith("warbot") || (content.startsWith("Warbot")))) {
            //remove the first part of string
            String newString = content.substring(content.indexOf(" ") + 1);

            //rng answers aka 8ball
            if (newString.startsWith("question") || newString.startsWith("Question")) {
                channel.sendMessage(eightBall()).queue();
            }

            //screenshot wikipedia infobox
            if (newString.startsWith("pic")) {
                channel = event.getChannel();
                try {
                    message.addReaction("ragetears:588143943892336896").queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    WikiBox.scrapeWikiPic(newString.substring(4));
                    channel.sendFile(new File(path)).queue();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    channel.sendMessage("can't find infobox").queue();
                } catch (Exception e) {
                    e.printStackTrace();
                    channel.sendMessage("dummy thicc error").queue();
                }
            }

            //turn off
            if (author.getId().equals("534564220704915456"))
                if (newString.startsWith("off yourself")) {
                    channel = event.getChannel();
                    channel.sendMessage("The sweet release of death <:woo:633411508180877339>").queue();
                    message.addReaction(":woo:633411508180877339").queue();
                    System.exit(0);
                }

        }
    }


    private String eightBall() {
        Random random = new Random();
        String result = "";
        int x = (int) (10.0 * Math.random());
        switch (x) {
            case 0:
                result = "no";
                break;
            case 1:
                result = "yes";
                break;
            case 2:
                result = "bruh no way";
                break;
            case 3:
                result = "yes obviously";
                break;
            case 4:
                result = "maybe";
                break;
            case 5:
                result = "not sure tbh";
                break;
            case 6:
                result = "shut up";
                break;
            case 7:
                result = "I don't think so";
                break;
            case 8:
                result = "if you believe";
                break;
            case 9:
                result = "sure";
                break;
        }
        return result;

    }
}
