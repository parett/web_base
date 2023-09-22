package de.parett.base;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jdbi.v3.core.ConnectionFactory;
import org.sqlite.Function;

public class SqLiteConnectionFactory implements ConnectionFactory {

	private final DataSource ds;

	public SqLiteConnectionFactory(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Connection openConnection() throws SQLException {
		Connection connection = ds.getConnection();
		initSqlFunctions(connection);
		return connection;
	}

	private void initSqlFunctions(Connection conn) throws SQLException{

		Function.create(conn, "newID", new NewID());

	}
}
