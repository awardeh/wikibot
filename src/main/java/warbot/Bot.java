package warbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {

    private static void startBot() throws LoginException {
        JDA api = new JDABuilder("NjQ0NzQyNzY1Njc4MTY2MDI2.XeUpgg.5SkpBxlRP-JUdKVshf_sKJl00FY").build();
        MyListener listener = new MyListener();
        api.addEventListener(listener);
    }

    public static void main(String[] args) throws LoginException {
        startBot();
    }
}
