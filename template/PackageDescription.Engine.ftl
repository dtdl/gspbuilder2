<?xml version="1.0" ?>
<PackageDescription minInstallCenterVersion="${yamlConfig["package"]["baseversion"]}" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="PackageDescription.xsd">
    <Package name="${yamlConfig["package"]["enginename"]}" type="${yamlConfig["package"]["type"]}" version="${yamlConfig["package"]["version"]}">
        <Description>${yamlConfig["package"]["enginename"]}</Description>
        <Component>${yamlConfig["package"]["enginename"]}</Component>
        <Content>
            <File path="${yamlConfig["package"]["enginename"]}.tar.gz" type="Package"/>            
        </Content>
    </Package>
    <Deployment>
        <Prerequisites>
            <Product id="BaseComp" name="Base Components" type="GOLDENSOURCE">
                <Version min="${yamlConfig["package"]["baseversion"]}"/>
            </Product>
            <Product id="EngineConfig" name="Reference Engine Default Configuration" type="GOLDENSOURCE">
                <Version min="${yamlConfig["package"]["baseversion"]}"/>
            </Product>            
            <Product id="datamodel" name="Datamodel GSDM" type="GOLDENSOURCE">
                <Version min="${yamlConfig["package"]["baseversion"]}"/>
            </Product>
            <Product id="datamodelvddb" name="Datamodel VDDB" type="GOLDENSOURCE">
                <Version min="${yamlConfig["package"]["baseversion"]}"/>
            </Product>			
            <Product id="starterset" name="Starterset GSDM VDDB" type="GOLDENSOURCE">
            	<Version min="${yamlConfig["package"]["baseversion"]}"/>
        	</Product>
        	<Product id="startersetEXM" name="Starterset Exception Management" type="GOLDENSOURCE">
            	<Version min="${yamlConfig["package"]["baseversion"]}"/>
        	</Product>
            <!-- DATABASE CLIENT/SERVER -->
            <Product id="Ora11Server" name="Oracle Server Version 11.2.0" type="ORACLE_SERVER">
            	<Version max="12.2.x.x.x" min="12.0.x.x.x"/>
            </Product>
            <Product id="Oracle112" name="Oracle Client Version 11.2.0" type="ORACLE_CLIENT">
	            <Version max="12.2.x.x.x" min="12.0.x.x.x"/>
            </Product>  
            
            <!-- OPERATING SYSTEM -->
            <Product id="LINUX_2.6" name="Linux with Kernel 3.10.x" type="OS_LINUX">
				<Version max="3.10.x" min="3.10.0"/>
			</Product>
            
        </Prerequisites>

		
        <Locations>
            <Location id="ReferenceInstall" type="EngineInstallation">
                <Description>Filesystem location where the engine should be installed.</Description>
                <Prerequisites>
                    <and name="ReferencePrerequisites">
                        <Prerequisite id="BaseComp"/>
                        <Prerequisite description="Certified Environment" id="LINUX_2.6"/>
                        <Prerequisite id="Oracle112"/>
                    </and>
                </Prerequisites>
            </Location>
            <Location id="DataModel" multiple="true" type="Database">
                <Description>The database where the Datamodel was installed.</Description>
                <Prerequisites>
                    <Prerequisite id="EngineConfig"/>
                    <Prerequisite id="Ora11Server"/>                  
                    <or name="GSDM Datamodel" required="true">
                        <Prerequisite id="datamodel"/>
                        <Prerequisite id="datamodelvddb"/>                        
                    </or>                    
					<or name="BaseStarterSet" required="true">
						<Prerequisite id="starterset"/>						
					</or>
                    <Prerequisite id="startersetEXM"/>
                </Prerequisites>
            </Location>
        </Locations>

        <Tasks>
            <tgz description="Unzip package archive" destLoc="ReferenceInstall" name="Unzip Package" src="${yamlConfig["package"]["enginename"]}.tar.gz" srcLoc="PackageZip"/>
        </Tasks>
    </Deployment>
</PackageDescription>
