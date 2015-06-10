package edu.harvard.lib.librarycloud.test.integration;

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

public class CollectionToItemAPIIntegrationTest {

    @Rule public final ExpectedException thrown = ExpectedException.none();

    public static String itemAPIURL;
    public static String collectionAPIURL;
    public static String token;

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
       
        token = System.getProperty("apiToken");
        if (token == null) {
          token = props.getProperty("api_token");
        } 

        String baseURIParam = System.getProperty("baseURI");
        if (baseURIParam == null) {
          baseURIParam = props.getProperty("base_uri");
        } 
        
        String basePathItems = System.getProperty("basePathItems");
        if (basePathItems == null) {
          basePathItems = props.getProperty("base_path_items");
        }

        String basePathCollections = System.getProperty("basePathCollections");
        if (basePathCollections == null) {
		  basePathCollections = props.getProperty("base_path_collections");
        }
        
        String port = "80";        
        if (System.getProperty("port") != null) {
            port = System.getProperty("port");
        } else if (props.getProperty("port") != null) {
            port = props.getProperty("port");
        }

        itemAPIURL = baseURIParam + ":" + port + basePathItems ;
        collectionAPIURL = baseURIParam + ":" + port + basePathCollections;

	}


    @Test
    public void addItemToCollectionAndGetInItemTest() throws Exception {

        String itemId = "000477939";

        /* Create collection */
        Response response = 
            given()
            .header("X-LibraryCloud-API-Key", token)
            .contentType("application/json")
            .body("{\"title\": \"Testing collection\",\"abstract\":\"Integration testing\"}")
            .post(collectionAPIURL + "/collections")
            .then().assertThat().statusCode(201)
            .extract().response();

        String collectionURI = response.header("Location");
        String[] components = collectionURI.split("/");
        String collectionId = components[components.length - 1];

        given()
            .header("X-LibraryCloud-API-Key", this.token)
            .contentType("application/json")
            .body("[{\"item_id\": \"" + itemId + "\"}]")
            .post(collectionURI)
            .then().assertThat().statusCode(204);

        /* Verify the change */
        given()
            .contentType("application/json")
            .get(collectionURI + "/items")
            .then().assertThat().body(equalTo("[{\"item_id\":\"" + itemId + "\"}]"));

        System.out.println("Waiting for 5 seconds");
        Thread.sleep(5000);

        Response itemResponse = 
        given()
            .header("Accept", "application/xml")
            .get(itemAPIURL + "/items/" + itemId)
            .then().assertThat().body(hasXPath("//collections/collection/identifier[text()='" + collectionId + "']"))
            .extract().response();

        itemResponse.body().print();

        /* Delete the collection */
        given()
            .header("X-LibraryCloud-API-Key", token)
            .contentType("application/json")
            .body("")
            .delete(collectionURI)
            .then().assertThat().statusCode(204);

    }




}