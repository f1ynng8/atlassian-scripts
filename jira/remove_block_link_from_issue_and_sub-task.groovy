import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.IssueInputParametersImpl

def issueManager = ComponentAccessor.getIssueManager()
def issueLinkManager = ComponentAccessor.getIssueLinkManager()
IssueService issueService = ComponentAccessor.getIssueService()
def bugActionId = 131  //Bug transition from 正在开发处理 to 正在测试
def storyActionId = 131  //Story transition from 正在开发处理 to 正在测试
def transitionValidationResult
def transitionResult
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def systemUser = ComponentAccessor.userManager.getUserByName("system")
def issue = issueManager.getIssueObject("NGP-284")

//第1步: 不论当前issue是什么类型，先处理当前issue的outward block链接 
//检查当前issue的outward block链接的目标issue
//如果目标issue是Story/Bug时，则尝试将目标issue推进到“正在开发处理”的下一个状态
//如果目标issue是task或者Sub-task, 则不需要执行任何操作
//目标issue不可能是Epic，目前Jira限制了Epic不能产生任何Link
issueLinkManager.getOutwardLinks(issue.getId()).each {eachLink ->
    if (eachLink.getIssueLinkType().getName() == "Blocks") {
        Issue blockLinkedIssue = eachLink.getDestinationObject()
        log.warn("-- Blocks: " + blockLinkedIssue.getSummary())
        issueLinkManager.removeIssueLink(eachLink,systemUser)
        String blockLinkedIssueStatus = blockLinkedIssue.getStatus().getName()
        if (blockLinkedIssueStatus == "正在开发处理" || blockLinkedIssueStatus == "等待开发处理"){
            if (blockLinkedIssue.getIssueType().name == "Story") {
                transitionValidationResult = issueService.validateTransition(systemUser, blockLinkedIssue.id, bugActionId, new IssueInputParametersImpl())
            } else if (blockLinkedIssue.getIssueType().name == "Bug") {
                transitionValidationResult = issueService.validateTransition(systemUser, blockLinkedIssue.id, storyActionId, new IssueInputParametersImpl())
            }

            if (transitionValidationResult.isValid()) {
                transitionResult = issueService.transition(systemUser, transitionValidationResult)
                if (!transitionResult.isValid()) { 
                    log.warn("Transition result is not valid") 
                }
            } else {
                log.warn("transitionValidationResul is not valid") 
            }
        }

    }
}
//第2步: 如果当前issue存在sub-task, 则遍历每个sub-task
//每个sub-task的处理流程与第1步中处理单个issue相同
issueLinkManager.getOutwardLinks(issue.getId()).each {eachSubtaskLink ->
    if (eachSubtaskLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue subtaskIssue = eachSubtaskLink.getDestinationObject()
        log.warn("Sub-task: " + subtaskIssue.getSummary())
        issueLinkManager.getOutwardLinks(subtaskIssue.getId()).each {eachLink ->
            if (eachLink.getIssueLinkType().getName() == "Blocks") {
                Issue blockLinkedIssue = eachLink.getDestinationObject()
                log.warn("-- Blocks: " + blockLinkedIssue.getSummary())
                issueLinkManager.removeIssueLink(eachLink,systemUser)
                String blockLinkedIssueStatus = blockLinkedIssue.getStatus().getName()
                if (blockLinkedIssueStatus == "正在开发处理" || blockLinkedIssueStatus == "等待开发处理"){
                    if (blockLinkedIssue.getIssueType().name == "Story") {
                        transitionValidationResult = issueService.validateTransition(systemUser, blockLinkedIssue.id, bugActionId, new IssueInputParametersImpl())
                    } else if (blockLinkedIssue.getIssueType().name == "Bug") {
                        transitionValidationResult = issueService.validateTransition(systemUser, blockLinkedIssue.id, storyActionId, new IssueInputParametersImpl())
                    }

                    if (transitionValidationResult.isValid()) {
                        transitionResult = issueService.transition(systemUser, transitionValidationResult)
                        if (!transitionResult.isValid()) { 
                            log.warn("Transition result is not valid") 
                        }
                    } else {
                        log.warn("transitionValidationResul is not valid") 
                    }
            	}

            }
        }
    }
}
//第3步：如果当前issue是sub-task, 还是检查一下父issue是否满足transition的条件
issueLinkManager.getInwardLinks(issue.getId()).each {eachSubtaskLink ->
    if (eachSubtaskLink.getIssueLinkType().getName() == "jira_subtask_link") {
        Issue parentIssue = issue.getParentObject()
        log.warn("Parent: " + parentIssue.getSummary())
        issueLinkManager.removeIssueLink(eachSubtaskLink,systemUser)
        String parentIssueStatus = parentIssue.getStatus().getName()
        if (parentIssueStatus == "正在开发处理" || parentIssueStatus == "等待开发处理"){
            if (parentIssue.getIssueType().name == "Story") {
                transitionValidationResult = issueService.validateTransition(systemUser, parentIssue.id, bugActionId, new IssueInputParametersImpl())
            } else if (parentIssue.getIssueType().name == "Bug") {
                transitionValidationResult = issueService.validateTransition(systemUser, parentIssue.id, storyActionId, new IssueInputParametersImpl())
            }

            if (transitionValidationResult.isValid()) {
                transitionResult = issueService.transition(systemUser, transitionValidationResult)
                if (!transitionResult.isValid()) { 
                    log.warn("Transition result is not valid") 
                }
            } else {
                log.warn("transitionValidationResul is not valid") 
            }
        }
    }
}



 


