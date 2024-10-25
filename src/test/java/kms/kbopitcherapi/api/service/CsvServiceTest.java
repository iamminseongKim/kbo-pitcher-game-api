package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.exception.NotFoundAtMakePlayerException;
import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import kms.kbopitcherapi.api.service.response.PlayerCsvProcessResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvServiceTest {

    @Test
    @DisplayName("csv파일을 읽어서 선수 생성용 dto List를 반환한다.")
    void createPitcherByCsv() throws Exception {
        //given
        String filePath = new ClassPathResource("test.csv").getFile().getAbsolutePath();
        CsvService csvService = new CsvService();

        //when
        PlayerCsvProcessResult pitcherByCsv = csvService.createPitcherByCsv(filePath);

        //then
        assertThat(pitcherByCsv.getSuccessList()).hasSize(2)
                .extracting("name").containsExactlyInAnyOrder("구대성","김서현");
    }

    @Test
    @DisplayName("순서나 정보가 잘못된 선수 정보를 읽으면 그 사람은 건너뛰고 다음 사람을 읽는다.")
    void notCreatePitcherByErrorCsv() throws Exception {
        //given
        String filePath = new ClassPathResource("testError.csv").getFile().getAbsolutePath();
        CsvService csvService = new CsvService();

        //when
        List<PlayerCommendServiceRequest> pitcherByCsv = csvService.createPitcherByCsv(filePath).getSuccessList();

        //then
        assertThat(pitcherByCsv).hasSize(1)
                .extracting("name").containsExactlyInAnyOrder("김서현");

    }

    @Test
    @DisplayName("없는 파일을 읽으면 예외를 반환한다.")
    void notCreatePitcherByEmptyCsvFile() throws Exception {
        //given
        String filePath = "/data/no.csv";
        CsvService csvService = new CsvService();

        //when
        assertThatThrownBy(() -> csvService.createPitcherByCsv(filePath))
                .isInstanceOf(NotFoundAtMakePlayerException.class);

    }
}