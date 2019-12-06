package warbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.openqa.selenium.NoSuchElementException;

import java.io.File;
import java.io.IOException;

public class MyListener extends ListenerAdapter {
    //    private static final String PATH = "C:\\Users\\aw\\IdeaProjects\\warbot\\test.jpg";
    private static final String PATH = "./test.jpg";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();

        MessageChannel channel = event.getChannel();
        //meme responses
        if (content.toLowerCase().startsWith("tch")) {
            channel.sendMessage("tch yourself").queue();
        }

        //commands
        if ((content.toLowerCase().startsWith("warbot")) || (content.startsWith("~w"))) {
            //remove the first part of string
            String newString = content.substring(content.indexOf(" ") + 1);
            if (newString.toLowerCase().contains("gay")) {
                channel.sendMessage("<@" + author.getId() + "> is gay").queue();
            }

            //rng answers aka 8ball
            else if (newString.toLowerCase().startsWith("question")) {
                channel.sendMessage(eightBall()).queue();
            }
            //screenshot wikipedia infobox
            else if (newString.toLowerCase().startsWith("pic ")) {
                channel = event.getChannel();
                try {
                    WikiBox.scrapeWikiPic(newString.substring(newString.indexOf(" ") + 1));
                    File file = new File(PATH);
                    channel.sendFile(file).queue();
                    file.delete();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    channel.sendMessage("can't find infobox").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                } catch (Exception e) {
                    e.printStackTrace();
                    channel.sendMessage("dummy thicc error").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                }
            }
            //weather
            else if (newString.toLowerCase().startsWith("weather")) {
                try {
                    String weather = Weather.getWeather(newString.substring(newString.indexOf(" ")));
                    channel.sendMessage(weather).queue();
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                    channel.sendMessage("not found, try being specific [format is either ~w weather toronto or ~w weather toronto, CA]").queue();
                    message.addReaction(":ragescream:621200977671749652").queue();
                } catch (Exception e) {
                    channel.sendMessage("dummy thicc error");
                    message.addReaction(":ragescream:621200977671749652").queue();
                }
            }
            //turn off
            if (author.getId().equals("534564220704915456") || author.getId().equals("108312797162541056"))
                if (newString.startsWith("off yourself")) {
                    channel = event.getChannel();
                    channel.sendMessage("The sweet release of death <:woo:633411508180877339>").queue();
                    message.addReaction(":woo:633411508180877339").queue();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }

            if (newString.toLowerCase().startsWith("help")) {
                channel.sendMessage("WIP current commands:\npic [~w pic battle of the bulge]\nquestion[~w question is this bot good?]," +
                        "\nweather [ ~w weather toronto or ~w weather toronto, CA]").queue();
            }

        }

    }


    private String eightBall() {
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
