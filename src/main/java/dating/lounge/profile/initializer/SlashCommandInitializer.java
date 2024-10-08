package dating.lounge.profile.initializer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SlashCommandInitializer {
    public static void registerCommands(JDA jda) {
        jda.upsertCommand("setprofile", "Set your profile details")
                .addOption(OptionType.STRING, "name", "Your name", true) // Custom field
                .addOption(OptionType.STRING, "hobbies", "Your hobbies", true) // Custom field
                .addOption(OptionType.STRING, "aboutme", "About you", true) // Custom field
                .queue();

        jda.upsertCommand("profile", "View your profile").queue();
    }
}
