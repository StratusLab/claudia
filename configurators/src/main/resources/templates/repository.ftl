<?xml version="1.0"?>
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

<!DOCTYPE Repository
          PUBLIC "-//The Apache Software Foundation//DTD Jackrabbit 2.0//EN"
          "http://jackrabbit.apache.org/dtd/repository-2.0.dtd">

<!-- Example Repository Configuration File
     Used by
     - org.apache.jackrabbit.core.config.RepositoryConfigTest.java
     -
-->
<Repository>
    <!--
        virtual file system where the repository stores global state
        (e.g. registered namespaces, custom node types, etc.)
    -->
    <!--
	<FileSystem class="org.apache.jackrabbit.core.fs.local.LocalFileSystem">
        <param name="path" value="${'$'}{rep.home}/repository"/>
    </FileSystem>
	-->
	
	<FileSystem class="org.apache.jackrabbit.core.fs.db.DbFileSystem">
		<param name="driver" value="org.postgresql.Driver"/>
		<param name="url" value="jdbc:postgresql://${ipDB}:5432/tcloudss" />
		<param name="user" value="${user}" />
		<param name="password" value="${password}" />
		<param name="schema" value="postgresql"/>
		<param name="schemaObjectPrefix" value="J_R_FS_"/>
	</FileSystem>

    <!--
        data store configuration
    -->
    <!--
	<DataStore class="org.apache.jackrabbit.core.data.FileDataStore"/>
	-->
	
	<DataStore class="org.apache.jackrabbit.core.data.db.DbDataStore">
		<param name="url" value="jdbc:postgresql://${ipDB}:5432/tcloudss" />
		<param name="user" value="${user}" />
		<param name="password" value="${password}" />
        <param name="databaseType" value="postgresql" />
        <param name="driver" value="org.postgresql.Driver" />
        <param name="minRecordLength" value="1024" />
        <param name="maxConnections" value="3" />
        <param name="copyWhenReading" value="true" />
    </DataStore>

    <!--
        security configuration
    -->
    <Security appName="Jackrabbit">
        <!--
            security manager:
            class: FQN of class implementing the JackrabbitSecurityManager interface
        -->
        <SecurityManager class="org.apache.jackrabbit.core.security.simple.SimpleSecurityManager" workspaceName="security">
            <!--
            workspace access:
            class: FQN of class implementing the WorkspaceAccessManager interface
            -->
            <!-- <WorkspaceAccessManager class="..."/> -->
            <!-- <param name="config" value="${'$'}{rep.home}/security.xml"/> -->
        </SecurityManager>

        <!--
            access manager:
            class: FQN of class implementing the AccessManager interface
        -->
        <AccessManager class="org.apache.jackrabbit.core.security.simple.SimpleAccessManager">
            <!-- <param name="config" value="${'$'}{rep.home}/access.xml"/> -->
        </AccessManager>

        <LoginModule class="org.apache.jackrabbit.core.security.simple.SimpleLoginModule">
           <!-- 
              anonymous user name ('anonymous' is the default value)
            -->
           <param name="anonymousId" value="anonymous"/>
           <!--
              administrator user id (default value if param is missing is 'admin')
            -->
           <param name="adminId" value="admin"/>
        </LoginModule>
    </Security>

    <!--
        location of workspaces root directory and name of default workspace
    -->
    <Workspaces rootPath="${'$'}{rep.home}/workspaces" defaultWorkspace="default"/>
    <!--
        workspace configuration template:
        used to create the initial workspace if there's no workspace yet
    -->
    <Workspace name="${'$'}{'$'}{wsp.name}">
        <!--
            virtual file system of the workspace:
            class: FQN of class implementing the FileSystem interface
        -->
		<!--
        <FileSystem class="org.apache.jackrabbit.core.fs.local.LocalFileSystem">
            <param name="path" value="${'$'}{wsp.home}"/>
        </FileSystem>
		-->
		
		<FileSystem class="org.apache.jackrabbit.core.fs.db.DbFileSystem">
			<param name="driver" value="org.postgresql.Driver"/>
			<param name="url" value="jdbc:postgresql://${ipDB}:5432/tcloudss" />
			<param name="user" value="${user}" />
			<param name="password" value="${password}" />
			<param name="schema" value="postgresql"/>
			<param name="schemaObjectPrefix" value="J_FS_${'$'}{wsp.name}_"/>
		</FileSystem>
        <!--
            persistence manager of the workspace:
            class: FQN of class implementing the PersistenceManager interface
        -->
		<!--
        <PersistenceManager class="org.apache.jackrabbit.core.persistence.pool.DerbyPersistenceManager">
          <param name="url" value="jdbc:derby:${'$'}{wsp.home}/db;create=true"/>
          <param name="schemaObjectPrefix" value="${'$'}{wsp.name}_"/>
        </PersistenceManager>
		-->
		
		<PersistenceManager class="org.apache.jackrabbit.core.persistence.bundle.PostgreSQLPersistenceManager">
			<param name="url" value="jdbc:postgresql://${ipDB}:5432/tcloudss"/>
			<param name="user" value="${user}" />
			<param name="password" value="${password}" />
			<param name="schemaObjectPrefix" value="J_PM_${'$'}{wsp.name}_"/>
			<param name="externalBLOBs" value="false"/>
		</PersistenceManager>
		
        <!--
            Search index and the file system it uses.
            class: FQN of class implementing the QueryHandler interface
        -->
        <SearchIndex class="org.apache.jackrabbit.core.query.lucene.SearchIndex">
            <param name="path" value="${'$'}{wsp.home}/index"/>
            <param name="supportHighlighting" value="true"/>
        </SearchIndex>
    </Workspace>

    <!--
        Configures the versioning
    -->
    <Versioning rootPath="${'$'}{rep.home}/version">
        <!--
            Configures the filesystem to use for versioning for the respective
            persistence manager
        -->
        <FileSystem class="org.apache.jackrabbit.core.fs.local.LocalFileSystem">
            <param name="path" value="${'$'}{rep.home}/version" />
        </FileSystem>

        <!--
            Configures the persistence manager to be used for persisting version state.
            Please note that the current versioning implementation is based on
            a 'normal' persistence manager, but this could change in future
            implementations.
        -->
        <!--
		<PersistenceManager class="org.apache.jackrabbit.core.persistence.pool.DerbyPersistenceManager">
          <param name="url" value="jdbc:derby:${'$'}{rep.home}/version/db;create=true"/>
          <param name="schemaObjectPrefix" value="version_"/>
        </PersistenceManager>
		-->
		<PersistenceManager class="org.apache.jackrabbit.core.persistence.bundle.PostgreSQLPersistenceManager">
			<param name="url" value="dbc:postgresql://${ipDB}:5432/tcloudss"/>
			<param name="user" value="${user}" />
			<param name="password" value="${password}" />
            <param name="schemaObjectPrefix" value="J_V_PM_"/>
            <param name="externalBLOBs" value="false"/>
		</PersistenceManager>
    </Versioning>

    <!--
        Search index for content that is shared repository wide
        (/jcr:system tree, contains mainly versions)
    -->
    <SearchIndex class="org.apache.jackrabbit.core.query.lucene.SearchIndex">
        <param name="path" value="${'$'}{rep.home}/repository/index"/>
        <param name="supportHighlighting" value="true"/>
    </SearchIndex>
	
	<!--    Cluster Configuration   -->
        
	<Cluster id="node2" syncDelay="2000">
		<Journal class="org.apache.jackrabbit.core.journal.DatabaseJournal">
			<param name="revision" value="${'$'}{rep.home}/revision"/>
			<param name="driver" value="org.postgresql.Driver"/>
			<param name="url" value="jdbc:postgresql://${ipDB}:5432/tcloudss" />
			<param name="user" value="${user}" />
			<param name="password" value="${password}" />
			<param name="schema" value="postgresql"/>
			<param name="schemaObjectPrefix" value="J_C_"/>
		</Journal>
	</Cluster>
</Repository>
