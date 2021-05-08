<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<PackageDescription minInstallCenterVersion="${yamlConfig["target"]["dwh"]["baseversion"]}" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="PackageDescription.xsd">
	<Package name="${yamlConfig["target"]["dwh"]["name"]}" type="${yamlConfig["target"]["dwh"]["type"]}" version="${yamlConfig["target"]["dwh"]["buildversion"]}">
		<Component>${yamlConfig["target"]["dwh"]["name"]}</Component>
		<Description>${yamlConfig["target"]["dwh"]["name"]}</Description>
		<Content>
			<File path="${yamlConfig["target"]["dwh"]["name"]}.tar.gz" type="Package"/>
			<File path="ReleaseNotes.docx" type="Package"/>
		</Content>
	</Package>
	<Deployment>
		<Prerequisites>
			<Product id="datamodel" name="Datamodel GSDM" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["dwh"]["baseversion"]}"/>
			</Product>
			<Product id="dmgso" name="Datamodel GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["dwh"]["baseversion"]}"/>
			</Product>
			<Product id="stgso" name="Starterset GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["dwh"]["baseversion"]}"/>
			</Product>
			<Product id="configuration" name="Datamodel Configuration" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["dwh"]["baseversion"]}"/>
			</Product>
			<Product id="workflow" name="Datamodel Workflow" type="GOLDENSOURCE">
				<Version min="${yamlConfig["target"]["dwh"]["baseversion"]}"/>
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
			<tgz description="Untar package archive" dest="tmp_out/" name="Untar Package" src="${yamlConfig["target"]["dwh"]["name"]}.tar.gz" srcLoc="PackageZip"/>
			
			<!--START SaveDTD-->
			<!--END SaveDTD-->

			<!--START SaveLOB-->
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
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["target"]["dwh"]["version"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--sql name="DML: 0010-ENTR" src="tmp_out/sql/DML/0010-ENTR.sql" destLoc="GSDMDataModel" patchLevel="8.7.2.01" dbDialect="ORACLE"/-->
			<!--END DDL-->

			<!--START PLSQL-->
			<!--END PLSQL-->

			<!--START DML-->
<#list componentList as component><#if component.cmptTyp == "dml">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["target"]["dwh"]["buildversion"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
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
