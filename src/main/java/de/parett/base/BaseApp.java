package de.parett.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.session.DatabaseAdaptor;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;
import org.jdbi.v3.core.ConnectionFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.sse.SseClient;
import io.javalin.http.staticfiles.Location;

public abstract class BaseApp {

	private Javalin app;

	public static Jdbi jdbi;
	private static final String dbUrl = "jdbc:sqlite:./db.sqlite3";
	protected static final SQLiteDataSource ds = new SQLiteDataSource();

	private static Logger LOGGER = LoggerFactory.getLogger(BaseApp.class);
	private static ConcurrentLinkedQueue<SseClient> clients = new ConcurrentLinkedQueue<>();

	static {

	}

	public int start() {
		return this.start(null);
	}

	public int start(Integer port) {
		if (port != null) {
			this.app.start(port);
		} else {
			this.app.start();
		}

		return this.app.port();
	}

	public void stop() {
		this.app.stop();
	}

	private static JDBCSessionDataStoreFactory jdbcDataStoreFactory() {
		DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();

		databaseAdaptor.setDatasource(ds);
		JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
		jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);

		return jdbcSessionDataStoreFactory;
	}

	public BaseApp() {

		ds.setUrl(dbUrl);
		ds.setEnforceForeignKeys(true);

		jdbi = Jdbi.create(new ConnectionFactory() {

			@Override
			public Connection openConnection() throws SQLException {
				Connection connection = ds.getConnection();
				configureConnection(connection);
				return connection;
			}
		});
		jdbi.installPlugin(new SqlObjectPlugin());
		jdbi.installPlugin(new SQLitePlugin());
		// jdbi.setSqlLogger(new Slf4JSqlLogger(LOGGER));
	//	jdbi.setSqlLogger(new SqlLogger() {

	//		@Override
	//		public void logAfterExecution(StatementContext context) {
	//			// TODO Auto-generated method stub
	//			SqlLogger.super.logAfterExecution(context);
	//		}

	//		@Override
	//		public void logBeforeExecution(StatementContext context) {
	//			LOGGER.info(context.getParsedSql().toString());
	//			LOGGER.info(context.getParsedSql().getParameters().getParameterNames().stream().map(p -> {
	//				return p + " " + context.getBinding().findForName(p, context).get().toString();
	//			}).collect(Collectors.joining(", ")));
	//		}

	//		@Override
	//		public void logException(StatementContext context, SQLException ex) {
	//			// TODO Auto-generated method stub
	//			SqlLogger.super.logException(context, ex);
	//		}

	//	});

		SessionHandler sessionHandler = new SessionHandler();
		SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
		sessionCache.setSessionDataStore(jdbcDataStoreFactory().getSessionDataStore(sessionHandler));
		sessionHandler.setSessionCache(sessionCache);
		sessionHandler.setHttpOnly(true);
		sessionHandler.getSessionCookieConfig().setComment("__SAME_SITE_STRICT__");

		this.app = Javalin
				.create(config -> {
					config.staticFiles.add(staticFiles -> {
						staticFiles.hostedPath = "/static"; // change to host files on a subpath, like '/assets'
						staticFiles.directory = "/public"; // the directory where your files are located
						staticFiles.location = Location.CLASSPATH; // Location.CLASSPATH (jar) or Location.EXTERNAL
																	// (file system)
						staticFiles.precompress = false; // if the files should be pre-compressed and cached in memory
															// (optimization)
						staticFiles.aliasCheck = null; // you can configure this to enable symlinks (=
														// ContextHandler.ApproveAliases())
						staticFiles.headers = Map.ofEntries(
						// Map.entry("Cache-Control", "max-age=1800")
						); // headers that will be set for the files
						staticFiles.skipFileFunction = req -> false; // you can use this to skip certain files in the
																		// dir, based on the HttpServletRequest
					});
					config.staticFiles.add(staticFiles -> {
						staticFiles.hostedPath = "/"; // change to host files on a subpath, like '/assets'
						staticFiles.directory = "/root_statics"; // the directory where your files are located
						staticFiles.location = Location.CLASSPATH; // Location.CLASSPATH (jar) or Location.EXTERNAL
						// (file system)
						staticFiles.precompress = false; // if the files should be pre-compressed and cached in memory
						// (optimization)
						staticFiles.aliasCheck = null; // you can configure this to enable symlinks (=
						// ContextHandler.ApproveAliases())
						staticFiles.headers = Map.ofEntries(
						// Map.entry("Cache-Control", "max-age=1800")
						); // headers that will be set for the files
						staticFiles.skipFileFunction = req -> false; // you can use this to skip certain files in the
						// dir, based on the HttpServletRequest
					});
					config.staticFiles.enableWebjars();
					configure(config);
					// config.accessManager(new MyAccessManager());
					// config.enableDevLogging();
				});

		app.routes(() -> {
			LOGGER.info("Init Routes");
			this.initRoutes();
		});

		app.sse("/images", client -> {
			client.keepAlive();
			clients.add(client);
		});

		app.before(new ContextHandler());
		app.after(new ResponseHeaderHandler());

		init(app);
	}

	protected abstract void initRoutes();

	protected abstract void configure(JavalinConfig config);

	protected abstract void init(Javalin app);

	protected abstract void configureConnection(Connection connection);

}
