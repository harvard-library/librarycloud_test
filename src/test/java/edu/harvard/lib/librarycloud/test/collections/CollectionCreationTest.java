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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CollectionCreationTest extends CollectionTestBase {

	String collectionURI;

    @Test
    public void createCollectionTest() throws Exception {
    	/* Create the collection */
    	Response response = 
    		given()
    		.header("X-LibraryCloud-API-Key", this.token)
    		.contentType("application/json")
    		.body("{\"title\": \"Testing collection\",\"abstract\":\"Call me Ishmael\"}")
        	.post("/collections")
        	.then().assertThat().statusCode(201)
        	.extract().response();

        
            System.out.println(response.header("Location"));

        /* Delete the collection */
    	given()
    		.header("X-LibraryCloud-API-Key", this.token)
    		.contentType("application/json")
    		.body("")
        	.delete(response.header("Location"))
        	.then().assertThat().statusCode(204);
    }


    @Test
    public void unauthorizedCreateCollectionTest() throws Exception {
        given()
            .header("X-LibraryCloud-API-Key", "I am not valid")
            .contentType("application/json")
            .body("{\"title\": \"Testing collection\",\"abstract\":\"Unauthorized - bad token\"}")
            .post("/collections")
            .then().assertThat().statusCode(401);

        given()
            .contentType("application/json")
            .body("{\"title\": \"Testing collection\",\"abstract\":\"Unauthorized - no token\"}")
            .post("/collections")
            .then().assertThat().statusCode(401);
    }



}