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

public class CollectionTestBase {

    @Rule public final ExpectedException thrown = ExpectedException.none();

    public static String token = "";

	@BeforeClass
	public static void setUpClient() {

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("src/test/resources/test.properties"));
		} catch (Exception e) {
			fail("Couldn't load project configuration!");
		}
        /* Check for a command-line parameter that will override the value in the settings
           Usage: mvn test -DbaseURI=http://host.example.com
        */
        String baseURIParam = System.getProperty("baseURI");
        if (baseURIParam == null) {
          RestAssured.baseURI = props.getProperty("base_uri");
        } else {
          RestAssured.baseURI = baseURIParam;
        }
        
        String basePath = System.getProperty("basePathCollections");
        if (basePath == null) {
          RestAssured.basePath = props.getProperty("base_path_collections");
        } else {
          RestAssured.basePath = basePath;  
        }

        // System.out.println(System.getProperty("port"));
        if (System.getProperty("port") != null) {
            RestAssured.port = Integer.parseInt(System.getProperty("port"));
        } else if (props.getProperty("port") != null) {
            RestAssured.port = Integer.parseInt(props.getProperty("port"));
        }

        CollectionTestBase.token = System.getProperty("apiToken");
        if (CollectionTestBase.token == null) {
          CollectionTestBase.token = props.getProperty("api_token");
        } 

	}

}