package kms.kbopitcherapi.domain.player;

import kms.kbopitcherapi.api.exception.NotFoundAtMakePlayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Team {

    HT("KIA 타이거즈"),
    SS("삼성 라이온즈"),
    LG("LG 트윈스"),
    OB("두산 베어스"),
    KT("KT 위즈"),
    SK("SSG 랜더스"),
    LT("롯데 자이언츠"),
    HH("한화 이글스"),
    NC("NC 다이노스"),
    WO("키움 히어로즈"),
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
