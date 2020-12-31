<?xml version="1.0"?>
<PackageDescription minInstallCenterVersion="${yamlConfig["package"]["baseversion"]}" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="PackageDescription.xsd">
	<Package name="${yamlConfig["package"]["name"]}" type="${yamlConfig["package"]["type"]}" version="${yamlConfig["package"]["version"]}">
		<Description>${yamlConfig["package"]["name"]}</Description>
		<Component>${yamlConfig["package"]["name"]}</Component>
		<Content>
			<File path="${yamlConfig["package"]["name"]}.tar.gz" type="Package"/>
			<File path="ReleaseNotes.docx" type="Package"/>
		</Content>
	</Package>
	<Deployment>
		<Prerequisites>
			<Product id="datamodel" name="Datamodel GSDM" type="GOLDENSOURCE">
				<Version min="${yamlConfig["package"]["baseversion"]}"/>
			</Product>
			<Product id="dmgso" name="Datamodel GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["package"]["baseversion"]}"/>
			</Product>
			<Product id="stgso" name="Starterset GoldenSource Objects" type="GOLDENSOURCE">
				<Version min="${yamlConfig["package"]["baseversion"]}"/>
			</Product>
			<Product id="configuration" name="Datamodel Configuration" type="GOLDENSOURCE">
				<Version min="${yamlConfig["package"]["baseversion"]}"/>
			</Product>
			<Product id="workflow" name="Datamodel Workflow" type="GOLDENSOURCE">
				<Version min="${yamlConfig["package"]["baseversion"]}"/>
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
			<!-- Unzip Installation Package -->
			<tgz description="Unzip package archive" dest="tmp_out/" name="Unzip Package" src="${yamlConfig["package"]["name"]}.tar.gz" srcLoc="PackageZip"/>
			<!--START SaveDTD-->
			<!--END SaveDTD-->

			<!--START SaveLOB-->
			<!--END SaveLOB-->

			<!--START RESOURCE-->
<#list componentList as component><#if component.cmptTyp == "resource">
			<<#if !component.active>!--</#if>deployResource name="${component.cmptTyp}: ${component.cmptName}" destLoc="CFDataModel" src="tmp_out${component.cmptPath}" dest="${component.cmptOrchPath}" encoding="UTF-8"/<#if !component.active>--</#if>>
</#if></#list>
			<!--deployResource name="MDX: GC_BNP_ACCT" destLoc="CFDataModel" src="tmp_out/mapping/NIKKO/BNP/GC_BNP_ACCT.mdx" dest="mapping/NIKKO/BNP" encoding="windows-1252"/-->
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
			<!--END Workflow-->

			<!--START Event-->
			<!--END Event-->

			<!--START Task-->
			<!--END Task-->

			<!--START DDL-->
<#list componentList as component><#if component.cmptTyp == "ddl">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["package"]["version"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--sql name="DML: 0010-ENTR" src="tmp_out/sql/DML/0010-ENTR.sql" destLoc="GSDMDataModel" patchLevel="8.7.2.01" dbDialect="ORACLE"/-->
			<!--END DDL-->

			<!--START PLSQL-->
			<!--END PLSQL-->

			<!--START DML-->
<#list componentList as component><#if component.cmptTyp == "dml">
			<<#if !component.active>!--</#if>sql name="${component.cmptTyp}: ${component.cmptName}" src="tmp_out${component.cmptPath}" destLoc="GSDMDataModel" patchLevel="${yamlConfig["package"]["version"]}" dbDialect="ORACLE"/<#if !component.active>--</#if>>
</#if></#list>
			<!--END DML-->

			<!--START GSO-->
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
