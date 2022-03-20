import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HotelBookingPage {
    private SelenideElement firstname = $(byId("firstname"));
    private SelenideElement surname = $(byId("lastname"));
    private SelenideElement price = $(byId("totalprice"));
    private Select deposit = new Select($(byId("depositpaid")));
    private SelenideElement checkIn = $(byId("checkin"));
    private SelenideElement checkOut = $(byId("checkout"));
    private SelenideElement saveButton = $(byAttribute("value", " Save "));
    private List<SelenideElement> rowListOnPage;
    private List<HotelBookingRowPage> reservationsList = new ArrayList<>();

    public List<HotelBookingRowPage> getRefreshedReservationsList() {
        rowListOnPage = $$(byCssSelector("#bookings > .row")).should(CollectionCondition.sizeGreaterThan(1));
        rowListOnPage = removeRowWithHeaders();
        reservationsList.clear();
        for (SelenideElement row : rowListOnPage) {
            reservationsList.add(new HotelBookingRowPage(row));
        }
        return reservationsList;
    }

    public void addRow(HotelBookingRowPage row) {
        firstname.setValue(row.getFirstname());
        surname.setValue(row.getSurname());
        price.setValue(row.getPrice().toString());
        deposit.selectByVisibleText(String.valueOf(row.isDeposit()));
        checkIn.setValue(row.getCheckIn());
        checkOut.setValue(row.getCheckOut());
        saveButton.click();
        Selenide.refresh();
        getRefreshedReservationsList();
    }

    public void deleteRowByFirstname(String firstname) {
        reservationsList.stream()
                .filter(r -> r.getFirstname().equals(firstname))
                .findFirst()
                .get()
                .deleteRow();
        Selenide.refresh();
        getRefreshedReservationsList();
    }

    private List<SelenideElement> removeRowWithHeaders() {
        return rowListOnPage.stream().skip(1).collect(Collectors.toList());
    }
}
