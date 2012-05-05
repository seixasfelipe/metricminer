package org.metricminer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.metricminer.model.Query;
import org.metricminer.model.QueryExecutor;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;

@Resource
public class QueryExecutorController {
    
    private final QueryExecutor queryExecutor;
    
    public QueryExecutorController(QueryExecutor queryExecutor, 
            HttpServletResponse response, Result result) {
        this.queryExecutor = queryExecutor;
    }
    
    @Get("/query")
    public void queryForm() {
    }
    
    @Post("/query")
    public Download execute(Query query) throws IOException {
        String tmpFileName = "/tmp/result" + new Random().nextLong() + ".csv";
        FileOutputStream outputStream = new FileOutputStream(tmpFileName);
        queryExecutor.execute(query, outputStream);
        return new FileDownload(new File(tmpFileName), "text/csv", "result.csv");
    }
    
    

    
}
