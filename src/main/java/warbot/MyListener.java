package warbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.openqa.selenium.NoSuchElementException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyListener extends ListenerAdapter {
    private static final String PATH = "./screenshot.jpg"; //path to screenshot
    private static final String PREFIX = "~";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return; // ignores messages from bots

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        //remove the first part of string
        if (content.startsWith(PREFIX)) {
            String input = content.substring(content.indexOf(PREFIX) + 1).toLowerCase();

            //rng answers aka 8ball
            if (input.startsWith("question ") || input.startsWith("q ")) {
                channel.sendMessage(eightBall()).queue();
            }

            if (input.equals("profile")) {
                channel.sendMessage(profile(message)).queue();
            }

            //WIP wiki command
            else if (input.startsWith("wiki ") || input.startsWith("w ")) {
                MessageChannel finalChannel = channel;
                new Thread(() -> {
                    try {
                        finalChannel.sendMessage("```" + WikiBox.scrapeWikiText(input.substring(input.indexOf(" "))) + "```").queue();
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        finalChannel.sendMessage("no infobox found").queue();
                    } catch (IOException e) {
                        e.printStackTrace();
                        finalChannel.sendMessage("dummy thicc error").queue();
                    }
                }).start();
            }

            //screenshot wikipedia infobox
            else if (input.startsWith("pic ")) {
                MessageChannel finalChannel = event.getChannel();
                new Thread(() -> {
                    try {
                        WikiBox.scrapeWikiPic(input.substring(input.indexOf(" ")));
                        File file = new File(PATH);
                        finalChannel.sendFile(file).queue();
                        file.delete();

                    } catch (Exception e) {
                        e.printStackTrace();
                        finalChannel.sendMessage("Not Found").queue();
                    }
                }).start();

            }
            //weather
            else if (input.startsWith("temp ") || input.startsWith("t ")) {
                try {
                    String weatherInput = input.substring(input.indexOf(" "));
                    //made this since everyone wants to know the weather of llanfair for some reason
                    if (weatherInput.contains("llanfair")) {
                        String weatherOut = Weather.getWeather("Llanfair");
                        channel.sendMessage(weatherOut).queue();
                    } else {
                        String weatherOut = Weather.getWeather(weatherInput);
                        channel.sendMessage(weatherOut).queue();
                    }
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                    channel.sendMessage("not found, try being specific [format is either ~t toronto or ~t toronto, CA]").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                } catch (Exception e) {
                    channel.sendMessage("dummy thicc error").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                }
            }

            //turn off
            if (author.getId().equals("534564220704915456") || author.getId().equals("108312797162541056"))
                if (input.startsWith("off yourself")) {
                    channel = event.getChannel();
                    channel.sendMessage("The sweet release of powering off <:woo:633411508180877339>").queue();
                    message.addReaction(":woo:633411508180877339").queue();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }

            //lists commands
            if (input.startsWith("help") || input.startsWith("h ")) {
                channel.sendMessage("WIP current commands:\nscreenshot infobox: ~p [page] (only works on pages with infobox)\n8ball/question: ~q [question]," +
                        "\nweather: ~t toronto or ~t toronto, CA\nWiki: ~w [page] (only works on pages with infobox)").queue();
            }


        }


    }


    private String eightBall() {
        final String[] ANSWERS = {"no", "yes", "bruh no way", "yes obviously", "maybe", "not sure tbh", "shut up", "I don't think so", "If you believe", "sure"};
        int x = (int) (10.0 * Math.random());
        return ANSWERS[x];

    }

    private MessageEmbed profile(Message message) {
        return new EmbedBuilder()
                .setTitle(message.getAuthor().getAsTag())
                .addField("username", message.getAuthor().getName(), false)
                .setColor(new Color(3764513))
                .setImage(message.getAuthor().getAvatarUrl())
                .addField("ID", message.getAuthor().getId(), false)
                .build();
    }
}
