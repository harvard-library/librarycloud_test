package edu.harvard.lib.librarycloud.test;

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

public class APIQueryTest extends APITestBase {

    @Test
    public void itemTestBasicQuery() throws Exception {
        // get("/items.json?q=peanuts").then().assertThat().body("paginationsss");
    }

}