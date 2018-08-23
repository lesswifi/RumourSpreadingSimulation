if [ $# -eq 1 ]
then
	javac $(find ./src/* | grep .java) -d "./build" -cp ".:./jars/*"

else
    java -cp "./build:./jars/*" Plot $1 $2 $3
fi
