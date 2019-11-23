import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

public class MyListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including ourself
        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
        if (content.startsWith("warbot ")) {
            WikiBox wikibox = null;
            try {
                wikibox = new WikiBox(content.substring(7));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MessageChannel channel = event.getChannel();
            channel.sendMessage(wikibox.title).queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)

        }
    }
}
