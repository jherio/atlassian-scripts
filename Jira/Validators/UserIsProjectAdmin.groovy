import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.user.ApplicationUsers
import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import com.atlassian.jira.bc.projectroles.DefaultProjectRoleService
import org.apache.log4j.Logger
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.Issue
import com.opensymphony.workflow.InvalidInputException
import com.onresolve.scriptrunner.runner.util.UserMessageUtil

def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cField = customFieldManager.getCustomFieldObject(example) //project key custom field id
def cFieldValue = issue.getCustomFieldValue(cField)
def customReq = customFieldManager.getCustomFieldObject(example) //request type custom field id
def requestType = issue.getCustomFieldValue(customReq).name[0]
def groupManager = ComponentAccessor.groupManager

if (requestType == "Project Configuration Request"){
  def test = cFieldValue.toString()
  def projectManager = ComponentAccessor.getProjectManager()
  def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
  def projectRoleManager = ComponentAccessor.getComponentOfType(ProjectRoleManager)
  def role = projectRoleManager.getProjectRole("Administrators")
  def jiraAdminGroup = groupManager.getUsersInGroup("jira-administrators")
  def jiraAdmins = groupManager.getUsersInGroup(jiraAdminGroup)
  def endlist = []
  def testconcat = test.replaceAll("\\s","")
  def testlist = testconcat.split(',')
  //log.warn(testlist[0])
  try{
    for (int i = 0; i < testlist.length; i++){
      def project = projectManager.getProjectByCurrentKey(testlist[i])
      log.warn(currentUser)
      log.warn(role)
      log.warn(project)
      
      def result = projectRoleManager.isUserInProjectRole(currentUser, role, project)
      log.warn(result)

      def lead = project.getProjectLead()

      def usersInRole = projectRoleManager.getProjectRoleActors(role, project).getApplicationUsers().tolist()
        for (a in jiraAdmins){
          usersInRole.remove(a)
        }
      if (usersInRole.size() > 10){
        log.warn("There are more than 10 Administrators for " + project + "Users " + usersInRole)
        endlist.add(false)
      }
      else{
        if (result == false && lead == currentUser){
          endlist.add(true)
        } else {
          log.warn("User is not an Administrator in" + project)
          endlist.add(result)
        }
      }
    }
    for (int i = 0; i < endlist.size(); i++){
      if (endlist[i] == false){
        invalidInputExecution = new InvalidInputException ("Please ensure you are a Project Lead or Administrator and have no more than 10 Administrators within your project.")
        UserMessageUtil.error("Please ensure you are a Project Lead or Administrator and have no more than 10 Administrators within your project.")
      }
    }
  }
  catch (all){
    invalidInputExecution = new InvalidInputException("Incorrect Project Key")
    UserMessageUtil.error("Incorrect Project Key")
  }
  log.warn("Admin first" + endlist)
}
else {
  return true
  log.warn("Not a Configuration Request")
}