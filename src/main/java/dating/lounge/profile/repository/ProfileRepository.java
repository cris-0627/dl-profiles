package dating.lounge.profile.repository;

import dating.lounge.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
        Optional<Profile> findByUserId(Long userId);
}
