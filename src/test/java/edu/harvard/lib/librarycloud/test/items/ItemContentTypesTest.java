package edu.harvard.lib.librarycloud.test.items;

import  com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseBuilder;
// import com.jayway.restassured.itest.java.support.WithJetty;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.exception.JsonPathException;
import com.jayway.restassured.path.xml.exception.XmlPathException;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.List;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.FLOAT_AND_DOUBLE;
import static com.jayway.restassured.path.json.config.JsonPathConfig.jsonPathConfig;
import static com.jayway.restassured.path.xml.XmlPath.CompatibilityMode.HTML;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class ItemContentTypesTest extends ItemTestBase {

    @Test
    public void itemTestResponseCodes() throws Exception {
        get("/items").then().assertThat().statusCode(200);
        get("/items?q=peanuts").then().assertThat().statusCode(200);        
    }

    @Test @Ignore("Default values are not correct")
    public void itemTestContentTypeDefault() throws Exception {
        get("/items").then().assertThat().contentType(ContentType.XML);
    }

    @Test
    public void itemTestContentTypeExtensions() throws Exception {
        get("/items.json").then().assertThat().contentType(ContentType.JSON);
        get("/items.xml").then().assertThat().contentType(ContentType.XML);
    }

	@Test @Ignore("Default values are not correct")
    public void itemTestContentTypeQueryWithExtensions() throws Exception {
		get("/items?q=peanuts").then().assertThat().contentType(ContentType.XML);
        get("/items.json?q=peanuts").then().assertThat().contentType(ContentType.JSON);
        get("/items.xml?q=peanuts").then().assertThat().contentType(ContentType.XML);
    }

	@Test
    public void itemTestContentTypeWithContentTypeRequested() throws Exception {
        given().header("Accept", "application/json").when().get("/items").then().assertThat().contentType(ContentType.JSON);
        given().header("Accept", "application/xml").when().get("/items").then().assertThat().contentType(ContentType.XML);
    }



}