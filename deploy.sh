#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog/blog
cd BLOG_REPOSITORY

docker run -i --rm \
jekyll/jekyll \
jekyll build