package model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Ignore;
import org.junit.Test;

public class QueryExecutorTest {

    @Ignore
    @Test
    public void JDBClearn() throws Exception {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        
        Connection connection = session.connection();

        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        
        ResultSet resultSet = statement.executeQuery("select * from SourceCode");
        resultSet.last();
        int total = resultSet.getRow();
        resultSet.first();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int j = 0; j < total; j++) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.print(resultSet.getString(i + 1) + ";");
            }
            System.out.println();
            resultSet.next();
        }
        session.close();
    }

    @Test
    public void shouldWriteCSV() throws Exception {
        Query query = new Query("select * from SourceCode");
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedConnection.prepareStatement(query.getSql())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(resultSet.getRow()).thenReturn(1);
        when(metaData.getColumnCount()).thenReturn(3);
        when(resultSet.getString(1)).thenReturn("column1");
        when(resultSet.getString(2)).thenReturn("column2");
        when(resultSet.getString(3)).thenReturn("column3");
        
        QueryExecutor queryExecutor = new QueryExecutor(query,
                mockedConnection, resultSet, metaData);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        queryExecutor.execute();
        queryExecutor.writeCSVTo(byteArrayOutputStream);

        String csvOutput = new String(byteArrayOutputStream.toByteArray(), Charset.defaultCharset());
        System.out.println(csvOutput);
        assertEquals("column1;column2;column3;\n", csvOutput);

    }

}
