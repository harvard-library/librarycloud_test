package edu.harvard.lib.librarycloud.test.collections;

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
import org.hamcrest.BaseMatcher;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

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

public class CollectionHeadersTest extends CollectionTestBase {

    private Matcher<String> compareStringToArray(final String[] array) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object o) {
                String[] splitString = ((String)o).replaceAll("\\s+", "").split(",");
                for(String s : splitString) {
                    if (!Arrays.asList(array).contains(s))
                        return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("compareStringToArray should return true if the string has an array of " +
                        "strings (comma delimited) that match ").appendValue(Arrays.toString(array));
            }
        };
    }

    @Test
    public void collectionsTestAccessControlHeaders() throws Exception {
        get("/collections").then().assertThat()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", compareStringToArray(new String[]
                        {"GET", "POST", "DELETE", "PUT"}))
                .header("Access-Control-Allow-Headers", compareStringToArray(new String[]
                        {"Access-Control-Allow-Headers", "Content-Type", "Accept", "X-Requested-With", "X-LibraryCloud-API-Key"}))
                .header("Access-Control-Expose-Headers", "Location");
    }
}