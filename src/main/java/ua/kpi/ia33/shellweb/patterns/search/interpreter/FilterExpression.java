package ua.kpi.ia33.shellweb.patterns.search.interpreter;

import ua.kpi.ia33.shellweb.domain.SearchResult;

/**
 * Абстрактний вираз фільтрації результату пошуку.
 */
public interface FilterExpression {
    boolean matches(SearchResult file);
}
