package ru.netology.bank.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner queryRunner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), System.getProperty("db.user"), System.getProperty("db.password"));
    }

    @SneakyThrows
    public static String getPayment_idInDB() {
        var conn = getConnection();

        //выберем последнюю транзакцию в списке и запомним значение payment_id
        var codeSQL = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        String payment_id = queryRunner.query(conn, codeSQL, new ScalarHandler<String>());
        return payment_id;
    }

    @SneakyThrows
    public static String getNameCardInDB() {
        var conn = getConnection();

        //выберем последнюю транзакцию в списке и запомним значение payment_id
        var payment_id = getPayment_idInDB();//queryRunner.query(conn, codeSQL, new ScalarHandler<String>());

        //найдем по payment_id в таблице оплат запись о карте, которая была использована
        var codeSQL = "SELECT status FROM payment_entity where transaction_id = " + "'" + payment_id + "'";
        var nameOfCard = queryRunner.query(conn, codeSQL, new ScalarHandler<String>());
        return nameOfCard;
    }

    @SneakyThrows
    public static int getAmountInDB() {
        var conn = getConnection();

        //выберем последнюю транзакцию в списке и запомним значение payment_id
        var payment_id = getPayment_idInDB();//queryRunner.query(conn, codeSQL, new ScalarHandler<String>());

        //найдем по payment_id в таблице оплат запись о сумме оплаты, которая была списана
        var codeSQL = "SELECT amount FROM payment_entity where transaction_id = " + "'" + payment_id + "'";
        var amount = queryRunner.query(conn, codeSQL, new ScalarHandler<Integer>());
        return amount;
    }

    @SneakyThrows
    public static void clean_order_entity() {
        var conn = getConnection();
        queryRunner.execute(conn, "DELETE FROM order_entity");
    }
}
