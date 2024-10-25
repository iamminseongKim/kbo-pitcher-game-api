package kms.kbopitcherapi.api.service.response;

import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayerCsvProcessResult {

    private int totalCount;
    private List<PlayerCommendServiceRequest> successList;
    private List<String> failedLines;

    @Builder
    private PlayerCsvProcessResult(int totalCount, List<PlayerCommendServiceRequest> successList, List<String> failedLines) {
        this.totalCount = totalCount;
        this.successList = successList;
        this.failedLines = failedLines;
    }

    public int getSuccessCount() {
        return successList.size();
    }

    public int getFailedCount() {
        return failedLines.size();
    }
}
