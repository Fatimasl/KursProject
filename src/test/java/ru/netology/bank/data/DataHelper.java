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
    private DataHelper() {}
    private static Faker faker = new Faker(new Locale("en"));

    @Value
    public static class CardId {
        private String cardName;
        private String cardId;
    }

    public static CardId getCardId1() {
        return new CardId("APPROVED","1111 2222 3333 4444");
    }

    public static CardId getCardId2() {
        return new CardId("DECLINED","5555 6666 7777 8888");
    }

    public static CardId getCardIdNotRight() {
        return new CardId("ERRORCARD","9999 9999 9999 9999");
    }

    public static String generateYear(int shift) {
        LocalDate varDate = LocalDate.now().plusYears(shift);
        String yearTwoSymbols = varDate.format(DateTimeFormatter.ofPattern("uu"));
        return yearTwoSymbols;
    }

    //возвращает псевдослучайное целое число - номер месяца -  с лидирующими нулями
    public static String generateMonth() {
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
    }

    public static String generateName() {
        String name = faker.name().lastName();
        return name;
    }

    public static String  generateRandomCVCCode(){
        return (faker.numerify("###"));
    }

    //выбирает случайную карту из коллекции карт
    public static DataHelper.CardId getRandomCard(ArrayList<CardId> cards) {
        Collections.shuffle(cards);
        return cards.get(0);
    }
}
