if(cfValues['直接移交给开发人员'].getValue() == "是") 
    return (cfValues['测试人'] != null)
else return 1;

if(cfValues['直接移交给开发人员'].getValue() == '是') 
    return (issue.getComponents().size() != 0)
else return 1;

if(cfValues['直接移交给开发人员'].getValue() == '是') 
    return (cfValues['Sprint'] != null)
else return 1;
