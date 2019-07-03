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
		if(!tag.isPresent())
			throw new IllegalArgumentException("tag is not set");
			
		StringBuilder htmlElement = new StringBuilder();
		
	//	if(tag.get().equals("input")) {
			htmlElement.append("<" + tag.get() + " ");
			
			if(type.isPresent())
				htmlElement.append("type=\""+ type.get() + "\" ");
			
			if(id.isPresent())
				htmlElement.append("id=\""+ id.get() + "\" ");
			
			if(classStyle.isPresent())
				htmlElement.append("class=\""+ classStyle.get() + "\" ");
			
			if(name.isPresent())
				htmlElement.append("name=\""+ name.get() + "\" ");
			
			if(value.isPresent())
				htmlElement.append("value=\""+ value.get() + "\" ");
			
			if(onclick.isPresent())
				htmlElement.append("onclick=\""+ onclick.get() + "\" ");
			
			if(src.isPresent())
				htmlElement.append("src=\""+ src.get() + "\" ");
			
			if(picture.isPresent())
				htmlElement.append("picture=\""+ picture.get() + "\" ");
			
			if(index.isPresent())
				htmlElement.append("index=\""+ index.get() + "\" ");
			
			if(html.isPresent()) 
				htmlElement.append(">" + html.get()  + "</"+ tag.get() + "> ");
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
		private String tag;
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

