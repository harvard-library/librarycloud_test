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

public class ItemPaginationTest extends ItemTestBase {

    @Test
    public void itemTestPaginationDefaultsWithAllEncodi() throws Exception {
        get("/items.json").then().assertThat().body("pagination.limit",equalTo(10));
        get("/items.xml").then().assertThat().body("results.pagination.limit.text()",equalTo("10"));
        get("/items.json").then().assertThat().body("pagination.start",equalTo(0));
        get("/items.xml").then().assertThat().body("results.pagination.start.text()",equalTo("0"));
    }

	@Test
    public void itemTestPaginationSpecifyStartLimit() throws Exception {
        get("/items.json?start=5").then().assertThat().body("pagination.start",equalTo(5));
        get("/items.json?limit=5").then().assertThat().body("pagination.limit",equalTo(5));
        get("/items.json?limit=2&start=3").then().assertThat().body("pagination.limit",equalTo(2)).body("pagination.start",equalTo(3));
    }

    @Test
    public void itemTestPaginationMaximumPageSize() throws Exception {
        get("/items.json?limit=300").then().assertThat().body("pagination.limit",equalTo(250));
    }

}