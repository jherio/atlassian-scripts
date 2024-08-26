/**
 * UpdateCustomField.groovy
 * 
 * This script is based on the original work by Adaptavist, available at:
 * https://library.adaptavist.com/entity/basics-updating-customfields
 *
 * Improvements made to the original script include:
 * - Added comprehensive error handling and logging
 * - Introduced constants for easier configuration
 * - Added checks for issue existence before attempting updates
 * - Demonstrated a wider range of custom field types
 *
 * This script updates various custom fields for a given issue using the ScriptRunner API.
 *
 * Usage: 
 * 1. Set the ISSUE_KEY constant to the desired issue key.
 * 2. Adjust the custom field names and values as needed.
 * 3. Run the script in ScriptRunner.
 */

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Logger

final String ISSUE_KEY = "SR-1"
def log = Logger.getLogger("UpdateCustomField")

def issueManager = ComponentAccessor.issueManager
def customFieldManager = ComponentAccessor.customFieldManager
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser

MutableIssue issue = issueManager.getIssueObject(ISSUE_KEY)

if (!issue) {
    log.error("Issue with key ${ISSUE_KEY} not found")
    return
}

try {
    Issues.getByKey(ISSUE_KEY).update {
        // Set custom fields with options (select lists, checkboxes, radio buttons)
        setCustomFieldValue('SelectListA', 'BBB')
        setCustomFieldValue('MultiSelectA', 'BBB', 'CCC')
        setCustomFieldValue('RadioButtons', 'Yes')
        setCustomFieldValue('Checkboxes', 'Maybe', 'Yes')

        // Cascading select
        setCustomFieldValue('CascadingSelect', 'BBB', 'B2')

        // Set text fields
        setCustomFieldValue('TextFieldA', 'New Value')

        // Set user fields
        setCustomFieldValue('UserPicker', 'bob')
        setCustomFieldValue('MultiUserPickerA', 'bob', 'alice')

        // Set group fields
        setCustomFieldValue('GroupPicker', 'jira-users')
        setCustomFieldValue('MultiGroupPicker', 'jira-users', 'jira-administrators')

        // Set date and date-time custom fields
        setCustomFieldValue('First DateTime', new Date().format("dd/MMM/yy h:mm a"))
        // setCustomFieldValue('Date', new Date().format("dd/MMM/yy"))

        // Set project picker custom field
        setCustomFieldValue('ProjectPicker', 'SSPA')

        // Set custom field of type version
        setCustomFieldValue('SingleVersionPicker', 'Version1')
    }
    log.info("Successfully updated custom fields for issue ${ISSUE_KEY}")
} catch (Exception e) {
    log.error("Error updating custom fields for issue ${ISSUE_KEY}: ${e.message}", e)
}