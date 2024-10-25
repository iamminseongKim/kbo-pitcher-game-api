package kms.kbopitcherapi.api.controller.csv;

import jakarta.validation.Valid;
import kms.kbopitcherapi.api.controller.ApiResponse;
import kms.kbopitcherapi.api.controller.csv.dto.request.CsvPitcherCreateFileDto;
import kms.kbopitcherapi.api.service.CsvService;
import kms.kbopitcherapi.api.service.PitcherCommendService;
import kms.kbopitcherapi.api.service.response.PlayerCsvProcessResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PitcherCsvInsertController {

    private final CsvService csvService;
    private final PitcherCommendService pitcherCommendService;

    @PostMapping("/api/v1/pitcher/csv/new")
    public ApiResponse<Object> insertCsvFile(@Valid @RequestBody CsvPitcherCreateFileDto request) {

        PlayerCsvProcessResult pitcherByCsv = csvService.createPitcherByCsv(request.getFullFilePath());
        return ApiResponse.ok(insertPitchers(pitcherByCsv));
    }

    private Map<String, Object> insertPitchers(PlayerCsvProcessResult pitcherByCsv) {
        int savedPitcherCount = pitcherCommendService.insertPitchers(pitcherByCsv.getSuccessList());
        int savedFailCount = pitcherByCsv.getFailedCount();
        int totalPitcherCsvCount = pitcherByCsv.getTotalCount();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "선수 등록 성공!");
        result.put("totalCount", totalPitcherCsvCount);
        result.put("savedPitcherCount", savedPitcherCount);
        result.put("savedFailCount", savedFailCount);
        return result;
    }

}
