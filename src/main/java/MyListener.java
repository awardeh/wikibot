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
        if (author.getId().equals("327404184720769034")){
            channel.sendMessage("SHUT THE FUCK UP <@327404184720769034>");
        }
        //commands
        if ((content.startsWith("warbot") || (content.startsWith("Warbot")))) {
            //remove the first part of string
            String newString = content.substring(content.indexOf(" ") + 1);

            //rng answers aka 8ball
            if (newString.startsWith("question") || newString.startsWith("Question")) {
                Random random = new Random();
                int x = (int) (10.0 * Math.random());
                switch (x) {
                    case 0:
                        channel.sendMessage("no").queue();
                        break;
                    case 1:
                        channel.sendMessage("yes").queue();
                        break;
                    case 2:
                        channel.sendMessage("bruh no way").queue();
                        break;
                    case 3:
                        channel.sendMessage("yes obviously").queue();
                        break;
                    case 4:
                        channel.sendMessage("maybe").queue();
                        break;
                    case 5:
                        channel.sendMessage("not sure myself").queue();
                        break;
                    case 6:
                        channel.sendMessage("shut up").queue();
                        break;
                    case 7:
                        channel.sendMessage("i don't think so").queue();
                        break;
                    case 8:
                        channel.sendMessage("if you believe").queue();
                        break;
                    case 9:
                        channel.sendMessage("sure").queue();
                        break;

                }

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
                    WikiBox.scrapeWikiPic(content.substring(11));
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
}
