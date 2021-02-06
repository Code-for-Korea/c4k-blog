#!/usr/bin/env bash

BLOG_REPOSITORY=/home/ec2-user/c4k-blog/blog
cd BLOG_REPOSITORY

docker run -i --rm \
jekyll/jekyll \
jekyll build



EDITOR_API_DEPLOY=/home/ec2-user/deploy_blog_api.sh
/bin/bash $EDITOR_API_DEPLOY