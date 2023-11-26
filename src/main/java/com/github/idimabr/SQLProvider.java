package com.github.idimabr;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.*;

public class SQLProvider {

    private String url;
    private String username;
    private String password;

    public SQLProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public <T> SQLResult<T> query(String query, Object... parameters) {
        return query(query, null, parameters);
    }

    @SneakyThrows
    public SQLResult<Void> update(String query, Object... parameters) {
        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, parameters);
            statement.executeUpdate();
            return new SQLResult<>(null, connection, statement, null);
        } catch (Exception e) {
            throw new SQLException("Erro no update do SQLProvider: " + e.getMessage());
        }
    }

    @SneakyThrows
    public <T> SQLResult<T> query(String query, Class<? extends SQLAdapter<T>> adapterClass, Object... parameters) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(query);
            setParameters(statement, parameters);
            resultSet = statement.executeQuery();

            SQLAdapter<T> adapter = (adapterClass != null) ? adapterClass.getDeclaredConstructor().newInstance() : null;
            return new SQLResult<>(resultSet, connection, statement, adapter);
        } catch (SQLException | ReflectiveOperationException e) {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            throw new SQLException("Erro no query do SQLProvider: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Conexão não foi possivel: " + e.getMessage());
        }
    }

    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }

    public static void main(String[] args){}
}
