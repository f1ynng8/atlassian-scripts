import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.user.ApplicationUser

// Get a pointer to the issue
Issue issueKey  = issue

// Get the current logged in user
def CurrentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser() as ApplicationUser

// Get access to the Jira comment and component manager
CommentManager commentManager = ComponentAccessor.getCommentManager()

// Get the last comment entered in on the issue to a String
def comment = "A Sample Comment"


// Check if the issue is not null
if(issueKey){
        // Create a comment on the issue
        commentManager.create(issueKey, CurrentUser,comment, true)
}
