#!/usr/bin/env bash

REPOSITORY=/etc/home/c4k-blog
cd $REPOSITORY

docker run -it --rm \
jekyll/jekyll \
jekyll build
