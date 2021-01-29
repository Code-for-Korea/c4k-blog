#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/c4k-blog/blog
cd $REPOSITORY

docker run -i --rm \
jekyll/jekyll \
jekyll build
