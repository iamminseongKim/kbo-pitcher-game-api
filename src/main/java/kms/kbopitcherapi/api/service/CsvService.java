package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.controller.csv.dto.request.CsvPitcherCreateDto;
import kms.kbopitcherapi.api.exception.NotFoundAtMakePlayerException;
import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import kms.kbopitcherapi.api.service.response.PlayerCsvProcessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class CsvService {

    public PlayerCsvProcessResult createPitcherByCsv(String fullFilePath) {

        List<PlayerCommendServiceRequest> playerListByCsv = new ArrayList<>();
        List<String> failedLines = new ArrayList<>();
        int totalCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(new FileSystemResource(fullFilePath).getFile()))) {

            String line;

            br.readLine(); // 첫 번째 줄(헤더)은 건너뜀

            while ((line = br.readLine()) != null) {

                totalCount++;

                try {
                    String[] data = line.split(",");

                    CsvPitcherCreateDto csvPitcher = new CsvPitcherCreateDto(
                            data[0],    // team
                            data[1],    // name
                            data[2],    // originalFileName
                            data[3],    // quizFilename
                            data[4],    // position
                            data[5],    // birthDate
                            data[6]     // backNumber
                    );

                    playerListByCsv.add(csvPitcher.toPlayerCommendServiceRequest());

                } catch (NotFoundAtMakePlayerException e) {
                    // 개별 라인에서 오류가 발생하면 로그로 남기고 계속 진행
                    log.error("라인 처리 중 오류 발생: {}", line, e);
                    failedLines.add(line);
                }
            }

            return PlayerCsvProcessResult.builder()
                    .totalCount(totalCount)
                    .successList(playerListByCsv)
                    .failedLines(failedLines)
                    .build();

        } catch (IOException e) {
            log.error("파일 읽기를 실패했습니다.", e);
            throw new NotFoundAtMakePlayerException("파일 읽기를 실패했습니다. file : " + fullFilePath);
        }
    }
}
