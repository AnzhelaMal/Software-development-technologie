package ua.kpi.ia33.shellweb.patterns.search.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Простий парсер розширеного виразу фільтрації.
 * Підтримка синтаксису:
 *   name:*report* AND (ext:pdf OR ext:docx) AND type:file
 */
public class FilterParser {

    public FilterExpression parse(String text) {
        List<String> tokens = tokenize(text);
        ParserState s = new ParserState(tokens);
        FilterExpression expr = parseOr(s);
        if (s.hasMore()) {
            throw new IllegalArgumentException("Зайві токени у виразі");
        }
        return expr;
    }

    // Expr   := OrExpr
    // OrExpr := AndExpr ('OR' AndExpr)*
    // AndExpr := Primary ('AND' Primary)*
    // Primary := Predicate | '(' OrExpr ')'

    private FilterExpression parseOr(ParserState s) {
        FilterExpression expr = parseAnd(s);
        while (s.peekIs("OR")) {
            s.consume();
            FilterExpression right = parseAnd(s);
            expr = new CompositeExpression(expr, LogicalOp.OR, right);
        }
        return expr;
    }

    private FilterExpression parseAnd(ParserState s) {
        FilterExpression expr = parsePrimary(s);
        while (s.peekIs("AND")) {
            s.consume();
            FilterExpression right = parsePrimary(s);
            expr = new CompositeExpression(expr, LogicalOp.AND, right);
        }
        return expr;
    }

    private FilterExpression parsePrimary(ParserState s) {
        if (s.peekIs("(")) {
            s.consume();
            FilterExpression inner = parseOr(s);
            s.expect(")");
            return inner;
        }
        return parsePredicate(s);
    }

    private FilterExpression parsePredicate(ParserState s) {
        String token = s.consume();
        String[] parts = token.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Невірний предикат: " + token);
        }
        String field = parts[0].trim();
        String value = parts[1].trim();
        return new SimpleExpression(field, value);
    }

    // ---------------- tokenize ----------------

    private List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                flush(tokens, current);
            } else if (ch == '(' || ch == ')') {
                flush(tokens, current);
                tokens.add(String.valueOf(ch));
            } else {
                current.append(ch);
            }
        }
        flush(tokens, current);
        return tokens;
    }

    private void flush(List<String> tokens, StringBuilder current) {
        if (current.length() > 0) {
            tokens.add(current.toString());
            current.setLength(0);
        }
    }

    // ---------------- parser state ----------------

    private static class ParserState {
        private final List<String> tokens;
        private int pos = 0;

        ParserState(List<String> tokens) {
            this.tokens = tokens;
        }

        boolean hasMore() {
            return pos < tokens.size();
        }

        String peek() {
            return hasMore() ? tokens.get(pos) : null;
        }

        boolean peekIs(String expected) {
            String p = peek();
            return p != null && p.equalsIgnoreCase(expected);
        }

        String consume() {
            if (!hasMore()) {
                throw new IllegalArgumentException("Неочікуваний кінець виразу");
            }
            return tokens.get(pos++);
        }

        void expect(String expected) {
            String actual = consume();
            if (!actual.equalsIgnoreCase(expected)) {
                throw new IllegalArgumentException(
                        "Очікував '" + expected + "', але отримав '" + actual + "'"
                );
            }
        }
    }
}
