#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog/front
cd BLOG_REPOSITORY

sudo docker run \
-v $PWD/:/srv/jekyll -v $PWD/_site:/srv/jekyll/_site \
jekyll/builder:latest /bin/bash -c "chmod 755 /srv/jekyll && jekyll build"
