package controller;

import model.Query;
import model.QueryExecutor;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
public class QueryExecutorController {
    
    private final QueryExecutor queryExecutor;

    public QueryExecutorController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }
    
    @Get("/query")
    public void queryForm() {
    }
    
    @Post("/query")
    public void execute(Query query) {
        queryExecutor.execute(query, System.out);
    }
    
    

    
}
