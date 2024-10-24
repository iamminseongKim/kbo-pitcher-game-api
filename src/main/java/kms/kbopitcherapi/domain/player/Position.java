package kms.kbopitcherapi.domain.player;

import kms.kbopitcherapi.api.controller.csv.exception.NotFoundAtMakePlayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

    SP("선발투수"),
    RP("구원투수"),
    ;

    private final String description;

    public static Position findPosition(String position) {
        for (Position positionName : Position.values()) {
            if (positionName.name().equalsIgnoreCase(position)) {
                return positionName;
            }
        }
        throw new NotFoundAtMakePlayerException("해당 포지션을 찾을 수 없습니다. position = " + position);
    }
}
