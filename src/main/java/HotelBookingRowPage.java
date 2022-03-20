import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HotelBookingRowPage {
    private String firstname;
    private String surname;
    private Double price;
    private boolean deposit;
    private String checkIn;
    private String checkOut;
    private SelenideElement deleteButton;

    public HotelBookingRowPage(SelenideElement row) {
        ElementsCollection cells = row.$$("div");
        this.firstname = cells.get(0).getText();
        this.surname = cells.get(1).getText();
        this.price = Double.valueOf(cells.get(2).getText());
        this.deposit = Boolean.valueOf(cells.get(3).getText());
        this.checkIn = cells.get(4).getText();
        this.checkOut = cells.get(5).getText();
        this.deleteButton = row.$("input");
    }

    public void deleteRow() {
        deleteButton.click();
    }
}
