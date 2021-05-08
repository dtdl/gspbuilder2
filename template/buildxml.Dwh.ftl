<project name="j4fe" default="build-all">
	<!-- Specify all paths here, also only specify relative paths -->
	<property name="build.dir" value="."/>
	<!--<property location="." file="${yamlConfig["ant"]["dir"]["base"]}/build.properties"/>-->

	<target name="prepare">
		<tstamp>
			<format property="today" pattern="yyyyMMdd" locale="en,UK"/>
		</tstamp>
	</target>
	
	<target name="makedir" depends="prepare">
		<echo message="Base Directory : ${yamlConfig["ant"]["dir"]["base"]}"/>
		<echo message="Packages Directory : ${yamlConfig["ant"]["dir"]["package"]}"/>
		<mkdir dir="${yamlConfig["ant"]["dir"]["temp"]}"/>
		<mkdir dir="${yamlConfig["ant"]["dir"]["package"]}"/>
		<mkdir dir="${yamlConfig["ant"]["dir"]["package"]}/${r"${today}"}"/>
	</target>

	<target name="clean">
		<delete includeemptydirs="true">
			<fileset dir="${yamlConfig["ant"]["dir"]["temp"]}" includes="**/*"/>
		</delete>
	</target>

	<!-- packaging GC components -->
	<target name="build.customgc">
		<!--START SaveDTD-->
<#list componentList as component><#if component.cmptTyp == "saveDtd">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END SaveDTD-->


		<!--START SaveLOB-->
<#list componentList as component><#if component.cmptTyp == "saveLob">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END SaveLOB-->


		<!--START Resource-->
<#list componentList as component><#if component.cmptTyp == "resource">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END Resource-->


		<!--START XMLFeed-->
		<!--END XMLFeed-->


		<!--START XSLT-->
		<!--END XSLT-->


		<!--START VendorDefinition-->
<#list componentList as component><#if component.cmptTyp == "vendordifinition">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END VendorDefinition-->


		<!--START BusinessFeed-->
		<!--END BusinessFeed-->


		<!--START MessageType-->
		<!--END MessageType-->


		<!--START Workflow-->
<#list componentList as component><#if component.cmptTyp == "workflow">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END Workflow-->


		<!--START Event-->
<#list componentList as component><#if component.cmptTyp == "event">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END Event-->


		<!--START Task-->
<#list componentList as component><#if component.cmptTyp == "task">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END Task-->


		<!--START DDL-->
<#list componentList as component><#if component.cmptTyp == "ddl">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END DDL-->


		<!--START PLSQL-->
<#list componentList as component><#if component.cmptTyp == "plsql">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END PLSQL-->


		<!--START DML-->
<#list componentList as component><#if component.cmptTyp == "dml">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END DML-->


		<!--START GSO-->
<#list componentList as component><#if component.cmptTyp == "gso">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END GSO-->


		<!--START GOC-->
		<!--END GOC-->


		<!--START PublishingProfile-->
<#list componentList as component><#if component.cmptTyp == "publishprofile">
		<<#if !component.active>!--</#if>copy file="${yamlConfig["ant"]["dir"]["configuration"]}${component.cmptPath}" todir="${yamlConfig["ant"]["dir"]["temp"]}${component.cmptFolder}"/<#if !component.active>--</#if>>
</#if></#list>
		<!--END PublishingProfile-->

		<tar destfile="${yamlConfig["ant"]["dir"]["temp"]}/${yamlConfig["target"]["dwh"]["name"]}.tar.gz" compression="gzip">
			<tarfileset dir="${yamlConfig["ant"]["dir"]["temp"]}">
				<include name="**"/>
			</tarfileset>
		</tar>
		
		<!-- move the sfile out of the temporary directory so they do not get packaged in the tar.gz file -->
		<copy file="./PackageDescription.xml" tofile="${yamlConfig["ant"]["dir"]["temp"]}/PackageDescription.xml"/>
		<copy file="./ReleaseNotes.docx" tofile="${yamlConfig["ant"]["dir"]["temp"]}/ReleaseNotes.docx" failonerror="false"/>

		<!-- zip the files together with the release notes and the package description -->
		<zip destfile="${yamlConfig["ant"]["dir"]["temp"]}/${yamlConfig["target"]["dwh"]["name"]}_${yamlConfig["target"]["dwh"]["baseversion"]}.${buildVersion}.zip">
			<fileset dir="${yamlConfig["ant"]["dir"]["temp"]}/">
				<include name="${yamlConfig["target"]["dwh"]["name"]}.tar.gz"/>
				<include name="PackageDescription.xml"/>
				<include name="ReleaseNotes.docx"/>
			</fileset>
		</zip>

		<move file="${yamlConfig["ant"]["dir"]["temp"]}/${yamlConfig["target"]["dwh"]["name"]}_${yamlConfig["target"]["dwh"]["baseversion"]}.${buildVersion}.zip" todir="${yamlConfig["ant"]["dir"]["package"]}/${r"${today}"}"/>
	</target>

	<target name="removetempdir">
		<delete dir="${yamlConfig["ant"]["dir"]["temp"]}"/>
	</target>
	

	<target name="build-all" depends="makedir,clean,build.customgc,removetempdir">
	</target>
</project>
