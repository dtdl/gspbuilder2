##<!-- generated by job scheduler generator, please do not manually update, contact psg if required -->
<#if schedule.dateOffset != "">DateOffset:${schedule["dateOffset"]}
</#if><#if schedule["extractionQueryName"]!= "">ExtractionQueryName:${schedule["extractionQueryName"]}
</#if><#if schedule["fileHeader"]!= "">FileHeader:${schedule["fileHeader"]}
</#if><#if schedule["publishToDirectory"]!= "">PublishToDirectory:${schedule["publishToDirectory"]}
</#if><#if schedule["publishToTemproryDirectory"]!= "">PublishToTemproryDirectory:${schedule["publishToTemproryDirectory"]}
</#if><#if schedule["publishToTemproryFileNamePattern"]!= "">PublishToTemproryFileNamePattern:${schedule["publishToTemproryFileNamePattern"]}
</#if><#if schedule["spliter"]!= "">Spliter:${schedule["spliter"]}
</#if><#if schedule["temproryFileType"]!= "">TemproryFileType:${schedule["temproryFileType"]}
</#if><#if schedule["SourceFileDirectory"]!= "">SourceFileDirectory:${schedule["SourceFileDirectory"]}
</#if><#if schedule["SourceFileSystem"]!= "">SourceFileSystem:${schedule["SourceFileSystem"]}
</#if><#if schedule["TargetFileSystem"]!= "">TargetFileSystem:${schedule["TargetFileSystem"]}
</#if><#if schedule["TargetFileDirectory"]!= "">TargetFileDirectory:${schedule["TargetFileDirectory"]}</#if>