#Open API Contract Plugin
## How to use locally
- Run `./gradlew clean build publishToMavenLocal` to publish to local maven repo
- Need a local nexus instance to work: run this: `docker run -dit -p 8081:8081 --name nexus sonatype/nexus3` then change admin password to gradle.properties in your service

Sample:
```aidl
nexusUser=admin
nexusPassword=123456x@X
nexusHost=http://localhost:8081/repository
```
