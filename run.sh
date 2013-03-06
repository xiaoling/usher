#CP=usher.jar
#CP=$CP:$(echo lib/*.jar | tr ' ' ':')

#java -Xmx24G -cp $CP $@
MAVEN_OPTS=-Xmx24g
echo $MAVEN_OPTS 
mvn package exec:java -Dexec.mainClass="el.Main" -Dexec.args=" all"
