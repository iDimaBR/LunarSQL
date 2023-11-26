package com.github.idimabr;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLResult<T> implements AutoCloseable {
    @Getter
    private ResultSet resultSet;
    private Connection connection;
    private PreparedStatement statement;
    private SQLAdapter<T> adapter;

    public SQLResult(ResultSet resultSet, Connection connection, PreparedStatement statement, SQLAdapter<T> adapter) {
        this.resultSet = resultSet;
        this.connection = connection;
        this.statement = statement;
        this.adapter = adapter;
    }

    public T initAdapter() throws SQLException {
        if (resultSet.next()) {
            try {
                return adapter.onResult(resultSet);
            } catch (Exception e) {
                throw new SQLException("Erro no adapter do SQLResult: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null && !connection.isClosed()) connection.close();
    }
}