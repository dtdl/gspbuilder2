<project name="j4fe" default="build-all">
	<!-- Specify all paths here, also only specify relative paths -->
	<property name="build.dir" value="."/>
	<property location="." file="${r"${build.dir}"}/build.properties"/>

	<target name="prepare">
		<tstamp>
			<format property="today" pattern="yyyyMMdd" locale="en,UK"/>
		</tstamp>
	</target>
	
	<target name="makedir" depends="prepare">
		<echo message="Base Directory : ${r"${base.dir}"}"/>
		<echo message="Packages Directory : ${r"${packages.dir}"}"/>
		<mkdir dir="${r"${temp.dir}"}"/>
		<mkdir dir="${r"${packages.dir}"}"/>
		<mkdir dir="${r"${packages.dir}/${today}"}"/>
	</target>

	<target name="clean">
		<delete includeemptydirs="true">
			<fileset dir="${r"${temp.dir}"}" includes="**/*"/>
		</delete>
	</target>

	<!-- packaging GC components -->
	<target name="build.customgc">
		<!--START SaveDTD-->
		<!--END SaveDTD-->


		<!--START SaveLOB-->
		<!--END SaveLOB-->


		<!--START MDX-->
		<!--END MDX-->


		<!--START XMLFeed-->
		<!--END XMLFeed-->


		<!--START XSLT-->
		<!--END XSLT-->


		<!--START VendorDefinition-->
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
		<!--END DDL-->


		<!--START PLSQL-->
		<!--END PLSQL-->


		<!--START DML-->
		<!--END DML-->


		<!--START GSO-->
		<!--END GSO-->


		<!--START GOC-->
		<!--END GOC-->


		<!--START PublishingProfile-->
		<!--END PublishingProfile-->

		<tar destfile="${r"${temp.dir}"}/${yamlConfig["target"]["gc"]["name"]}.tar.gz" compression="gzip">
			<tarfileset dir="${r"${temp.dir}"}">
				<include name="**"/>
			</tarfileset>
		</tar>
		
		<!-- move the sfile out of the temporary directory so they do not get packaged in the tar.gz file -->
		<copy file="./PackageDescription.xml" tofile="${r"${temp.dir}"}/PackageDescription.xml"/>
		<copy file="./ReleaseNotes.docx" tofile="${r"${temp.dir}"}/ReleaseNotes.docx" failonerror="false"/>

		<!-- zip the files together with the release notes and the package description -->
		<zip destfile="${r"${temp.dir}"}/${yamlConfig["target"]["gc"]["name"]}_${r"${build.version}"}.zip">
			<fileset dir="${r"${temp.dir}"}/">
				<include name="${yamlConfig["target"]["gc"]["name"]}.tar.gz"/>
				<include name="PackageDescription.xml"/>
				<include name="ReleaseNotes.docx"/>
			</fileset>
		</zip>

		<move file="${r"${temp.dir}"}/${yamlConfig["target"]["gc"]["name"]}_${r"${build.version}"}.zip" todir="${r"${packages.dir}/${today}"}"/>
	</target>

	<target name="removetempdir">
		<delete dir="${r"${temp.dir}"}"/>
	</target>
	

	<target name="build-all" depends="makedir,clean,build.customgc,removetempdir">
	</target>
</project>
