import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import java.util.stream.Stream;
import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HotelBookingTest {
    HotelBookingPage hotelBookingPage = new HotelBookingPage();

    @BeforeAll
    public static void setup() {
        Configuration.headless = false;
        Selenide.open("http://hotel-test.equalexperts.io/");
    }

    @AfterAll
    public static void cleanup() {
        Selenide.closeWebDriver();
    }

    @Test
    public void addBooking() {
        //given
        HotelBookingRowPage reservation = getRandomReservation();

        //when
        hotelBookingPage.addRow(reservation);

        //then
        HotelBookingRowPage newlyCreatedRow = hotelBookingPage.getRefreshedReservationsList().stream()
            .filter(row -> row.getFirstname().equals(reservation.getFirstname()))
            .findFirst().get();

        Assertions.assertThat(newlyCreatedRow)
            .usingRecursiveComparison()
            .ignoringFields("deleteButton")
            .isEqualTo(reservation);
    }

    @Test
    public void deleteBooking() {
        //given
        HotelBookingRowPage reservation = getRandomReservation();
        hotelBookingPage.addRow(reservation);

        //when
        hotelBookingPage.deleteRowByFirstname(reservation.getFirstname());

        //then
        Assertions.assertThat(hotelBookingPage.getRefreshedReservationsList())
            .extracting(HotelBookingRowPage::getFirstname)
            .doesNotContain(reservation.getFirstname());
    }

    @ParameterizedTest
    @MethodSource
    public void addBookingValidation(HotelBookingRowPage reservation) {
        //given
        HotelBookingRowPage hotelReservation = reservation;

        //when
        hotelBookingPage.addRow(hotelReservation);

        //then
        Assertions.assertThat(hotelBookingPage.getRefreshedReservationsList())
            .extracting(HotelBookingRowPage::getFirstname)
            .doesNotContain(reservation.getFirstname());
    }

    private HotelBookingRowPage getRandomReservation() {
        return HotelBookingRowPage.builder().firstname(RandomStringUtils.randomAlphabetic(10)).surname(RandomStringUtils.randomAlphabetic(10))
            .deposit(true).price(12.0).checkIn("2021-12-08").checkOut("2021-12-08").build();
    }


    static Stream<Arguments> addBookingValidation() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        return Stream.of(
            Arguments.of(HotelBookingRowPage.builder().firstname("")
                .surname(randomString).price(10.0).deposit(true).checkIn("2021-12-08").checkOut("2021-12-08").build()),
            Arguments.of(HotelBookingRowPage.builder().firstname(randomString)
                .surname("").price(10.0).deposit(true).checkIn("2021-12-08").checkOut("2021-12-08").build()),
            Arguments.of(HotelBookingRowPage.builder().firstname(randomString)
                .surname(randomString).price(10.0).deposit(true).checkIn("").checkOut("2021-12-08").build()),
            Arguments.of(HotelBookingRowPage.builder().firstname(randomString)
                .surname(randomString).price(10.0).deposit(true).checkIn("2021-12-08").checkOut("").build())
        );
    }
}
