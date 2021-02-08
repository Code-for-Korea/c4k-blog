#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog-front
cd BLOG_REPOSITORY

sudo docker run --name blog --volume="$PWD:/srv/jekyll" -it jekyll/jekyll jekyll build

