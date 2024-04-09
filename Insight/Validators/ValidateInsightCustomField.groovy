import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
 
def value = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject(10000)); // Change ID to the correct one     
 
/* If an insight custom field has an object called "Microsoft" it will fail */
if (value != null && "Microsoft".equals(value[0].getName())) {
    return "This is not a valid vendor!";
}
return true