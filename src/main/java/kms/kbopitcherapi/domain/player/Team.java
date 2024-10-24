package kms.kbopitcherapi.domain.player;

import kms.kbopitcherapi.api.controller.csv.exception.NotFoundAtMakePlayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Team {

    HT("KIA"),
    SS("삼성"),
    LG("LG"),
    OB("두산"),
    KT("KT"),
    SK("SSG"),
    LT("롯데"),
    HH("한화"),
    NC("NC"),
    WO("키움"),
    ;

    private final String teamName;

    public static Team findTeam(String team) {
        for (Team teamCode : Team.values()) {
            if (teamCode.name().equals(team)) {
                return teamCode;
            }
        }
        throw new NotFoundAtMakePlayerException("해당 팀 코드는 잘못됬습니다. teamCode= " + team);
    }
}
