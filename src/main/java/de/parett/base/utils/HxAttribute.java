package de.parett.base.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.javalin.http.Context;
import j2html.attributes.Attribute;

public class HxAttribute{

	public static Attribute hx_get(String path){
		return new Attribute("hx-get", path);
	}
	
	public static Attribute hx_delete(String path){
		return new Attribute("hx-delete", path);
	}

	public static Attribute hx_include(String expr){
		return new Attribute("hx-include", expr);
	}

	public static Attribute hx_include_by_name(String... names){
		String value = Stream.of(names).map(name -> String.format("[name='%s']", name)).collect(Collectors.joining(", "));
		return hx_include(value);
	}
	public static Attribute hx_target(String target){
		return new Attribute("hx-target", target);
	}

	public static Attribute hx_target_body = hx_target("body");


	public static Attribute hx_post(String path){
		return new Attribute("hx-post", path);
	}

	public static Context addTriggerEventToContext(Context ctx, String trigger){
		ctx.res().setHeader("HX-TRIGGER", trigger);
		return ctx;
	}

	public static Context addRefreshHeaderToContext(Context ctx){
		ctx.res().setHeader("HX-Refresh","true");
		return ctx;
	}

	public static Context addRedirectHeaderToContext(Context ctx, String path){
		ctx.res().setHeader("HX-Redirect", path);
		return ctx;
	}

	public static Context addPushUrlHeaderToContext(Context ctx, String path){
		ctx.header("HX-Push-Url", path);
		return ctx;
	}

	public static final Attribute hx_vals(String expr){
		return new Attribute("hx-vals", expr);
	}

	public static Attribute hx_trigger(String trigger){
		return new Attribute("hx-trigger", trigger);
	}

	public static Attribute hx_encoding(String encoding){
		return new Attribute("hx-encoding", encoding);
	}

	public static final Attribute hx_encoding_multipart_form_data = hx_encoding("multipart/form-data");




	public static Attribute hx_on(String name, String script){
		return new Attribute("hx-on:" + name, script);
	}

	public static final Attribute hx_trigger_dblClick = hx_trigger("dblclick");
	public static final Attribute hx_trigger_ctrl_left = hx_trigger("keyup[ctrlKey&&key=='ArrowLeft'] from:body");
	public static final Attribute hx_trigger_ctrl_right = hx_trigger("keyup[ctrlKey&&key=='ArrowRight'] from:body");

	public static final Attribute hx_swap_none = new Attribute("hx-swap", "none");
	public static final Attribute hx_swap_innerHtml = new Attribute("hx-swap", "innerHTML");
	public static final Attribute hx_swap_outerHtml = new Attribute("hx-swap", "outerHTML");
	public static final Attribute hx_swap_afterbegin = new Attribute("hx-swap", "afterbegin");
	public static final Attribute hx_swap_beforebegin = new Attribute("hx-swap", "beforebegin");
	public static final Attribute hx_swap_beforeend = new Attribute("hx-swap", "beforeend");
	public static final Attribute hx_swap_afterend = new Attribute("hx-swap", "afterend");
	public static final Attribute hx_swap_delete = new Attribute("hx-swap", "delete");
	public static final Attribute hx_push_url = new Attribute("hx-push-url", "true");
	public static final Attribute hx_push_url(String url){
		return new Attribute("hx-push-url", url);
	}


	/**
	 ********************************************
	 * Extensions
	 ********************************************
	 */

	public static Attribute hx_ext(String extention){
		return new Attribute("hx-ext", extention);
	}

	/**
	 * Server send events
	 */
	public static final Attribute hx_ext_sse = hx_ext("sse");

	public static Attribute hx_sse_connect(String path){
		return new Attribute("sse-connect", path);
	}

	public static Attribute hx_sse_swap(String messageName){
		return new Attribute("sse-swap", messageName);
	}

	public static final Attribute hx_sse_swap_message = hx_sse_swap("message");
}
