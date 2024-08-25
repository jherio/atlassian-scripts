/**
 * ExampleRestEndpoint.groovy
 * 
 * This endpoint receives XML data, processes it, and returns a JSON response.
 * It demonstrates basic XML parsing and JSON response generation.
 */

import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.transform.BaseScript
import groovy.json.JsonBuilder
import groovy.xml.XmlSlurper
import groovy.xml.XmlException

import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import org.slf4j.LoggerFactory

@BaseScript CustomEndpointDelegate delegate

def log = LoggerFactory.getLogger("com.onresolve.jira.groovy")

postExample(httpMethod: "POST") { MultivaluedMap queryParams, String body ->
    log.debug("Received POST request with body: ${body}")

    try {
        def xmlData = new XmlSlurper().parseText(body)
        log.debug("Parsed XML: ${xmlData}")

        def exampleValue = xmlData.example.text()
        log.debug("Value of 'example' XML tag: ${exampleValue}")

        // Process the data (replace this with your actual processing logic)
        def result = ["status": "success", "message": "XML processed successfully", "exampleValue": exampleValue]

        return Response.ok(new JsonBuilder(result).toString()).build()
    } catch (XmlException e) {
        log.error("Failed to parse XML: ${e.message}")
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new JsonBuilder([error: "Invalid XML format"]).toString())
                .build()
    } catch (Exception e) {
        log.error("Unexpected error processing request: ${e.message}", e)
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new JsonBuilder([error: "Internal server error"]).toString())
                .build()
    }
}