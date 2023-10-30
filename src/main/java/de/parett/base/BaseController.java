package de.parett.base;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Handler;

public class BaseController {


	protected static void get(String path, Handler handler){
		ApiBuilder.get(path, handler);
	}

	protected static void post(String path, Handler handler){
		ApiBuilder.post(path, handler);
	}
}
