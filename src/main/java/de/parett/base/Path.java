package de.parett.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;

import lombok.Getter;

public class Path {

	@Getter
	private String path;

	private ThreadLocal<String> buildPath = new ThreadLocal<>();

	//private ThreadLocal<Map<String,String>> pathParamMap = new ThreadLocal<>();

	@Getter
	private Set<String> pathParams = new HashSet<>();

	public Path(String path) {
		this.path = path;
	}

	public Path addPath(String path) {
		Path clone = this.clone();
		clone.ensureTailingSlash();
		clone.path += path;
		return clone;

	}

	public Path addPathParam(String param) {
		Path clone = this.clone();
		clone.pathParams.add(param);
		clone.ensureTailingSlash();
		clone.path += "{" + param + "}";
		return clone;
	}

	public Path setPathParam(String param, String value){
		
		String currentPath = getCurrentBuildPath();
		LoggerFactory.getLogger(Path.class).info("replace param {} in {} with {}", param, currentPath, value);
		currentPath = currentPath.replace("{"+param+"}", encodeValue(value));
		buildPath.set(currentPath);
		return this;
	}

	public String build() {
		String path = getCurrentBuildPath();
		buildPath.remove();
		return path;
	}

	protected Path clone() {
		Path clone = new Path(this.path);
		clone.pathParams = new HashSet<>(this.pathParams);
		return clone;
	}

	public Path setQueryParam(String name, String value) {

		String currentPath = getCurrentBuildPath();
		StringBuilder sb = new StringBuilder(currentPath);
		if (currentPath.contains("?")) {
			sb.append('&');
		} else {
			sb.append('?');
		}

		sb.append(encodeValue(name));
		sb.append('=');
		sb.append(encodeValue(value));

		buildPath.set(sb.toString());

		return this;
	}

	private String getCurrentBuildPath() {
		String currentPath = buildPath.get();
		if (currentPath == null) {
			currentPath = this.path;
			buildPath.set(currentPath);
		}

		return currentPath;

	}

	private String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private void ensureTailingSlash() {
		if (!path.endsWith("/")) {
			path += "/";
		}
	}

}
