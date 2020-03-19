import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.project.Project
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.IssueManager

MutableIssue mutableIssue = ComponentAccessor.getIssueManager().getIssueByCurrentKey(issue.getKey());
String projectLead = issue.getProjectObject().getProjectLead().getKey()

mutableIssue.setAssigneeId(projectLead)
