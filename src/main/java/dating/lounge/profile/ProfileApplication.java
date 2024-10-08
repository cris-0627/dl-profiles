package dating.lounge.profile;

import dating.lounge.profile.initializer.SlashCommandInitializer;
import dating.lounge.profile.listener.ProfileCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class ProfileApplication {

    public static void main(String[] args) throws LoginException {

        ApplicationContext context = SpringApplication.run(ProfileApplication.class, args);

        String token = "MTI5MzMwNjg3MDQ3OTcyMDQ4MA.GICQtG.Qgn8QasqGIby5fqNf9YS-Yi4JJ0CLNCGnCc_Hc";
        JDA jda = JDABuilder.createDefault(token).build();

        SlashCommandInitializer.registerCommands(jda);

        ProfileCommandListener profileCommandListener = context.getBean(ProfileCommandListener.class);

        jda.addEventListener(profileCommandListener);
    }
}
