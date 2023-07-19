# CartCrafters

## Backend

- `.war` will be compiled each time a file in `src` is saved. Wait a few seconds and the server will reload
- Run `Publish Server (Full)` to update servlet paths

### Initial Setup

1. Install recommended extensions
2. Right-click on `Community Server Connector` > Create New Server > Yes > Apache Tomcat 9.0.41 > Agree
3. Run `mvn clean package` to generate the initial `.war` (in `/target`)
4. Right-click on the `.war` > Debug on Server > apache-tomcat-9.0.41 > No > eecs4413-project
5. Right-clikc on `apache-tomcat-9.0.41` > Publish Server (Full)
6. Go to `http://localhost:8080/eecs4413-project-1.0-SNAPSHOT/`
