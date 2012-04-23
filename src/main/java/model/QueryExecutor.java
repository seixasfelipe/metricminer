package model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QueryExecutor {
    private Query query;
    private Connection connection;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    
    public QueryExecutor(Query query, Connection connection) {
        this.query = query;
        this.connection = connection;
    }

    public QueryExecutor(Query query, Connection connection, ResultSet resultSet,
            ResultSetMetaData metaData) {
        this(query, connection);
        this.resultSet = resultSet;
        this.metaData = metaData;
    }

    public void execute() {
        try {
            PreparedStatement statement = connection.prepareStatement(query.getSql());
            this.resultSet = statement.executeQuery();
            metaData = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCSVTo(OutputStream csvOutputStream) {
        try {
            writeResultSetTo(csvOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResultSetTo(OutputStream csvOutputStream) throws SQLException, IOException {
        PrintStream csvPrint = new PrintStream(csvOutputStream);
        int total = rowCount();

        for (int i = 0; i < total; i++) {
            for (int j = 0; j < metaData.getColumnCount(); j++) {
                csvPrint.print(resultSet.getString(j + 1) + ";");
            }
            resultSet.next();
            csvPrint.print("\n");
        }
        csvPrint.close();
    }

    private int rowCount() throws SQLException {
        resultSet.last();
        int total = resultSet.getRow();
        resultSet.first();
        return total;
    }
}
