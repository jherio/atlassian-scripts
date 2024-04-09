import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import javax.ws.rs.core.Response
import groovyx.net.http.ContentType
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.ModifiedValue
import com.atlassian.jira.component.ComponentAccessor


IssueManager im = ComponentAccessor.getIssueManager()
MutableIssue issue = im.getIssueObject("JSD-1")

if(issue){
log.info("Found Issue, retrieving custom fields..")

def jenkins_job_1_cf_val = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_11122"))

def jenkins_job_2_cf_val = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_15202"))

def jenkins_job_3_cf_val = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_15203"))

def jenkins_job_4_cf_val = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_15204"))

def jenkins_job_4_cf_val = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_14302"))

def jenkins_job_5_cf_val_list = issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObject("customfield_13709"))

//Store your jenkin parameter here as a map
def data = [JJ1: "$jenkins_job_1_cf_val", JJ2: "$jenkins_job_2_cf_val", JJ3: "$jenkins_job_2_cf_val", JJ3: "$jenkins_job_3_cf_val", JJ4: "$jenkins_job_4_cf_val", JJ5: "$jenkins_job_5_cf_val"]

def httpBuilder = new HTTPBuilder("http://<JenkinsURL>/job/<JobPath>/buildWithParameters")

def username='JenkinsUser'
def password='JenkinsUserToken'

String userPassBase64 = "$username:$password".toString().bytes.encodeBase64()

httpBuilder.setHeaders(["Authorization": "Basic $userPassBase64"])
httpBuilder.request(Method.POST, ContentType.JSON) {
uri.query = data
response.success = {resp, json ->
log.warn "request succeed with status ${resp.status}"
//log.warn "request succeed with status ${resp.status}, response body was [${resp.entity.content.text}]"
}
response.failure = { resp, json ->
log.warn "request failed with status ${resp.status}"
//log.warn "request failed with status ${resp.status} response body was [${resp.entity.content.text}]"
}
}
}