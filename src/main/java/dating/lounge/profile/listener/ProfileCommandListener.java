package dating.lounge.profile.listener;

import dating.lounge.profile.entity.Profile;
import dating.lounge.profile.repository.ProfileRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;

@Component
public class ProfileCommandListener extends ListenerAdapter {

    private final ProfileRepository profileRepository;

    public ProfileCommandListener(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("setprofile")) {
            updateProfile(event); // Handles profile updates
        } else if (event.getName().equals("profile")) {
            String userId = event.getUser().getId();
            Optional<Profile> profileOpt = profileRepository.findByUserId(Long.valueOf(userId));

            Member member = event.getMember();
            if (member == null) {
                event.reply("Unable to retrieve member information.").queue();
                return;
            }

            Profile profile;
            if (profileOpt.isPresent()) {
                profile = profileOpt.get();
            } else {
                event.reply("You haven't set your profile yet! Use /setprofile to set it.").queue();
                return;
            }

            profile.setAge(getRoleByCategory(member, "18+", "22+", "26+", "30+"));
            profile.setGender(getRoleByCategory(member, "Female", "Male", "Trans-Female", "Trans-Male", "Non-Binary", "Genderfluid"));
            profile.setOrientation(getRoleByCategory(member, "Heterosexual", "Bisexual", "Homosexual", "Lesbian", "Pansexual", "Demisexual", "Asexual", "Bicurious", "Aromantic"));
            profile.setLocation(getRoleByCategory(member, "North America", "South America", "Europe", "Middle East", "Asia", "Africa", "Oceania"));
            profile.setDmStatus(getRoleByCategory(member, "DMs Open", "DMs Closed", "DMs Ask"));
            profile.setVerificationStatus(getRoleByCategory(member, "[F] Verified 18+", "[M] Verified 18+", "[T-F] Verified 18+", "[T-M] Verified 18+", "[NB] Verified 18+", "[G] Verified 18+"));
            profile.setLookingFor(getRoleByCategory(member, "Searching | Relationship", "Searching | Friends", "Not Searching"));

            profileRepository.save(profile);

            sendProfileEmbed(event, profile);
        }
    }

    private void updateProfile(SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        Member member = event.getMember();

        if (member == null) {
            event.reply("Unable to retrieve member information.").queue();
            return;
        }

        OptionMapping nameOption = event.getOption("name");
        OptionMapping hobbiesOption = event.getOption("hobbies");
        OptionMapping aboutMeOption = event.getOption("aboutme");

        Optional<Profile> existingProfileOpt = profileRepository.findByUserId(Long.valueOf(userId));
        Profile profile;

        if (existingProfileOpt.isPresent()) {
            profile = existingProfileOpt.get();
        } else {
            profile = new Profile();
            profile.setUserId(Long.valueOf(userId));
        }

        profile.setName(nameOption != null ? nameOption.getAsString() : profile.getName());
        profile.setHobbies(hobbiesOption != null ? hobbiesOption.getAsString() : profile.getHobbies());
        profile.setAboutMe(aboutMeOption != null ? aboutMeOption.getAsString() : profile.getAboutMe());

        profileRepository.save(profile);

        event.reply("Your profile has been updated!").queue();
    }

    private void sendProfileEmbed(SlashCommandInteractionEvent event, Profile profile) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Profile Information");
        embedBuilder.setColor(Color.BLACK);

        embedBuilder.addField("Name", profile.getName(), true);
        embedBuilder.addField("Age", profile.getAge(), true);
        embedBuilder.addField("Gender", profile.getGender(), true);
        embedBuilder.addField("Orientation", profile.getOrientation(), true);
        embedBuilder.addField("Location", profile.getLocation(), true);
        embedBuilder.addField("DM Status", profile.getDmStatus(), true);
        embedBuilder.addField("Verification Status", profile.getVerificationStatus(), true);
        embedBuilder.addField("Looking For", profile.getLookingFor(), true);
        embedBuilder.addField("", "", true);
        embedBuilder.addField("Hobbies", profile.getHobbies(), false);
        embedBuilder.addField("About Me", profile.getAboutMe(), false);

        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        embedBuilder.setThumbnail(avatarUrl);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private String getRoleByCategory(Member member, String... roles) {
        for (String roleName : roles) {
            if (member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName))) {
                return roleName;
            }
        }
        return "N/A"; // Default if no role is found
    }
}
