FROM releases-docker.jfrog.io/jfrog/artifactory-pro:7.117.14
USER root
COPY --chown=artifactory:artifactory ./ArtifactoryAgent-1.0-790fix-SNAPSHOT-all.jar /opt/jfrog/artifactory/app/artifactory/ArtifactoryAgent.jar
COPY --chown=artifactory:artifactory ./setenv.sh /opt/jfrog/artifactory/app/artifactory/tomcat/bin/setenv.sh
