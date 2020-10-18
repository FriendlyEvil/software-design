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

    @Override
    public List<Product> selectAll() {
        List<List<String>> select = selectSql("SELECT NAME, PRICE FROM PRODUCT", List.of("NAME", "PRICE"));
        return select.stream().map(this::parseProduct).collect(Collectors.toList());
    }

    @Override
    public void doInsert(List<Product> objs) {
        StringBuilder builder = new StringBuilder("INSERT INTO PRODUCT(NAME, PRICE) VALUES ");
        for (int i = 0; i < objs.size() - 1; i++) {
            builder.append(objs.get(i).toSqlData());
            builder.append(", ");
        }
        builder.append(objs.get(objs.size() - 1).toSqlData());
        execSql(builder.toString());
    }
}
