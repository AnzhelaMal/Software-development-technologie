package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import ua.kpi.ia33.shellweb.service.SearchService;
import ua.kpi.ia33.shellweb.domain.SearchQuery;

public class SearchOp extends AbstractFsOperation {
    private final SearchService search;
    public SearchOp(SearchService search) { this.search = search; }

    @Override
    public void execute(OperationContext ctx) throws Exception {
        if (user == null) throw new IllegalStateException("User is required for SEARCH");
        SearchQuery q = search.createQuery(user, nameMask, ext);
        ctx.setProgress(100);
        ctx.setMessage("Пошук завершено: запит #" + q.getId());
    }
}
