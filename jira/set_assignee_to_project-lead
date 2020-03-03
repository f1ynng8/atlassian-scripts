//Run as script runner in post-function
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.issue.Issue

String projectLead = issue.getProjectObject().getProjectLead().getKey()

issue.setAssigneeId(projectLead)
