package ru.netology.bank.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
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
    }

    @Test
    @DisplayName("Successful payment by payment service (year of the future)")
    public void successfulEntry() throws InterruptedException{
        DataHelper.CardId сardForUse;

        PaymentPage paymentPage = entryPage.paymentClick();

        //коллекция из доступных карт
        ArrayList<DataHelper.CardId> cards = new ArrayList<DataHelper.CardId>();
        cards.add(DataHelper.getCardId1());
        cards.add(DataHelper.getCardId2());
        //случайно определим карту для оплаты
        сardForUse = DataHelper.getRandomCard(cards);
        paymentPage.validPayment(сardForUse.getCardId(), 1);

        var notes = SQLHelper.getNotesInDB();

        assertEquals(сardForUse.getCardName(), notes);
        //paymentPage.haveSuccess("Успешно", "Операция одобрена банком.");
    }
}
