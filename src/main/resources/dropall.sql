SET foreign_key_checks = 0;


DROP TABLE  `Artifact` ,
`Author` ,
`Commit` ,
`Commit_Artifact` ,
`ProjectConfigurationEntry` ,
`Modification` ,
`Project` ,
`SourceCode` ,
`Task` ,
`Task_Task` ,
`TaskConfigurationEntry`,
`CCResult`,
`FanOutResult`, 
`LComResult`,
`MethodsInvocationResult`,
`LinesOfCodeResult`,
`TestedMethodFinderResult`,
`MethodsCountResult`,
`BlamedLine`,
`Project_Tag`,
`Query`,
`Tag`;

SET foreign_key_checks = 1;
