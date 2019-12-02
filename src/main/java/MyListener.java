import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;

public class MyListener extends ListenerAdapter {
    private static final String path = "";

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
        //commands
        if ((content.startsWith("warbot") || (content.startsWith("Warbot")))) {
            String newString = content.substring(7);
            //screenshot infobox
            if (newString.startsWith("pic")) {
                channel = event.getChannel();
                try {
                    message.addReaction("ragetears:588143943892336896").queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    WikiBox.scrapeWikiPic(content.substring(11));
                } catch (Exception e) {
                    e.printStackTrace();
                    channel.sendMessage("bruh not found try a more specific title").queue();
                }
                channel.sendFile(new File(path)).queue();
            }
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
