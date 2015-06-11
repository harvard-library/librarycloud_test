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

public class ItemQueryTest extends ItemTestBase {

    @Test
    public void itemTestBasicQuery() throws Exception {
        get("/items.xml?q=peanuts").then().assertThat().body(hasXPath("//items"));
    }

    @Test
    public void itemTestQueryEmbeddedColons() throws Exception {
        get("/items.xml?q=peanuts:balha:doesnotexists").then().assertThat().body(hasXPath("//items"));
    }

    @Test
    public void itemTestTitleQuery() throws Exception {
        get("/items.xml?title=peanuts").then().assertThat().body(hasXPath("//items"));
    }

    @Test
    public void itemTestFetchMissingItem() throws Exception {
        get("/items/i_am_a_bogus_item_id").then().assertThat().statusCode(404);
    }

    @Test
    public void itemTestFetchMissingItemWithColons() throws Exception {
        get("/items/i_am_a_bogus_item_id:hahaha").then().assertThat().statusCode(404);
    }

    @Test
    public void itemTestCollectionQueries() throws Exception {
        get("/items.xml?collectionTitle=peanuts").then().assertThat().body(hasXPath("//items"));
    }

    @Test
    public void itemTestFieldQueries() throws Exception {
    	String[] fields = {
							"abstractTOC",
							"classification",
							// "collectionId",
							"collectionTitle",
							"edition",
							"genre",
							"identifier",
							"isCollection",
							"isManuscript",
							"isOnline",
							"issuance",
							"languageCode",
							// "languageText",
							"name",
							"originPlace",
							"physicalDescription",
							"physicalLocation",
							"publisher",
							"recordIdentifier",
							"relatedItem",
							"resourceType",
							"role",
							"shelfLocator",
							"source",
							"subject",
							"subject.geographic",
							"subject.hierarchicalGeographic",
							"subject.hierarchicalGeographic.continent",
							"subject.hierarchicalGeographic.country",
							"subject.hierarchicalGeographic.province",
							"subject.hierarchicalGeographic.region",
							"subject.hierarchicalGeographic.state",
							"subject.hierarchicalGeographic.territory",
							"subject.hierarchicalGeographic.county",
							"subject.hierarchicalGeographic.city",
							"subject.hierarchicalGeographic.island",
							"subject.hierarchicalGeographic.area",
							"subject.hierarchicalGeographic.extraterrestrialArea",
							"subject.hierarchicalGeographic.citySection",
							"subject.hierarchicalGeographic.area",
							"subject.name",
							// "subject.name.role",
							"subject.temporal",
							"title",
							"url",
							// "url.access",
							"urn"
    	};
    	for (int i = 0; i < fields.length; i++) {    		
        	given().log().ifValidationFails()
        	.get("/items.xml?" + fields[i] + "=peanuts").then().assertThat().body(hasXPath("//items"));    		
    	}
    }






}