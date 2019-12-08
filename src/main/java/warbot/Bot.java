package warbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bot extends ListenerAdapter {

    private static void startBot() throws LoginException, IOException {
        String botToken = ""; // intializes bot token
        Path path = Paths.get("./bottoken"); //the path of the bottoken file should be in the project folder or in the jar folder
        try {
            botToken = Files.readString(path); //reads the bottoken
        } catch (NoSuchFileException e) {
            System.out.println("no bottoken");
            Files.writeString(path, ""); //creates an emtpy bottoken file in case there is none
        }
        JDA api = new JDABuilder(botToken).build();
        MyListener listener = new MyListener();
        api.addEventListener(listener);
    }

    public static void main(String[] args) throws LoginException, IOException {
        startBot();
    }
}
