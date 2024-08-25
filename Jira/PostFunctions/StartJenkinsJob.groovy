/*
 * StartJenkinsJob.groovy
 * 
 * This script triggers a Jenkins job based on Jira issue custom field values.
 * 
 * Instructions:
 * 1. Replace the placeholders in the 'jenkinsJobFields' list with your actual custom field IDs.
 * 2. Set up the following application properties in Jira:
 *    - JENKINS_URL_PROPERTY_KEY: The URL of your Jenkins instance
 *    - JENKINS_JOB_PATH_PROPERTY_KEY: The path to the Jenkins job you want to trigger
 *    - JENKINS_USERNAME_PROPERTY_KEY: The username for Jenkins authentication
 *    - JENKINS_API_TOKEN_PROPERTY_KEY: The API token for Jenkins authentication
 * 3. Add this script as a post-function in your Jira workflow.
 * 
 * Note: Ensure that the user running this script has the necessary permissions
 * to read the application properties and access the custom fields.
 */

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.MutableIssue
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType
import com.atlassian.jira.config.properties.ApplicationProperties

// Get the current issue
def issue = issue as MutableIssue

if (issue) {
    log.info("Processing issue ${issue.key}, retrieving custom fields...")

    def customFieldManager = ComponentAccessor.getCustomFieldManager()
    def jenkinsJobFields = [
        "CUSTOM_FIELD_ID_1",
        "CUSTOM_FIELD_ID_2",
        "CUSTOM_FIELD_ID_3",
        "CUSTOM_FIELD_ID_4",
        "CUSTOM_FIELD_ID_5",
        "CUSTOM_FIELD_ID_6"
    ]

    // Retrieve custom field values
    def jenkinsJobValues = jenkinsJobFields.collectEntries { fieldId ->
        def customField = customFieldManager.getCustomFieldObject(fieldId)
        [(customField.name): issue.getCustomFieldValue(customField)]
    }

    // Prepare Jenkins job parameters
    def data = jenkinsJobValues.findAll { it.value != null }

    // Retrieve Jenkins configuration from application properties
    def applicationProperties = ComponentAccessor.getComponent(ApplicationProperties)
    def jenkinsUrl = applicationProperties.getString("JENKINS_URL_PROPERTY_KEY")
    def jenkinsJobPath = applicationProperties.getString("JENKINS_JOB_PATH_PROPERTY_KEY")
    def jenkinsUsername = applicationProperties.getString("JENKINS_USERNAME_PROPERTY_KEY")
    def jenkinsApiToken = applicationProperties.getString("JENKINS_API_TOKEN_PROPERTY_KEY")

    if (!jenkinsUrl || !jenkinsJobPath || !jenkinsUsername || !jenkinsApiToken) {
        log.error("Jenkins configuration is incomplete. Please check application properties.")
        return
    }

    def httpBuilder = new HTTPBuilder("${jenkinsUrl}/job/${jenkinsJobPath}/buildWithParameters")

    try {
        String userPassBase64 = "${jenkinsUsername}:${jenkinsApiToken}".bytes.encodeBase64().toString()

        httpBuilder.request(Method.POST, ContentType.JSON) { req ->
            headers."Authorization" = "Basic ${userPassBase64}"
            uri.query = data

            response.success = { resp, json ->
                log.info("Jenkins job triggered successfully for issue ${issue.key}. Status: ${resp.status}")
            }

            response.failure = { resp, json ->
                log.error("Failed to trigger Jenkins job for issue ${issue.key}. Status: ${resp.status}, Response: ${json}")
            }
        }
    } catch (Exception e) {
        log.error("Error occurred while triggering Jenkins job for issue ${issue.key}", e)
    }
} else {
    log.warn("No issue found in the current context")
}