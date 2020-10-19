package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDatabase extends Database<Product> {

    public ProductDatabase(String databaseConnectionString) {
        super(databaseConnectionString);
    }

    @Override
    public void createIfNotExist() {
        execSql("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @Override
    public void dropIfExist() {
        execSql("DROP TABLE IF EXISTS PRODUCT");
    }

    private Product parseProduct(List<String> obj) {
        return new Product(obj.get(0), Long.parseLong(obj.get(1)));
    }

    public List<Product> select(String sql) {
        List<List<String>> select = selectSql(sql, List.of("NAME", "PRICE"));
        return select.stream().map(this::parseProduct).collect(Collectors.toList());
    }

    @Override
    public List<Product> selectAll() {
        return select("SELECT NAME, PRICE FROM PRODUCT");
    }

    @Override
    public Product max() {
        return select("SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE DESC LIMIT 1").stream().findFirst().orElse(null);
    }

    @Override
    public Product min() {
        return select("SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE LIMIT 1").stream().findFirst().orElse(null);
    }

    @Override
    public int sum() {
        return execSqlIntAsResult("SELECT SUM(price) FROM PRODUCT");
    }

    @Override
    public int count() {
        return execSqlIntAsResult("SELECT COUNT(*) FROM PRODUCT");
    }

    @Override
    public int doInsert(List<Product> objs) {
        StringBuilder builder = new StringBuilder("INSERT INTO PRODUCT(NAME, PRICE) VALUES ");
        for (int i = 0; i < objs.size() - 1; i++) {
            builder.append(objs.get(i).toSqlData());
            builder.append(", ");
        }
        builder.append(objs.get(objs.size() - 1).toSqlData());
        return execSql(builder.toString());
    }
}
