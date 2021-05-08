<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<PackageDescription minInstallCenterVersion="${yamlConfig["target"]["gc"]["baseversion"]}" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="PackageDescription.xsd">
		<Package name="${yamlConfig["target"]["gc"]["name"]}" type="${yamlConfig["target"]["gc"]["type"]}" version="${yamlConfig["target"]["gc"]["buildversion"]}">
		<Component>${yamlConfig["target"]["gc"]["name"]}</Component>
		<Description>${yamlConfig["target"]["gc"]["name"]}</Description>
		<Content>
			<File path="${yamlConfig["target"]["gc"]["name"]}.tar.gz" type="Package"/>
			<File path="ReleaseNotes.docx" type="Package"/>
		</Content>
	</Package>
	<Deployment>
		<Prerequisites>
			<Product id="datamodel" name="Datamodel GSDM" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["gc"]["baseversion"]}"/>
			</Product>
			<Product id="dmgso" name="Datamodel GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["gc"]["baseversion"]}"/>
			</Product>
			<Product id="stgso" name="Starterset GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["gc"]["baseversion"]}"/>
			</Product>
			<Product id="configuration" name="Datamodel Configuration" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["gc"]["baseversion"]}"/>
			</Product>
			<Product id="workflow" name="Datamodel Workflow" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["gc"]["baseversion"]}"/>
			</Product>
		</Prerequisites>
		<Locations>
			<Location id="GSDMDataModel" multiple="true" type="Database">
				<Description>The destination where the DataModel GoldenSource Objects was installed.</Description>
				<Prerequisites>
					<Prerequisite id="datamodel" required="true"/>
					<Prerequisite id="dmgso" required="true"/>
					<Prerequisite id="stgso" required="true"/>
				</Prerequisites>
			</Location>
			<Location id="VDDBDataModel" type="Database" multiple="true">
				<Description>A database where the Datamodel updates should be installed.</Description>
			</Location>
			<Location id="CFDataModel" multiple="true" type="Database">
				<Description>The destination where the Configuration Datamodel was installed.</Description>
				<Prerequisites>
					<Prerequisite id="configuration"/>
				</Prerequisites>
			</Location>
			<Location id="WFDataModel" multiple="true" type="Database">
				<Description>The destination where the Workflow Datamodel was installed.</Description>
				<Prerequisites>
					<Prerequisite id="workflow"/>
				</Prerequisites>
			</Location>
		</Locations>
		<Tasks>
			<!-- Untar Installation Package -->
			<tgz description="Untar package archive" dest="tmp_out/" name="Untar Package" src="${yamlConfig["target"]["gc"]["name"]}.tar.gz" srcLoc="PackageZip"/>
			
			<!--START SaveDTD-->
<#list componentList as component><#if component.cmptTyp == "saveDtd">
			<<#if !component.active>!--</#if>savedtd name="${component.cmptTyp}: ${component.cmptName}" destLoc="CFDataModel" src="tmp_out${component.cmptPath}" columnName="MSG_SET_BLOB" columnkeyname="XML_MSG_SET_ID" columnkeyvalue="1" dtdFile="tmp_out${component.cmptPath}msgtype.dtd" lastChgUsrId="CUSTOM" tableName="FT_T_XMGS"/<#if !component.active>--</#if>>
</#if></#list>
			<!--<savedtd name="Save DTD: StreetRefMsgSetGC" destLoc="CFDataModel" src="tmp_out/reference/rulexml/StreetRefMsgSetGC.xml" columnName="MSG_SET_BLOB" columnkeyname="XML_MSG_SET_ID" columnkeyvalue="1" dtdFile="tmp_out/reference/rulexml/msgtype.dtd" lastChgUsrId="RMBP:CSTM" tableName="FT_T_XMGS"/>-->
			<!--END SaveDTD-->

			<!--START SaveLOB-->
<#list componentList as component><#if component.cmptTyp == "saveLob">
			<<#if !component.active>!--</#if>savelob name="${component.cmptTyp}: ${component.cmptName}" destLoc="CFDataModel" src="tmp_out${component.cmptPath}" columnName="XML_CONFIG_CLOB" columnkeyname="XML_CONFIG_MNEM" columnkeyvalue="VSH" dtdFile="tmp_out${component.cmptPath}VSH.dtd" lastChgUsrId="CUSTOM" tableName="FT_T_XMGS"/<#if !component.active>--</#if>>
