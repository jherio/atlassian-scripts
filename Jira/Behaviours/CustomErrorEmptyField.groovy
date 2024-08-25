/*
 * CustomErrorEmptyField.groovy
 * 
 * This script validates that the "Fix Version/s" field is not empty.
 * It displays an error message if the field is empty, and clears the error otherwise.
 */

import com.onresolve.jira.groovy.user.FieldBehaviours
import com.onresolve.jira.groovy.user.FormField

final String ERROR_MESSAGE = "<div>The field \"Fix Version/s\" should be filled in.</div>"

FormField fixVersionField = getFieldById(fieldChanged)
def fieldFormValue = fixVersionField.getValue()

if (!fieldFormValue) {
   fixVersionField.setError(ERROR_MESSAGE)
} else {
   fixVersionField.setError("")
}