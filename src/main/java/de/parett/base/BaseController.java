package de.parett.base;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Handler;

public class BaseController {

	protected static void get(Path path, Handler handler) {
		ApiBuilder.get(path.getPath(), handler);
	}

	protected static void post(Path path, Handler handler) {
		ApiBuilder.post(path.getPath(), handler);
	}

	protected static void put(Path path, Handler handler) {
		ApiBuilder.put(path.getPath(), handler);
	}

	protected static void delete(Path path, Handler handler) {
		ApiBuilder.delete(path.getPath(), handler);
	}

	protected static String getPathParam(String param) {
		return ContextHandler.ctx.get().pathParam(param);
	}

}
