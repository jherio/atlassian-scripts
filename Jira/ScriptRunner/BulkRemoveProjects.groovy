import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.project.DefaultProjectService
import com.atlassian.jira.util.ErrorCollection
import com.atlassian.jira.util.SimpleErrorCollection
import com.atlassian.jira.bc.project.ProjectService.DeleteProjectValidationResult
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.bc.project.ProjectService
import org.apache.log4j.Logger

/*
This script will bulk remote Jira projects passed in via CSV. /sharedhome/files/projects_to_remove.csv
*/

def u = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
def ps = ComponentAccessor.getComponent (DefaultProjectService.class);
def pm = ComponentAccessor.getComponent (ProjectManager.class);

def projects = []
new File(/sharedhome/files/projects_to_remove.csv).splitEachLine(",") {field ->
  projects.add(
    fields[0]
  )
}

for (project in projects) {
  def p = pm.getProjectObjBykey(project)
  if (p != null) {
    ErrorCollection e = new SimpleErrorCollection();
    def dpvr = new ProjectService.DeleteProjectValidationResult (e, p);
    ps.deleteProject (u, dpvr);
    log.warn "Project " + p + " has been removed."
  }
  else {
    log.warn "Project " + project + " does not exist."
  }
}

log.warn "Projects have been successfully removed."