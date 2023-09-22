package de.parett.base;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ResponseHeaderHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
	   //ctx.res().addHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval'");
       ctx.res().addHeader("X-Frame-Options",	"DENY"); 
       ctx.res().addHeader("X-XSS-Protection", "1; mode=block"); 
	   ctx.res().addHeader("X-Content-Type-Options:", "nosniff");
	   ctx.res().addHeader("Referrer-Policy", "no-referrer");
    }
}
