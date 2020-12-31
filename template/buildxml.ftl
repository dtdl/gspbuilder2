<project name="j4fe" default="build-all">
	<!-- Specify all paths here, also only specify relative paths -->
	<property name="build.dir" value="."/>
	<property location="." file="${build.dir}/build.properties"/>

	<target name="makedir" depends="prepare">
		<echo message="Base Directory : ${base.dir}"/>
		<echo message="Packages Directory : ${packages.dir}"/>
		<mkdir dir="${temp.dir}"/>
		<mkdir dir="${packages.dir}"/>
		<mkdir dir="${packages.dir}/${today}"/>
	</target>

	<target name="clean">
		<delete includeemptydirs="true">
			<fileset dir="${temp.dir}" includes="**/*"/>
		</delete>
	</target>

	<target name="prepare">
		<tstamp>
			<format property="today" pattern="yyyyMMdd" locale="en,UK"/>
		</tstamp>
	</target>

	<!-- packaging GC components -->
	<target name="build.customgc">
		<!--START SaveDTD-->
		<!--END SaveDTD-->


		<!--START SaveLOB-->
		<!--END SaveLOB-->


		<!--START MDX-->
		<copy file="${base.dir}/configuration/resources/mapping/NIKKO/BNP/GC_BNP_ISSU_SGFA_NAV.mdx" todir="${temp.dir}/mapping/NIKKO/BNP"/>
		<copy file="${base.dir}/configuration/resources/mapping/NIKKO/BNP/GC_BNP_ISSU_NZFA_Valuation.mdx" todir="${temp.dir}/mapping/NIKKO/BNP"/>
		<copy file="${base.dir}/configuration/resources/mapping/NIKKO/BNP/GC_BNP_ISSU_NZ_MMCPosition.mdx" todir="${temp.dir}/mapping/NIKKO/BNP"/>
		<copy file="${base.dir}/configuration/resources/mapping/NIKKO/BNP/GC_BNP_ISSU_SGFA_INVENTORY.mdx" todir="${temp.dir}/mapping/NIKKO/BNP"/>
		<copy file="${base.dir}/configuration/resources/mapping/NIKKO/BNP/GC_BNP_ACCT.mdx" todir="${temp.dir}/mapping/NIKKO/BNP"/>
		<!--END MDX-->


		<!--START XMLFeed-->
		<copy file="${base.dir}/configuration/resources/xml/feeds/NIKKO/LineByLineCSTM.xml" todir="${temp.dir}/xml/feeds/NIKKO"/>
		<!--END XMLFeed-->


		<!--START XSLT-->
		<!--END XSLT-->


		<!--START VendorDefinition-->
		<copy file="${base.dir}/configuration/vendordefinitions/NIKKO_CSV.gsp" todir="${temp.dir}/vendordefinitions"/>
		<copy file="${base.dir}/configuration/vendordefinitions/NIKKO_CSV_SKIPHEADER.gsp" todir="${temp.dir}/vendordefinitions"/>
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
		<copy file="${base.dir}/configuration/sql/DML/0010-ENTR.sql" todir="${temp.dir}/sql/DML"/>
		<!--END DML-->


		<!--START GSO-->
		<!--END GSO-->


		<!--START GOC-->
		<!--END GOC-->


		<!--START PublishingProfile-->
		<!--END PublishingProfile-->


		<copy todir="${temp.dir}/sql">
			<fileset dir="${base.dir}/configuration/sql"/>
		</copy>		<!--Custom Workstation Code-->
		<copy todir="${temp.dir}/workstation">
		<fileset dir="${base.dir}/workstation"/>
		</copy>		<!-- tar the copied files -->
		<tar destfile="${temp.dir}/${build.package.name}.tar.gz" compression="gzip">
			<tarfileset dir="${temp.dir}">
				<include name="**"/>
			</tarfileset>
		</tar>
		
		<!-- move the sfile out of the temporary directory so they do not get packaged in the tar.gz file -->
		<copy file="./PackageDescription.xml" tofile="${temp.dir}/PackageDescription.xml"/>
		<copy file="./ReleaseNotes.docx" tofile="${temp.dir}/ReleaseNotes.docx" failonerror="false"/>

		<!-- zip the files together with the release notes and the package description -->
		<zip destfile="${temp.dir}/${build.package.name}_${build.version}.zip">
			<fileset dir="${temp.dir}/">
				<include name="${build.package.name}.tar.gz"/>
				<include name="PackageDescription.xml"/>
				<include name="ReleaseNotes.docx"/>
			</fileset>
		</zip>

		<move file="${temp.dir}/${build.package.name}_${build.version}.zip" todir="${packages.dir}/${today}"/>
	</target>

	<target name="removetempdir">
		<delete dir="${temp.dir}"/>
	</target>
	

	<target name="build.engines">
	<copy file="${base.dir}/engines/ReferenceEngine/lib/NIKKOCustomGCRule.jar" todir="${temp.dir}/engines/ReferenceEngine/BRE/java"/> 
	<tar destfile="${temp.dir}/engines/${build.package.name}_Engines.tar.gz" compression="gzip">
			<tarfileset dir="${temp.dir}/engines">
			</tarfileset>
		</tar>
		
		<copy file="./PackageDescription_Engines.xml" tofile="${temp.dir}/engines/PackageDescription.xml"/>
		<copy file="./ReleaseNotes_Engines.docx" tofile="${temp.dir}/engines/ReleaseNotes.docx" failonerror="false"/>
		
		<!-- zip the files together with the release notes and the package description -->
		<zip destfile="${temp.dir}/engines/${build.package.name}_Engines_${build.version}.zip">
			<fileset dir="${temp.dir}/engines/">
				<include name="*${build.package.name}_Engines.tar.gz"/>
				<include name="*PackageDescription.xml"/>
				<include name="*ReleaseNotes.docx"/>
			</fileset>
		</zip>		
		
		<move file="${temp.dir}/engines/${build.package.name}_Engines_${build.version}.zip" todir="${packages.dir}/${today}"/>
	</target>	

	<target name="build-all" depends="makedir,clean,build.customgc,build.engines,removetempdir">
	</target>
</project>
