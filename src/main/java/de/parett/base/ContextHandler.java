package de.parett.base;


import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ContextHandler implements Handler {

	public final static ThreadLocal<Context> ctx = new ThreadLocal<>();
	//public final static ThreadLocal<Map<Settings,String>> settings = new ThreadLocal<>();

	@Override
	public void handle(Context ctx) throws Exception {
		ContextHandler.ctx.set(ctx);
		//settings.set(Settings.getValueMap());
	}


//	public static String getSetting(Settings key){
//		return settings.get().get(key);
//	}
}
