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

public class CollectionManipulationTest extends CollectionTestBase {

	String collectionURI;

	@Before
	public void setup() throws Exception {
    	Response response = 
    		given()
    		.header("X-LibraryCloud-API-Key", this.token)
    		.contentType("application/json")
    		.body("{\"title\": \"Testing collection\",\"abstract\":\"Call me Ishmael\"}")
        	.post("/collections")
        	.then().assertThat().statusCode(201)
        	.extract().response();

        this.collectionURI = response.header("Location");
	}

	@After
	public void teardown() {
        if (this.collectionURI != null) {            
            given()
                .header("X-LibraryCloud-API-Key", this.token)
                .contentType("application/json")
                .body("")
                .delete(this.collectionURI)
                .then().assertThat().statusCode(204);
        }
	}


    @Test
    public void modifyCollectionAbstractTest() throws Exception {
        /* Modify the abstract */
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("{\"abstract\":\"I have been updated\"}")
            .put(this.collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify the change */
        given()
            .contentType("application/json")
            .get(this.collectionURI)
            .then().assertThat().body("[0].abstract", equalTo("I have been updated"))
            .and().assertThat().body("[0].title", equalTo("Testing collection"));
    }

    @Test
    public void modifyCollectionTitleTest() throws Exception {
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("{\"title\": \"Testing collection update\"}")
            .put(this.collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify the change */
        given()
            .contentType("application/json")
            .get(this.collectionURI)
            .then().assertThat().body("[0].title", equalTo("Testing collection update"));
    }

    @Test
    public void modifyCollectionTitleTestAndAbstract() throws Exception {
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("{\"title\": \"Testing collection update again\",\"abstract\":\"I have been updated again\"}")
            .put(this.collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify the change */
        given()
            .contentType("application/json")
            .get(this.collectionURI)
            .then().assertThat().body("[0].title", equalTo("Testing collection update again"))
            .and().body("[0].abstract", equalTo("I have been updated again"));
    }

    @Test
    public void addItemTest() throws Exception {
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("[{\"item_id\": \"xyzzy\"}]")
            .post(this.collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify the change */
        given()
            .contentType("application/json")
            .get(this.collectionURI + "/items")
            .then().assertThat().body(equalTo("[{\"item_id\":\"xyzzy\"}]"));
    }

    @Test
    public void deleteItemTest() throws Exception {
        /* Add item */
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("[{\"item_id\": \"xyzzy\"}]")
            .post(this.collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify added */
        given()
            .contentType("application/json")
            .get(this.collectionURI + "/items")
            .then().assertThat().body(equalTo("[{\"item_id\":\"xyzzy\"}]"));

        /* Delete item */
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("[{\"item_id\": \"xyzzy\"}]")
            .delete(this.collectionURI + "/items/xyzzy")
            .then().assertThat().statusCode(204);

        /* Verify delete */
        given()
            .contentType("application/json")
            .get(this.collectionURI + "/items")
            .then().assertThat().body(equalTo("[]"));
    }

    @Test
    public void searchByItemTest() throws Exception {
        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("[{\"item_id\": \"xyzzy_i_will_be_unique\"}]")
            .post(this.collectionURI)
            .then().assertThat().statusCode(204);

        given()
            .contentType("application/json")
            .get("collections?contains=xyzzy_i_will_be_unique")
            .then().assertThat().body("[0].title", equalTo("Testing collection"));
    }   


}