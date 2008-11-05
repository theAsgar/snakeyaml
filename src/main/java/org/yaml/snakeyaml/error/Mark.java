package org.yaml.snakeyaml.error;

/**
 * It's just a record and its only use is producing nice error messages. Parser
 * does not use it for any other purposes.
 * 
 * @see imported from PyYAML
 */
public class Mark {
    private String name;
    private int line;
    private int column;
    private String buffer;
    private int pointer;

    public Mark(String name, int index, int line, int column, String buffer, int pointer) {
        super();
        this.name = name;
        this.line = line;
        this.column = column;
        this.buffer = buffer;
        this.pointer = pointer;
    }

    private boolean isLineBreak(char ch) {
        switch (ch) {
        case '\0':
            return true;
        case '\r':
            return true;
        case '\n':
            return true;
        case '\u0085':
            return true;
        case '\u2028':
            return true;
        case '\u2029':
            return true;
        }
        return false;
    }

    public String get_snippet(int indent, int max_length) {
        if (buffer == null) {
            return null;
        }
        float half = max_length / 2 - 1;
        int start = pointer;
        String head = "";
        while ((start > 0) && (!isLineBreak(buffer.charAt(start - 1)))) {
            start -= 1;
            if (pointer - start > half) {
                head = " ... ";
                start += 5;
                break;
            }
        }
        String tail = "";
        int end = pointer;
        while ((end < buffer.length()) && (!isLineBreak(buffer.charAt(end)))) {
            end += 1;
            if (end - pointer > half) {
                tail = " ... ";
                end -= 5;
                break;
            }
        }
        String snippet = buffer.substring(start, end);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < indent; i++) {
            result.append(" ");
        }
        result.append(head);
        result.append(snippet);
        result.append(tail);
        result.append("\n");
        for (int i = 0; i < indent + pointer - start + head.length(); i++) {
            result.append(" ");
        }
        result.append("^");
        return result.toString();
    }

    public String get_snippet() {
        return get_snippet(4, 75);
    }

    @Override
    public String toString() {
        String snippet = get_snippet();
        StringBuffer where = new StringBuffer(" in \"");
        where.append(name);
        where.append("\", line ");
        where.append(line);
        where.append(", column ");
        where.append(column);
        if (snippet != null) {
            where.append(":\n");
            where.append(snippet);
        }
        return where.toString();
    }
}