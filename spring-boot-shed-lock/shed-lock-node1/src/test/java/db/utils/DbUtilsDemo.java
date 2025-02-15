package db.utils;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbUtilsDemo {
    public static void main(String[] args) throws Exception {        // 准备数据库连接
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spring-study", "root", "NBfe@2099");
        // 创建QueryRunner对象
        QueryRunner runner = new QueryRunner();

        // 插入数据
        String insertSQL = "INSERT INTO dbUtilsDemo (id, name, money) VALUES (?, ?, ?)";
        int rows = runner.update(conn, insertSQL, 1, "Macle", 10000);
        int rows2 = runner.update(conn, insertSQL, 2, "Macle2", 20000);
        System.out.println("rows=" + rows + "; " + rows2);


        // 执行查询，一行代码搞定！
        String sql = "SELECT * FROM dbUtilsDemo WHERE id = ?";
        User user = runner.query(
                conn,
                sql, new BeanHandler<>(User.class), 1
        );
        System.out.println(user);

        // 查询多条记录
        String sql3 = "SELECT * FROM dbUtilsDemo where id > ?";
        List<User> users = runner.query(
                conn,
                sql3,    new BeanListHandler<>(User.class), 1
        );

        // 自定义结果集处理
        ResultSetHandler<String> handler = rs -> {
            if (rs.next()) {
                return rs.getString("name");
            }
            return null;
        };
        String name = runner.query(conn, "SELECT name FROM dbUtilsDemo WHERE id = ?", handler, 1);

        //使用ScalarHandler：当你只需要查询单个值时
        Long count = runner.query(
                conn,    "SELECT COUNT(*) FROM dbUtilsDemo where id = ?", new ScalarHandler<Long>(), 2
        );

        //事务处理
        conn.setAutoCommit(false);
        try {
            runner.update(conn, "UPDATE dbUtilsDemo SET money = money - 100 WHERE id = ?", 1);
            runner.update(conn, "UPDATE dbUtilsDemo SET money = money + 100 WHERE id = ?", 2);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();    throw e;
        }

        conn.close();

    }
}