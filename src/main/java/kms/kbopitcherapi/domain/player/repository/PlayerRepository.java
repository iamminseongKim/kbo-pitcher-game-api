package kms.kbopitcherapi.domain.player.repository;

import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByName(String name);

    List<Player> findByNameContaining(String name);

    Player findByNameAndTeamAndBirthDate(String name, Team team, LocalDate birthDate);

    @Query("select p from Player p join fetch p.playerFile pf order by rand() limit 1")
    Player findPlayerByRandom();
}
