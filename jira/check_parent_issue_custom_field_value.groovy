import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField

def issueManager = ComponentAccessor.getIssueManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issue = issueManager.getIssueObject("WLR-491")
int result = 0
log.warn(issue.parentObject?.issueType.name)
log.warn(issue.parentObject.status.name)
if (["Bug", "改进"].contains(issue.parentObject?.issueType.name) && ["正在协作处理","正在测试", "等待测试"].contains(issue.parentObject.status.name)){
    result = result + 1
    //if (result > 0) return 1
}

Collection cfs = customFieldManager.getCustomFieldObjectsByName("自动创建子任务")
CustomField cf = cfs[0]
def cfValueObject = issue.parentObject.getCustomFieldValue(cf) as String
log.warn(cfValueObject)
if (["Bug"].contains(issue.parentObject?.issueType.name) && cfValueObject == "是"){
    result = result + 1
}

return result
