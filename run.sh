CP=usher.jar
CP=$CP:$(echo lib/*.jar | tr ' ' ':')

java -Xmx24G -cp $CP $@ 
