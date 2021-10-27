import com.atlassian.jira.component.ComponentAccessor

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def customField = customFieldManager.getCustomFieldObjectByName("暂停原因") // name of CF
def optionsManager = ComponentAccessor.getOptionsManager()
def fieldConfig = customField.getRelevantConfig(issue)
def option = optionsManager.getOptions(fieldConfig).find { it.value == "父事务已暂停" } // value of option 

issueInputParameters.addCustomFieldValue(customField.id, option.optionId as String)
issueInputParameters.setSkipScreenCheck(true)

issueInputParameters.setComment('当前子任务所属的父事务处于暂停状态，因此当前子任务也被置为暂停状态。')
