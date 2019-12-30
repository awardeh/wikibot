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
        // intializes bot token
        String botToken = "";
        //the path of the bottoken file should be in the project folder or in the jar folder
        Path path = Paths.get("./bottoken");
        try {
            botToken = Files.readString(path); //reads the bottoken
        } catch (NoSuchFileException e) {
            System.out.println("no bot token");
            //creates an empty bottoken file in case there is none
            Files.writeString(path, "");
        }
        JDA api = new JDABuilder(botToken).build();
        MyListener listener = new MyListener();
        api.addEventListener(listener);
    }

    public static void main(String[] args) throws LoginException, IOException {
        startBot();
    }
}
