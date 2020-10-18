package ru.akirakozov.sd.refactoring.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Database<T> {
    private final String databaseConnectionString;

    public Database(String databaseConnectionString) {
        this.databaseConnectionString = databaseConnectionString;
    }

    public abstract void createIfNotExist();

    public abstract void dropIfExist();

    public abstract List<T> selectAll();

    public void insert(T obj) {
        insert(List.of(obj));
    }

    public void insert(List<T> objs) {
        if (objs.size() == 0) {
            throw new IllegalArgumentException("count of object must be more that one");
        }
        doInsert(objs);
    }

    public abstract void doInsert(List<T> objs);


    protected List<List<String>> selectSql(String sql, List<String> fields) {
        List<List<String>> result = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(databaseConnectionString)) {
            try (Statement stmt = c.createStatement()) {
                try (ResultSet resultSet = stmt.executeQuery(sql)) {
                    while (resultSet.next()) {
                        List<String> tmp = new ArrayList<>();
                        for (String field : fields) {
                            tmp.add(resultSet.getString(field));
                        }
                        result.add(tmp);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected void execSql(String sql) {
        try (Connection c = DriverManager.getConnection(databaseConnectionString)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
