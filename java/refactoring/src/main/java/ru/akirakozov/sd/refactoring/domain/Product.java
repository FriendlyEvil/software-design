package ru.akirakozov.sd.refactoring.domain;

public class Product {
    private final String name;
    private final long price;
    private static final String SQL_TEMPLATE = "('%s', %d)";
    private static final String HTML_TEMPLATE = "%s\t%d</br>";

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String toHtml() {
        return String.format(HTML_TEMPLATE, name, price);
    }

    public String toSqlData() {
        return String.format(SQL_TEMPLATE, name, price);
    }
}
