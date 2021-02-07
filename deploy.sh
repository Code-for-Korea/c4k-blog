#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog/blog
cd BLOG_REPOSITORY

docker run -i --rm \
jekyll/jekyll \
jekyll build




EDITOR_REPOSITORY=/home/ec2-user/c4k-blog
PROJECT_NAME=editor


echo "> Check pid of Running Application"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME})
echo "> pid: $CURRENT_PID"

echo "> kill -15 $CURRENT_PID"
sudo kill -15 $CURRENT_PID
sleep 5

echo "> Start Project Build"
cd $EDITOR_REPOSITORY/$PROJECT_NAME/
sudo chmod 755 gradlew
sudo ./gradlew build

echo "> step1. Change Directory"
cd $EDITOR_REPOSITORY

echo "> Copy Build File"
sudo cp $EDITOR_REPOSITORY/$PROJECT_NAME/build/libs/*.jar $EDITOR_REPOSITORY/


echo "> Deploy New Application"

JAR_NAME=$(ls -tr $EDITOR_REPOSITORY/ | grep "\.jar$" | tail -n 1)

echo "> JAR Name: $JAR_NAME"


sudo nohup java -jar \
    $EDITOR_REPOSITORY/$JAR_NAME > sudo $EDITOR_REPOSITORY/editor_nohup.out 2>&1 &