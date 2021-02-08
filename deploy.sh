#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog-front
cd BLOG_REPOSITORY

sudo docker run --volume="$PWD:/srv/jekyll" jekyll/jekyll jekyll build

