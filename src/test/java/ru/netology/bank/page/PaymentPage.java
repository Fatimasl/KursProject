package ru.netology.bank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.netology.bank.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private SelenideElement heading = $(byText("Оплата по карте"));
    private ElementsCollection arrayInputElements = $$(".input__control");
    private SelenideElement paymentButton = $(byText("Продолжить"));
    private SelenideElement notificationTitle = $(".notification__title");
    private SelenideElement notificationContent = $(".notification__content");
    //WebDriver driver = new ChromeDriver();
    //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public PaymentPage () {
        heading.shouldBe(visible);
    }

    public void validPayment(String cardID, int shift) {
        arrayInputElements.get(0).setValue(cardID);
        arrayInputElements.get(1).setValue(DataHelper.generateMonth());
        arrayInputElements.get(2).setValue(DataHelper.generateYear(shift));
        arrayInputElements.get(3).setValue(DataHelper.generateName());
        arrayInputElements.get(4).setValue(DataHelper.generateRandomCVCCode());
        paymentButton.click();
    }

    public void haveSuccess(String title, String content){

        notificationTitle.shouldHave(text(title));
        notificationTitle.shouldBe(visible);//, Duration.ofSeconds(5));
        notificationContent.shouldHave(text(content));
        notificationContent.shouldBe(visible);//, Duration.ofSeconds(5));
    }
}
