package ru.netology.bank.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;
import ru.netology.bank.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class EntryPage {
    private SelenideElement heading = $("[id=root] .heading");
    private SelenideElement paymentButton = $(byText("Купить"));

    public EntryPage() {
        heading.shouldBe(visible);
        heading.shouldHave(text("Путешествие дня"));
    }

    public PaymentPage paymentClick() {
        paymentButton.click();
        return new PaymentPage();
    }
}
