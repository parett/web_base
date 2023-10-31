package de.parett.base.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@UseClasspathSqlLocator
public interface CrudDao<T> {

	@SqlQuery
	public List<T> list();

	@SqlQuery
	public Optional<T> findById(@Bind("id") UUID id);

	@SqlUpdate
	public void save(@BindBean T entity);

}
