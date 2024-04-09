import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.search.SearchProvider
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.util.ImportUtils
import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.issue.attachment.CreateAttachmentParamsBean
import com.atlassian.crowd.embedded.impl.ImmutableUser
import com.atlassian.jira.bc.user.UserService
import com.atlassian.jira.user.DelegatingApplicationUser
import com.atlassian.jira.user.ApplicationUsers


// User Manager
def userManager = ComponentAccessor.getUserManager();

// Authentication Context
def authContext                     = ComponentAccessor.getJiraAuthenticationContext()
// User Service
def userService                     = ComponentAccessor.getComponent(UserService)


// Get Insight Object Facade from plugin accessor
def objectFacadeClass               = ComponentAccessor.getPluginAccessor().getClassLoader().findClass("com.riadalabs.jira.plugins.insight.channel.external.api.facade.ObjectFacade")
def objectFacade                    = ComponentAccessor.getOSGiComponentInstanceOfType(objectFacadeClass)

/* Get Insight Object Attribute Facade from plugin accessor */
def objectTypeAttributeFacadeClass  = ComponentAccessor.getPluginAccessor().getClassLoader().findClass("com.riadalabs.jira.plugins.insight.channel.external.api.facade.ObjectTypeAttributeFacade")
def objectTypeAttributeFacade       = ComponentAccessor.getOSGiComponentInstanceOfType(objectTypeAttributeFacadeClass)

/* Get the factory that creates Insight Attributes */
def objectAttributeBeanFactoryClass = ComponentAccessor.getPluginAccessor().getClassLoader().findClass("com.riadalabs.jira.plugins.insight.services.model.factory.ObjectAttributeBeanFactory")
def objectAttributeBeanFactory      = ComponentAccessor.getOSGiComponentInstanceOfType(objectAttributeBeanFactoryClass)

// Get Object Schema list
def objectSchemaFacadeClass         = ComponentAccessor.getPluginAccessor().getClassLoader().findClass("com.riadalabs.jira.plugins.insight.channel.external.api.facade.ObjectSchemaFacade")
def objectSchemaFacade              = ComponentAccessor.getOSGiComponentInstanceOfType(objectSchemaFacadeClass)

// Get Insight IQL Facade from plugin accessor
def iqlFacadeClass                  = ComponentAccessor.getPluginAccessor().getClassLoader().findClass("com.riadalabs.jira.plugins.insight.channel.external.api.facade.IQLFacade"); 
def iqlFacade                       = ComponentAccessor.getOSGiComponentInstanceOfType(iqlFacadeClass);

// Switching to Admin user to perform operations restricted by access
def curUser = authContext.getLoggedInUser()

if (object) {
    log.warn(object.toString())
    //https://community.atlassian.com/t5/Jira-questions/Is-it-possible-to-create-new-JIRA-users-with-Groovy/qaq-p/222688
    def insUserName = objectFacade.loadObjectAttributeBean(object.getId(), "Name").getObjectAttributeValueBeans()[0]
    def insUserMail = objectFacade.loadObjectAttributeBean(object.getId(), "Email").getObjectAttributeValueBeans()[0]
    def insUserCurr = objectFacade.loadObjectAttributeBean(object.getId(), "Jira Users").getObjectAttributeValueBeans()[0]
    log.warn("Name: ${insUserName.getValue()}")
    log.warn("Mail: ${insUserMail.getValue()}")
    log.warn("Jira Users: ${insUserCurr.getValue()}")

    if ( insUserMail.getValue().toString().toLowerCase() != insUserCurr.getValue().toString().toLowerCase() ) {
        def updateUser = userManager.getUserByName(insUserCurr.getValue().toString())

        if ( updateUser ) {
            def builder = ImmutableUser.newUser(ApplicationUsers.toDirectoryUser(updateUser));
            builder.emailAddress(insUserMail.getValue())
            builder.name(insUserMail.getValue())
            builder.displayName(insUserName.getValue())
            userManager.updateUser(new DelegatingApplicationUser(updateUser.getId(), updateUser.getKey(), builder.toUser()));
            
            def insattrJiraUsers     = objectFacade.loadObjectAttributeBean(object.getId(), "Jira Users")
            def insattrJiraUsersType = objectTypeAttributeFacade.loadObjectTypeAttributeBean(insattrJiraUsers.objectTypeAttributeId)

            def insattrJiraUsersNew = objectAttributeBeanFactory.createObjectAttributeBeanForObject(object, insattrJiraUsersType, insUserMail.getValue().toString())

            if ( insattrJiraUsers ) {
                insattrJiraUsersNew.setId( insattrJiraUsers.getId() )
            }

            try {
                insattrJiraUsers = objectFacade.storeObjectAttributeBean(insattrJiraUsersNew)
            } catch (Exception vie) {
                log.warn("Could not update object attribute due to validation exception:" + vie.getMessage())
            }

        }
    }

}