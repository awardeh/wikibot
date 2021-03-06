package warbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.openqa.selenium.NoSuchElementException;

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
            MessageChannel finalChannel = event.getChannel();
            String input = content.substring(content.indexOf(PREFIX) + 1).toLowerCase();

            // fun commands
            if (input.startsWith("question ") || input.startsWith("q ")) {
                channel.sendMessage(Fun.eightBall()).queue();
            }

            //WIP wiki command
            else if (input.startsWith("wiki ") || input.startsWith("w ")) {
                new Thread(() -> {
                    try {
                        finalChannel.sendMessage("```"
                                + WikiBox.scrapeInfobox(input.substring(input.indexOf(" "))) + "```").queue();
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
            else if (input.startsWith("pic ") || input.startsWith("p ")) {
                new Thread(() -> {
                    try {
                        WikiBox.screenshotInfobox(input.substring(input.indexOf(" ")));
                        File file = new File(PATH);
                        finalChannel.sendFile(file).queue();
                        file.delete();

                    } catch (Exception e) {
                        e.printStackTrace();
                        finalChannel.sendMessage("Not Found").queue();
                    }
                }).start();

            } else if (input.startsWith("image ") || input.startsWith("i ")) {
                new Thread(() -> {
                    try {
                        finalChannel.sendMessage(WikiBox.getImage(input.substring(input.indexOf(" ")))).queue();

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
                    channel.sendMessage("not found, try being specific [format is either" +
                            " ~t toronto or ~t toronto, CA]").queue();
                } catch (Exception e) {
                    channel.sendMessage("dummy thicc error").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                }
            }

            //turn off
            if (author.getId().equals("534564220704915456") || author.getId().equals("108312797162541056")) {
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
            }

            //lists commands
            if (input.startsWith("help") || input.startsWith("h ")) {
                channel.sendMessage(new EmbedBuilder().setTitle("Wikibot")
                        .setDescription("WIP")
                        .setFooter("Wikibot",
                                "https://cdn.discordapp.com/avatars/644742765678166026/" +
                                        "94a9a050fd980695db4c7200aeb1a9b2.webp")
                        .addField("Prefix", "~", false)
                        .addField("text from wikipedia infobox",
                                "[~wiki or ~w] <wikipedia page with infobox>", false)
                        .addField("screenshot of the infobox",
                                "[~pic or ~p] <wikipedia page with infobox>", false)
                        .addField("temperature of city",
                                "~temp <city> or ~t <city, country code>", false)
                        .addField("basic 8ball-like command",
                                "~question <anything>", false)
                        .build()).queue();
            }
        }
    }
}