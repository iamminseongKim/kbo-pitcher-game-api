package kms.kbopitcherapi.domain.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

    SP("선발투수"),
    RP("구원투수"),
    ;

    private final String description;
}
