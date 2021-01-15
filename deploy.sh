#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/c4k-blog
cd $REPOSITORY

docker run -it --rm \
jekyll/jekyll \
jekyll build
