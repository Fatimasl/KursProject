package ru.netology.bank.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner queryRunner = new QueryRunner();

    private SQLHelper(){

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), System.getProperty("db.user"), System.getProperty("db.password"));
    }

    @SneakyThrows
    public static String getNotesInDB() {
        var conn = getConnection();
        //удалим все записи о транзакциях, которые были сделаны ранее, чем 5 секунд назад
        //queryRunner.execute(conn, "DELETE FROM order_entity WHERE created < NOW() - INTERVAL 3 SECONDS");

        //выберем последнюю транзакцию в списке и запомним значение payment_id
        var codeSQL = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        var payment_id = queryRunner.query(conn, codeSQL, new ScalarHandler<String>());
        //(conn, codeSQL, new ScalarHandler<String>());

        //найдем по payment_id в таблице оплат запись о сумме оплаты и о карте, которая была использована
        codeSQL = "SELECT status FROM payment_entity where transaction_id = " + "'" + payment_id + "'";
        var notes = queryRunner.query(conn, codeSQL, new ScalarHandler<String>());
        return notes;
    }

//    @SneakyThrows
//    public static void cleanDatabase(){
//        var conn = getConnection();
//        queryRunner.execute(conn, "DELETE FROM auth_codes");
//        queryRunner.execute(conn, "DELETE FROM card_transactions");
//        queryRunner.execute(conn, "DELETE FROM cards");
//        queryRunner.execute(conn, "DELETE FROM users");
//    }
//
//    @SneakyThrows
//    public static void cleanAuthCodes(){
//        var conn = getConnection();
//        queryRunner.execute(conn, "DELETE FROM auth_codes");
//    }
}
