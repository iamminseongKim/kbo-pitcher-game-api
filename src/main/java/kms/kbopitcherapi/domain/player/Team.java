package kms.kbopitcherapi.domain.player;

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

}
