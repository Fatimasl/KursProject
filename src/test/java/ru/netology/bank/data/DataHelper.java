package ru.netology.bank.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRU = new Faker(new Locale("ru"));

    @Value
    public static class CardId {
        private String cardName;
        private String cardId;
    }

    public static CardId getCardId1() {
        return new CardId("APPROVED", "1111 2222 3333 4444");
    }

    public static CardId getCardId2() {
        return new CardId("DECLINED", "5555 6666 7777 8888");
    }

    public static CardId getCardIdNotRight() {
        return new CardId("ERRORCARD", "9999 9999 9999 9999");
    }

    public static String generateYear(int shift) {
        if (shift == -8) {
            return "8";
        } else if (shift == -9) {
            return "";
        } else {
            LocalDate varDate = LocalDate.now().plusYears(shift);
            String yearTwoSymbols = varDate.format(DateTimeFormatter.ofPattern("uu"));
            return yearTwoSymbols;
        }
    }

    public static String generateMonth(int sinceThisMonth) {
        //если sinceThisMonth = -13, то это означает, что вернуть пустую строку
        //если sinceThisMonth = -14, то это означает, что вернуть строку из одной цифры
        //если sinceThisMonth = 0, то это означает, что надо вернуть надо любой месяц
        //если в sinceThisMonth передадут номер текущего месяца, то надо вернуть любой месяц, начиная с текущего
        //если в sinceThisMonth передадут отрицательный номер текущего месяца, то надо вернуть любой месяц, до текущего
        //в противном случае возвращаем не существующий номер месяца из 2-х цифр

        if (sinceThisMonth == 0) {
            String[] arrayOfMonths = {
                    "01",
                    "02",
                    "03",
                    "04",
                    "05",
                    "06",
                    "07",
                    "08",
                    "09",
                    "10",
                    "11",
                    "12"
            };

            Random r = new Random();
            String MonthTwoSymbols = arrayOfMonths[r.nextInt(11)];
            return MonthTwoSymbols;

        } else if (sinceThisMonth == -13) {
            return "";
        } else if (sinceThisMonth == -14) {
            return "8";
        } else if (sinceThisMonth > 0 & sinceThisMonth < 13) {
            int myNumeric = 12 - sinceThisMonth;
            myNumeric = (int) (Math.random() * myNumeric) + 1 + sinceThisMonth;
            if (myNumeric / 10 < 1) {
                return "0" + myNumeric;
            } else {
                return String.valueOf(myNumeric);
            }
        } else if (sinceThisMonth < 0 & sinceThisMonth > -13) {
            sinceThisMonth = sinceThisMonth * (-1);
            int myNumeric = 12 - sinceThisMonth;
            myNumeric = (int) (Math.random() * myNumeric) + 1;
            if (myNumeric / 10 < 1) {
                return "0" + myNumeric;
            } else {
                return String.valueOf(myNumeric);
            }
        }

        return "15";
    }

    public static String generateName(String whatAboutName) {
        String name = "";

        if (whatAboutName == "empty") {

        } else if (whatAboutName == "ru") {
            name = fakerRU.name().lastName();
        } else {
            name = faker.name().lastName();
        }

        return name;
    }

    public static String generateRandomCVCCode(String whatAboutCVC) {
        String cvc = "";
        if (whatAboutCVC == "empty") {

        } else if (whatAboutCVC == "one symbol") {
            cvc = "1";
        } else {
            cvc = faker.numerify("###");
        }
        return cvc;
    }

    //выбирает случайную карту из коллекции карт
    public static DataHelper.CardId getRandomCard() {
        ArrayList<DataHelper.CardId> cards = new ArrayList<DataHelper.CardId>();
        cards.add(getCardId1());
        cards.add(getCardId2());
        Collections.shuffle(cards);
        return cards.get(0);
    }

}
