package kms.kbopitcherapi.api.controller.csv;

import jakarta.validation.Valid;
import kms.kbopitcherapi.api.controller.ApiResponse;
import kms.kbopitcherapi.api.controller.csv.dto.request.CsvPitcherCreateFileDto;
import kms.kbopitcherapi.api.service.CsvService;
import kms.kbopitcherapi.api.service.PitcherCommendService;
import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PitcherCsvInsertController {

    private final CsvService csvService;
    private final PitcherCommendService pitcherCommendService;

    @PostMapping("/api/v1/pitcher/csv/new")
    public ApiResponse<Object> insertCsvFile(@Valid @RequestBody CsvPitcherCreateFileDto request) {
        List<PlayerCommendServiceRequest> pitchersByCsv = csvService.createPitcherByCsv(request.getFullFilePath());
        pitcherCommendService.insertPitchers(pitchersByCsv);
        return ApiResponse.ok("선수 등록 성공!");
    }

}
