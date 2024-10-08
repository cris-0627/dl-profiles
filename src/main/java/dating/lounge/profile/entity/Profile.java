package dating.lounge.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "orientation")
    private String orientation;

    @Column(name = "location")
    private String location;

    @Column(name = "dm_status")
    private String dmStatus;

    @Column(name = "verification_status")
    private String verificationStatus;

    @Column(name = "looking_for")
    private String lookingFor;

    @Column(name = "hobbies")
    private String hobbies;

    @Column(name = "about_me")
    private String aboutMe;
}
