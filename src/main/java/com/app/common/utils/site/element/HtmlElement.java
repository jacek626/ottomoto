package com.app.common.utils.site.element;

import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;


@Builder
public class HtmlElement {
	@NonNull
	private final String tag;
	private final String id;
	private final String name;
	private final String value;
	private final String type;
	private final String onclick;
	private final String html;
	private final String src;
	private final String classStyle;
	private final String picture;
	private final String index;
	private final String style;

	@Builder.Default
	private final StringBuilder htmlElement = new StringBuilder();

	private void appendElement(String elementValue, String elementName) {
		if(elementValue != null)
			htmlElement.append(elementName).append("=\"").append(elementValue).append("\" ");
	}
	public String toHtml() {
		if(StringUtils.isBlank(tag))
			throw new IllegalArgumentException("Tag is not set");

		htmlElement.append("<").append(tag).append(" ");

		appendElement(type, "type");
		appendElement(id, "id");
		appendElement(classStyle, "class");
		appendElement(name, "name");
		appendElement(value, "value");
		appendElement(onclick, "onclick");
		appendElement(src, "src");
		appendElement(picture, "picture");
		appendElement(index, "index");
		appendElement(style, "style");

		if(html != null)
			htmlElement.append(">").append(html).append("</").append(tag).append("> ");
		else
			htmlElement.append("/>");


		return htmlElement.toString();
	}
}