</#if></#list>
			<!--<savelob name="Save LOB: IDUniquenessCheckMatchKeySet" destLoc="CFDataModel" src="tmp_out/reference/streetreffiles/IDUniquenessCheckMatchKeySet.xml" columnName="XML_CONFIG_CLOB" columnkeyname="XML_CONFIG_MNEM" columnkeyvalue="IDUniquenessCheckMatchKeySet" dtdFile="tmp_out/reference/streetreffiles/IDUniquenessCheckMatchKeySet.dtd" lastChgUsrId="RMBP:CSTM" tableName="FT_T_XCFG"/>-->
			<!--END SaveLOB-->

			<!--START RESOURCE-->
<#list componentList as component><#if component.cmptTyp == "resource">
			<<#if !component.active>!--</#if>deployResource name="${component.cmptTyp}: ${component.cmptName}" destLoc="CFDataModel" src="tmp_out${component.cmptPath}" dest="${component.cmptOrchPath}" encoding="UTF-8"/<#if !component.active>--</#if>>
</#if></#list>
			<!--deployResource name="MDX: GC_BNP_ACCT" destLoc="CFDataModel" src="tmp_out/mapping/NIKKO/BNP/GC_BNP_ACCT.mdx" dest="mapping/NIKKO/BNP" encoding="utf-8"/-->
			<!--END RESOURCE-->

			<!--START XMLFeed-->
			<!--END XMLFeed-->

			<!--START XSLT-->
			<!--END XSLT-->

			<!--START VendorDefinition-->
<#list componentList as component><#if component.cmptTyp == "vendordifinition">
			<<#if !component.active>!--</#if>deployGSP name="${component.cmptTyp}: ${component.cmptName}" destLoc="CFDataModel" src="tmp_out${component.cmptPath}"/<#if !component.active>--</#if>>
</#if></#list>
			<!--deployGSP name="VendorDefinition: NIKKO_CSV_LINEBYLINE" destLoc="CFDataModel" src="tmp_out/vendordefinitions/NIKKO_CSV_LINEBYLINE.gsp"/-->
			<!--END VendorDefinition-->

			<!--START BusinessFeed-->
			<!--END BusinessFeed-->

			<!--START MessageType-->
			<!--END MessageType-->

			<!--START Workflow-->
<#list componentList as component><#if component.cmptTyp == "workflow">
			<<#if !component.active>!--</#if>deployGSP name="${component.cmptTyp}: ${component.cmptName}" destLoc="WFDataModel" src="tmp_out${component.cmptPath}" type="workflow"/<#if !component.active>--</#if>>
</#if></#list>
			<!-- deployGSP name="Workflow: WF_S3Test" destLoc="WFDataModel" src="tmp_out/workflows/Custom/S3/AutoTest/WF_S3Test.gsp" type="workflow"/ -->
			<!--END Workflow-->

			<!--START Event-->
			<!--END Event-->

			<!--START Task-->
			<!--END Task-->

			<!--START DDL-->
<#list componentList as component><#if component.cmptTyp == "ddl">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["target"]["gc"]["buildversion"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--sql name="DML: 0010-ENTR" src="tmp_out/sql/DML/0010-ENTR.sql" destLoc="GSDMDataModel" patchLevel="8.7.2.01" dbDialect="ORACLE"/-->
			<!--END DDL-->

			<!--START PLSQL-->
<#list componentList as component><#if component.cmptTyp == "plsql">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["target"]["gc"]["buildversion"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--END PLSQL-->

			<!--START DML-->
<#list componentList as component><#if component.cmptTyp == "dml">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["target"]["gc"]["buildversion"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--END DML-->

			<!--START GSO-->
<#list componentList as component><#if component.cmptTyp == "gso">
			<<#if !component.active>!--</#if>deployGSE name="${component.cmptTyp}: ${component.cmptName}" destLoc="GSDMDataModel" src="tmp_out${component.cmptPath}" encoding="utf-8"/<#if !component.active>--</#if>>
</#if></#list>
			<!--deployGSE name="GSO: ApplicationUser" destLoc="GSDMDataModel" src="tmp_out/gso/GSOConfig/ApplicationUser.gso" encoding="windows-1252"/-->
			<!--END GSO-->

			<!--START GOC-->
			<!--END GOC-->

			<!--START PublishingProfile-->
			<!--END PublishingProfile-->

			</Tasks>
	</Deployment>
	<Undeployment>
		<Prerequisites cloneFromDeploymentSection="true"/>
		<Locations cloneFromDeploymentSection="true"/>
		<Tasks cloneFromDeploymentSection="true"/>
	</Undeployment>
</PackageDescription>
