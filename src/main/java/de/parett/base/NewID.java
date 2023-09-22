package de.parett.base;

import java.sql.SQLException;
import java.util.UUID;

import org.sqlite.Function;

public class NewID extends Function {

	@Override
	protected void xFunc() throws SQLException {
		UUID newID = UUID.randomUUID();
		result(newID.toString());
	}
}
