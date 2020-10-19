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

    public abstract T max();

    public abstract T min();

    public abstract int sum();

    public abstract int count();

    public int insert(T obj) {
        return insert(List.of(obj));
    }

    public int insert(List<T> objs) {
        if (objs.size() == 0) {
            throw new IllegalArgumentException("count of object must be more that one");
        }
        return doInsert(objs);
    }

    public abstract int doInsert(List<T> objs);


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

    protected int execSqlIntAsResult(String sql) {
        try (Connection c = DriverManager.getConnection(databaseConnectionString)) {
            try (Statement stmt = c.createStatement()) {
                try (ResultSet resultSet = stmt.executeQuery(sql)) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    protected int execSql(String sql) {
        try (Connection c = DriverManager.getConnection(databaseConnectionString)) {
            try (Statement stmt = c.createStatement()) {
                return stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
