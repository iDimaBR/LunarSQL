package com.github.idimabr;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLAdapter<T> {
    public abstract T onResult(ResultSet resultSet) throws SQLException;
}
