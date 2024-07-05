package ru.netology.bank.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.bank.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private SelenideElement heading = $(byText("Оплата по карте"));
    private ElementsCollection arrayInputElements = $$(".input__control");
    private SelenideElement inputError = $(".input__sub");
    private SelenideElement paymentButton = $(byText("Продолжить"));
    private SelenideElement notificationTitleOK = $(".notification_status_ok .notification__title");
    private SelenideElement notificationContentOK = $(".notification_status_ok .notification__content");
    private SelenideElement notificationTitleError = $(".notification_status_error .notification__title");
    private SelenideElement notificationContentError = $(".notification_status_error .notification__content");
    private ElementsCollection listConditionElements = $$(".list__item");

    public PaymentPage() {
        heading.shouldBe(visible);
    }

    public void validPayment(String cardID, int shift, int thisMonth, String whatAboutName, String whatAboutCVC) {
        arrayInputElements.get(0).setValue(cardID);
        arrayInputElements.get(1).setValue(DataHelper.generateMonth(thisMonth));
        arrayInputElements.get(2).setValue(DataHelper.generateYear(shift));
        arrayInputElements.get(3).setValue(DataHelper.generateName(whatAboutName));
        arrayInputElements.get(4).setValue(DataHelper.generateRandomCVCCode(whatAboutCVC));
        paymentButton.click();
    }

    public void haveNotificationOk(String title, String content) {

        notificationTitleOK.shouldBe(visible, Duration.ofSeconds(15));//, Duration.ofSeconds(5));
        notificationContentOK.shouldBe(visible, Duration.ofSeconds(15));//, Duration.ofSeconds(5));
        notificationTitleOK.shouldHave(text(title));
        notificationContentOK.shouldHave(text(content));
    }

    public void haveNotificationError(String title, String content) {

        notificationTitleError.shouldBe(visible, Duration.ofSeconds(15));//, Duration.ofSeconds(5));
        notificationContentError.shouldBe(visible, Duration.ofSeconds(15));//, Duration.ofSeconds(5));
        notificationTitleError.shouldHave(text(title));
        notificationContentError.shouldHave(text(content));
    }

    public void shouldBeVisibleInputError(String content) {

        inputError.shouldBe(visible);
        inputError.shouldHave(text(content));
    }

    public int getJourneyAmount() {
        String strWithAmount = listConditionElements.get(listConditionElements.size() - 1).getText();
        String digits = strWithAmount.replaceAll("\\D+", "");
        int a = Integer.parseInt(digits);
        return a;
    }
}
