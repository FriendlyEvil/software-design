package ru.akirakozov.sd.refactoring.writer;

public class HtmlBuilder {
    private final StringBuilder builder = new StringBuilder();
    private String startTags = "<html><body>\n";
    private String endTags = "</body></html>\n";

    public void appendText(String str) {
        builder.append(str);
        builder.append('\n');
    }

    public void appendH1Tag(String str) {
        builder.append("<h1>");
        builder.append(str);
        builder.append("</h1>\n");
    }

    public void clearStartAndEnd() {
        startTags = "";
        endTags = "";
    }

    public String build() {
        return startTags + builder.toString() + endTags;
    }

    @Override
    public String toString() {
        return build();
    }
}
