import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.link.*


def issueManager = ComponentAccessor.getIssueManager()
def issue = issueManager.getIssueObject("WLR-469")
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def cfManager = ComponentAccessor.getCustomFieldManager()
def project = issue.getProjectObject()
def projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager.class) 
def projectManager = ComponentAccessor.getProjectManager()
def projectCategory = projectManager.getProjectCategoryForProject(issue.getProjectObject())    
log.warn(projectCategory.name)    
//设计人
def projectRole = projectRoleManager.getProjectRole("产品设计Leader")
def usersInRole = projectRoleManager.getProjectRoleActors(projectRole, project).getApplicationUsers().toList()
def user = usersInRole[0]
log.warn(user)
log.warn(usersInRole)
Collection cfs = cfManager.getCustomFieldObjectsByName("设计人")
CustomField cf = cfs[0]
issue.setCustomFieldValue(cf,user)
//测试人
projectRole = projectRoleManager.getProjectRole("测试Leader")
usersInRole = projectRoleManager.getProjectRoleActors(projectRole, project).getApplicationUsers().toList()
user = usersInRole[0]
log.warn(user)
log.warn(usersInRole)
cfs = cfManager.getCustomFieldObjectsByName("测试人")
cf = cfs[0]
issue.setCustomFieldValue(cf,user)
if (projectCategory.name == "VersionFlow-B"){
    //负责人
    projectRole = projectRoleManager.getProjectRole("测试环境负责人")
    usersInRole = projectRoleManager.getProjectRoleActors(projectRole, project).getApplicationUsers().toList()
    user = usersInRole[0]
    log.warn(user)
    log.warn(usersInRole)
    cfs = cfManager.getCustomFieldObjectsByName("负责人")
    cf = cfs[0]
    issue.setCustomFieldValue(cf,user)
}

//更新issue
issueManager.updateIssue(systemUser, issue, EventDispatchOption.DO_NOT_DISPATCH, false)
