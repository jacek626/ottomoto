package com.app.common.utils;

import com.app.common.utils.site.element.HtmlElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HtmlElementTest {

    @Test
    public void shouldPrepareBlankInputElement() {
        //given
        HtmlElement htmlElement = HtmlElement.builder().tag("input").build();

        //when
        String htmlElementAfterConvert = htmlElement.toHtml();

        //then
        assertThat(htmlElementAfterConvert).isEqualTo("<input />");
    }

    @Test
    public void shouldPrepareTextInputWithIdAndName() {
        //given
        HtmlElement htmlElement = HtmlElement.builder().tag("input").id("elementId").name("elementName").type("text").build();

        //when
        String htmlElementAfterConvert = htmlElement.toHtml();

        //then
        assertThat(htmlElementAfterConvert).contains("id=\"elementId\"");
        assertThat(htmlElementAfterConvert).contains("name=\"elementName\"");
        assertThat(htmlElementAfterConvert).contains("type=\"text\"");
    }

    @Test
    public void shouldPrepareInputWithClassAndStyleAndHtmlValue() {
        //given
        HtmlElement htmlElement = HtmlElement.builder().tag("input").classStyle("classStyleValue").style("styleValue").html("htmlValue").build();

        //when
        String htmlElementAfterConvert = htmlElement.toHtml();

        //then
        assertThat(htmlElementAfterConvert).contains("class=\"classStyleValue\"");
        assertThat(htmlElementAfterConvert).contains("style=\"styleValue\"");
        assertThat(htmlElementAfterConvert).contains(">htmlValue</input>");
    }

    @Test
    public void shouldPrepareImageWithSrcAndIndex() {
        //given
        HtmlElement htmlElement = HtmlElement.builder().tag("img").src("imageSrc").index("100").build();

        //when
        String htmlElementAfterConvert = htmlElement.toHtml();

        //then
        assertThat(htmlElementAfterConvert).contains("index=\"100\"");
        assertThat(htmlElementAfterConvert).contains("src=\"imageSrc\"");
        assertThat(htmlElementAfterConvert).contains("<img");
    }

    @Test
    public void shouldPrepareButtonWithValueAndOnClick() {
        //given
        HtmlElement htmlElement = HtmlElement.builder().tag("input").type("button").value("thisIsValue").onclick("alert('click');").build();

        //when
        String htmlElementAfterConvert = htmlElement.toHtml();

        //then
        assertThat(htmlElementAfterConvert).contains("value=\"thisIsValue\"");
        assertThat(htmlElementAfterConvert).contains("onclick=\"alert('click');\"");
        assertThat(htmlElementAfterConvert).contains("type=\"button\"");
    }

}
