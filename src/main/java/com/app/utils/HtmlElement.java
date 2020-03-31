package com.app.utils;

import java.util.Optional;

public class HtmlElement {
	private final Optional<String> id;
	private final Optional<String> name;
	private final Optional<String> value;
	private final Optional<String> type;
	private final Optional<String> tag;
	private final Optional<String> onclick;
	private final Optional<String> html;
	private final Optional<String> src;
	private final Optional<String> classStyle;
	private final Optional<String> picture;
	private final Optional<String> index;
	
	
	private HtmlElement(Builder builder) {
		id = Optional.ofNullable(builder.id);
		name = Optional.ofNullable(builder.name);
		value = Optional.ofNullable(builder.value);
		type = Optional.ofNullable(builder.type);
		tag = Optional.ofNullable(builder.tag);
		onclick = Optional.ofNullable(builder.onclick);
		html = Optional.ofNullable(builder.html);
		src = Optional.ofNullable(builder.src);
		classStyle = Optional.ofNullable(builder.classStyle);
		picture = Optional.ofNullable(builder.picture);
		index = Optional.ofNullable(builder.index);
	}

	public String returnHtml() {
		if(tag.isEmpty())
			throw new IllegalArgumentException("tag is not set");
			
		StringBuilder htmlElement = new StringBuilder();
	//	if(tag.get().equals("input")) {
			htmlElement.append("<").append(tag.get()).append(" ");
			
			if(type.isPresent())
				htmlElement.append("type=\"").append(type.get()).append("\" ");
			
			if(id.isPresent())
				htmlElement.append("id=\"").append(id.get()).append("\" ");
			
			if(classStyle.isPresent())
				htmlElement.append("class=\"").append(classStyle.get()).append("\" ");
			
			if(name.isPresent())
				htmlElement.append("name=\"").append(name.get()).append("\" ");
			
			if(value.isPresent())
				htmlElement.append("value=\"").append(value.get()).append("\" ");
			
			if(onclick.isPresent())
				htmlElement.append("onclick=\"").append(onclick.get()).append("\" ");
			
			if(src.isPresent())
				htmlElement.append("src=\"").append(src.get()).append("\" ");
			
			if(picture.isPresent())
				htmlElement.append("picture=\"").append(picture.get()).append("\" ");
			
			if(index.isPresent())
				htmlElement.append("index=\"").append(index.get()).append("\" ");
			
			if(html.isPresent()) 
				htmlElement.append(">").append(html.get()).append("</").append(tag.get()).append("> ");
			else 
				htmlElement.append("/>");
		//}
			
		return htmlElement.toString();
	}
	
	public static class Builder {
		private String id;
		private String name;
		private String value;
		private String type;
		private final String tag;
		private String onclick;
		private String html;
		private String src;
		private String classStyle;
		private String picture;
		private String index;
		
		
		public Builder(String tag) {
			this.tag = tag;
		}
		
		public Builder id(String id) { this.id = id; return this; }
		public Builder name(String name) { this.name = name; return this; }
		public Builder value(String value) { this.value = value; return this; }
		public Builder type(String type) { this.type = type; return this; }
		public Builder onclick(String onclick) { this.onclick = onclick; return this; }
		public Builder html(String html) { this.html = html; return this; }
		public Builder src(String src) { this.src = src; return this; }
		public Builder classStyle(String classStyle) { this.classStyle = classStyle; return this; }
		public Builder picture(String picture) { this.picture = picture; return this; }
		public Builder index(String index) { this.index = index; return this; }
		
		public HtmlElement build() {
			return new HtmlElement(this);
		}
		
	}

}

