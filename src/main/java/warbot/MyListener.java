package warbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;

public class MyListener extends ListenerAdapter {
    private static final String PATH = "./screenshot.jpg"; //path to screenshot

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return; // ignores messages from bots

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        //meme responses
        if (content.toLowerCase().startsWith("tch")) {
            channel.sendMessage("tch yourself").queue(); //annoys kratos
        }
        if (content.toLowerCase().startsWith("cope")) {
            message.addReaction(":cope:588146215779172378").queue(); //reacts with the cope emote to messages starting with cope
        }

        if (content.toLowerCase().startsWith("based") || content.toLowerCase().contains(":based:") || content.toLowerCase().contains(" based")) {
            channel.sendMessage("based").queue(); //responds with based when someone says  based
            message.addReaction(":based:653403140829478958").queue();
        }
        //commands
        if (((content.startsWith("~")))) {
            //remove the first part of string
            String newString = content.substring(content.indexOf("~") + 1);

            //rng answers aka 8ball
            if (newString.toLowerCase().startsWith("question") || newString.toLowerCase().startsWith("q ")) {
                channel.sendMessage(eightBall()).queue();
            }
            //screenshot wikipedia infobox
            else if (newString.toLowerCase().startsWith("pic") || newString.toLowerCase().startsWith("p ")) {
                channel = event.getChannel();
                MessageChannel finalChannel = channel;
                new Thread(() -> {
                    try {
                        WikiBox.scrapeWikiPic(newString.substring(newString.indexOf(" ")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    File file = new File(PATH);
                    finalChannel.sendFile(file).queue();
                    file.delete();
                    return;
                }).start();

            }
            //weather
            else if (newString.toLowerCase().startsWith("weather") || newString.toLowerCase().startsWith("w ")) {
                try {
                    String weatherInput = newString.substring(newString.indexOf(" "));
                    //made this since everyone wants to know the weather of llanfair for some reason
                    if (weatherInput.toLowerCase().contains("llanfair")) {
                        String weatherOut = Weather.getWeather("Llanfair");
                        channel.sendMessage(weatherOut).queue();
                    } else {
                        String weatherOut = Weather.getWeather(weatherInput);
                        channel.sendMessage(weatherOut).queue();
                    }
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                    channel.sendMessage("not found, try being specific [format is either ~w weather toronto or ~w weather toronto, CA]").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                } catch (Exception e) {
                    channel.sendMessage("dummy thicc error").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                }
            }
            //turn off
            if (author.getId().equals("534564220704915456") || author.getId().equals("108312797162541056"))
                if (newString.startsWith("off yourself")) {
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
            if (newString.toLowerCase().startsWith("help")) {
                channel.sendMessage("WIP current commands:\npic [~p battle of the bulge]\nquestion[~q is this bot good?]," +
                        "\nweather [ ~w toronto or ~w toronto, CA]").queue();
            }

        }

    }


    private String eightBall() {
        final String[] ANSWERS = {"no", "yes", "bruh no way", "yes obviously", "maybe", "not sure tbh", "shut up", "I don't think so", "If you believe", "sure"};
        int x = (int) (10.0 * Math.random());
        return ANSWERS[x];

    }
}
