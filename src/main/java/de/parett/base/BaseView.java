package de.parett.base;

import static j2html.TagCreator.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;

public class BaseView {

	public static final String TEXT_HTML_UTF8 = ContentType.HTML + ";charset=utf-8";
	public final Set<String> jsLibs;
	public final Set<String> cssFiles;
	public final Set<String> resourceCssFiles;
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private DomContent content;

	private DomContent jsLibsToHtmlTag() {
		return each(
				jsLibs.stream().map(src -> script().withType("text/javascript").withSrc(src)));
	}

	private DomContent cssFilesToHtmlTag() {
		return each(
				cssFiles.stream()
						.map(src -> link().withRel("stylesheet").withHref(src)));
	}

	private DomContent resourceCssFilesToHtmlTag() {
		return each(
				resourceCssFiles.stream()
						.map(url -> link().withRel("stylesheet").withHref("/public/" + url)));
	}
	public BaseView() {
		this(null, null);
	}

	protected void addResourceCssFile(Class<?> clazz, String file){
		this.resourceCssFiles.add(clazz.getPackageName().replace('.', '/') + "/" + file);
	}

	public BaseView(String title, DomContent content) {
		this.title = title != null ? title : "Change it!";
		this.content = content != null ? content : div();

		jsLibs = new LinkedHashSet<>();
//		jsLibs.add("/static/app.js");
		jsLibs.add("/webjars/htmx.org/1.9.4/dist/htmx.js");
		jsLibs.add("/webjars/htmx.org/1.9.4/dist/ext/sse.js");
		jsLibs.add("/webjars/masonry-layout/4.2.2/dist/masonry.pkgd.js");
		jsLibs.add("/webjars/imagesloaded/4.1.4/imagesloaded.pkgd.min.js");

		cssFiles = new LinkedHashSet<>();
		//cssFiles.add("/webjars/purecss/2.0.6/build/pure.css");
		//cssFiles.add("/webjars/font-awesome/6.1.0/css/all.css");
		cssFiles.add("/static/main.css");
		cssFiles.add("/webjars/picnic/7.1.0/picnic.min.css");

		resourceCssFiles = new LinkedHashSet<>();
	}

	protected <T> String valueOrEmptyString(T object, Function<T, String> mapper){
		return Optional.ofNullable(object)
			.map(mapper)
			.orElse("");
	}

	protected String valueOrEmptyString(Object object){
		return valueOrEmptyString(object, Object::toString);
	}


	public void readInContext(){
		this.readInContext(ContextHandler.ctx.get());
	}

	public void readInContext(Context ctx){
	}

	public void render() {

		String html = "<!DOCTYPE html>\n"
				+ html(
						head(
							title(title),
							rawHtml("<meta charset=\"utf-8\">"),
							rawHtml("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"),
							link().withRel("icon").withHref("/static/favicon.ico").withType("image/x-icon"),
							cssFilesToHtmlTag(),
							jsLibsToHtmlTag(),
//						script("htmx.logger = function(elt, event, data) { if(console) { console.log(event, elt, data); } }"),
							resourceCssFilesToHtmlTag()
						),
						body(
							content
						))
						.renderFormatted();

		Context ctx = ContextHandler.ctx.get();
		ctx.contentType(TEXT_HTML_UTF8);
		ctx.result(html);
	}

}
