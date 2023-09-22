if pgrep -f de.parett.collage.App
then
	pkill -f de.parett.collage.App
fi
mvn clean compile exec:java -Dexec.mainClass=de.parett.collage.App -Duser.timezone=UTC &
