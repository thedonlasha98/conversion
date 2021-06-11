# conversion

#For Bog TNS
spring.datasource.url=jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.45.50.12)(PORT = 1521)))(CONNECT_DATA = (SERVICE_NAME = XL04)))
spring.datasource.username=thedonlasha
spring.datasource.password=lasha111
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

#for Bog Oracle Dependencies
<dependency>
            <groupId>ge.bog.commons</groupId>
            <artifactId>jdbc-encoding-wrapper</artifactId>
            <version>1.2-6</version>
        </dependency>
        <dependency>
            <groupId>ge.bog.oracle</groupId>
            <artifactId>bog-orai18n-mapping</artifactId>
            <version>1.0</version>
        </dependency>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>orai18n</artifactId>
            <version>11.1.0.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>orai18n-mapping</artifactId>
            <version>11.1.0.6.0</version>
        </dependency>
        <!-- -->
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc6</artifactId>
            <version>11.2.0.4</version>
        </dependency>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ucp</artifactId>
            <version>11.2.0.4</version>
        </dependency>
