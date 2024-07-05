package ru.netology.bank.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.netology.bank.data.DataHelper;
import ru.netology.bank.data.SQLHelper;
import ru.netology.bank.page.EntryPage;
import ru.netology.bank.page.PaymentPage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTests {
    EntryPage entryPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
        entryPage = new EntryPage();
        //удалим все записи о транзакциях оплаты
        SQLHelper.clean_order_entity();
    }

    @Test
    @DisplayName("Successful payment by payment service (year of the future), valid name of the card")
    //1.1. Успешная оплата тура через сервис платежей (будущий год и любой месяц), правильное имя карты списания
    public void successRedistedNameCardFutureYear() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 1, 0, "en", "not empty");

        while (SQLHelper.getPayment_idInDB() == null) {
            //ждем, когда в базе данных появится запись о транзакции оплаты
        }

        var actualNameOfCard = SQLHelper.getNameCardInDB();

        assertAll(() -> paymentPage.haveNotificationOk("Успешно", "Операция одобрена банком."),
                () -> assertEquals(сardForUse.getCardName(), actualNameOfCard));
    }

    @Test
    @DisplayName("Successful payment by payment service (this year), valid name of the card")
    //1.2. Успешная оплата тура через сервис платежей (текущий год и текущий/будущий месяц), правильное имя карты списания
    public void successRedistedNameCardThisYear() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();

        LocalDate varDate = LocalDate.now();
        int thisMonth = Integer.parseInt(varDate.format(DateTimeFormatter.ofPattern("MM")));

        paymentPage.validPayment(сardForUse.getCardId(), 0, thisMonth, "en", "not empty");

        while (SQLHelper.getPayment_idInDB() == null) {
            //ждем, когда в базе данных появится запись о транзакции оплаты
        }

        var actualNameOfCard = SQLHelper.getNameCardInDB();
        //System.out.println(actualNameOfCard);
        assertAll(() -> paymentPage.haveNotificationOk("Успешно", "Операция одобрена банком."),
                () -> assertEquals(сardForUse.getCardName(), actualNameOfCard));
    }

    @Test
    @DisplayName("invalid amount in DataBase. Successful payment by payment service (year of the future), ")
    //1.1.Доп. Успешная оплата тура через сервис платежей (будущий год и любой месяц), неправильная сумма списания с карты
    public void successRedistedNameCardFutureYearInvalidAmount() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 1, 0, "en", "not empty");

        while (SQLHelper.getPayment_idInDB() == null) {
            //ждем, когда в базе данных появится запись о транзакции оплаты
        }
        var expectedAmount = paymentPage.getJourneyAmount();
        var actualAmount = SQLHelper.getAmountInDB();
        System.out.println(expectedAmount);
        assertAll(() -> paymentPage.haveNotificationOk("Успешно", "Операция одобрена банком."),
                () -> assertEquals(expectedAmount, actualAmount));
    }

    @Test
    @DisplayName("unredisted card, year of the future - failed payment by payment service ")
    //1.3. Неуспешная оплата тура через сервис платежей (несуществующий номер карты правильного формата)
    public void unredistedNameCardFutureYearFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getCardIdNotRight();

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 1, 0, "en", "not empty");

        paymentPage.haveNotificationError("Ошибка", "Ошибка! Банк отказал в проведении операции.");

    }

    @Test
    @DisplayName("empty card, year of the future - failed payment by payment service")
    //1.4. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный формат номера карты - пустой)
    public void emptyNameCardFutureYearFailedPayment() {

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment("", 2, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("4-symbols cardID, year of the future - failed payment by payment service")
    //1.5. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный формат номера карты - длина номера карты 4 символа)
    public void fewSymbolsNameCardFutureYearFailedPayment() {

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment("1234", 2, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("empty year - failed payment by payment service")
    //1.6. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный год действия карты - пустой)
    public void emptyYearFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), -9, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("one symbol year - failed payment by payment service")
    //1.7. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный год действия карты - из одной цифры)
    public void oneSymbolYearFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), -8, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("past year - failed payment by payment service")
    //1.8. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный год действия карты - из двух цифр, но прошлый год)
    public void pastYearFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), -1, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Истёк срок действия карты");

    }

    @Test
    @DisplayName("very future year - failed payment by payment service")
    //1.9. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный год действия карты - из двух цифр, но более чем через 5 лет от текущего)
    public void veryFutureYearFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 6, 0, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверно указан срок действия карты");

    }

    @Test
    @DisplayName("empty month - failed payment by payment service")
    //1.10. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный месяц действия карты - пустой)
    public void emptyMonthFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 3, -13, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("one symbol month - failed payment by payment service")
    //1.11. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный месяц действия карты - из одной цифры)
    public void oneSymbolMonthFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 4, -14, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("non-existent month - failed payment by payment service")
    // 1.12. Неуспешное заполнение формы для оплаты тура через сервис платежей (неправильный месяц действия карты - из двух цифр, но НЕ валидные номера месяцев)
    public void nonExistentMonthFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 5, -15, "en", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверно указан срок действия карты");

    }

    @Test
    @DisplayName("last month - failed payment by payment service")
    // 1.13. Неуспешное заполнение формы для оплаты тура через сервис платежей (срок действия карты в текущем году и прошлом месяце)
    public void lastMonthFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        LocalDate varDate = LocalDate.now();
        int thisMonth = Integer.parseInt(varDate.format(DateTimeFormatter.ofPattern("MM")));
        int thisYear = 0;

        PaymentPage paymentPage = entryPage.paymentClick();

        if (thisMonth > 1) {
            thisMonth = thisMonth * (-1);
            thisYear = 0;
            paymentPage.validPayment(сardForUse.getCardId(), thisYear, thisMonth, "en", "not empty");
            paymentPage.shouldBeVisibleInputError("Неверно указан срок действия карты");
        } else if (thisMonth == 1) {
            thisYear = -1;
            thisMonth = 12;
            paymentPage.validPayment(сardForUse.getCardId(), thisYear, thisMonth, "en", "not empty");
            paymentPage.shouldBeVisibleInputError("Истёк срок действия карты");
        }
    }

    @Test
    @DisplayName("empty owner - failed payment by payment service")
    // 1.14. Неуспешное заполнение формы для оплаты тура через сервис платежей (не указан Владелец карты)
    public void emptyOwnerFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 5, 0, "empty", "not empty");

        paymentPage.shouldBeVisibleInputError("Поле обязательно для заполнения");

    }

    @Test
    @DisplayName("ru owner - failed payment by payment service")
    // 1.15. Неуспешное заполнение формы для оплаты тура через сервис платежей (Владелец карты указан не на латинице)
    public void ruOwnerFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 5, 0, "ru", "not empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("empty CVC - failed payment by payment service")
    // 1.16. Неуспешное заполнение формы для оплаты тура через сервис платежей (не указан CVC/CVV)
    public void emptyCVCFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 2, 0, "en", "empty");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

    @Test
    @DisplayName("one symbol CVC - failed payment by payment service")
    // 1.17. Неуспешное заполнение формы для оплаты тура через сервис платежей (указан CVC/CVV из одной цифры)
    public void oneSymbolCVCFailedPayment() {
        DataHelper.CardId сardForUse;
        сardForUse = DataHelper.getRandomCard();//случайно определим карту для оплаты

        PaymentPage paymentPage = entryPage.paymentClick();
        paymentPage.validPayment(сardForUse.getCardId(), 2, 0, "en", "one symbol");

        paymentPage.shouldBeVisibleInputError("Неверный формат");

    }

}
