import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class bot extends ListenerAdapter {
    public static void startBot() throws LoginException {
        JDA api = new JDABuilder("NjQ0NzQyNzY1Njc4MTY2MDI2.XdjaLQ.u1iuCgPLf9CDu5jgb-EgKeJAsv8").build();
        MyListener listener = new MyListener();
        api.addEventListener(listener);
    }

    public static void main(String[] args) throws LoginException {
        startBot();
    }
}