---
title: "support로 보낸 이메일이 root로 포워딩되는 현상"
author:
  id: "44586666"
  login: "960813"
  name: "Taeyang Jin"
  avatar_url: "https://avatars.githubusercontent.com/u/44586666?v=4"
  bio: "모든 잘못은 전적으로 우리 프로그래머에게 있다."
date: "2021-03-02 23:37:06"
categories: [기술,  Linux]
tags: [Linux,   postfix,   dovecot,   ses,   aws,   이메일]
---
## 개요
Code for Korea의 이메일 서버는 Amazon SES를 활용하고 있다.
* 발신: Amazon SES with postfix on EC2
* 수신: dovecot on EC2


정말 잘 사용하고 있었는데 2021-03-02 저녁, support@example.com으로 보내진 이메일이 수신되지 않는 문제를 발견했다.

## 문제의 파악
1. 로그 확인
    ```
    postfix/local[29515]: DD532671A6: to=<root@example.com>, orig_to=<support@example.com>, relay=local, delay=0.02, delays=0.01/0.01/0/0, dsn=2.0.0, status=sent (delivered to maildir)
    ```
    orig_to는 support이지만, 실제 수신(to)은 root인 것을 확인할 수 있었다.
2. 다른 계정에서도 동일한 문제가 발생하는가? => `No.` 다른 계정은 모두 정상적으로 작동했다.
3. 그렇다면 왜 이런 문제가 발생했는가..

## 문제의 해결
`/etc/aliases`를 확인 해보니 support가 postmaster로 alias 되어 있고, postmaster는 root로 alias 되어 있음을 확인할 수 있었다.

![2021-03-02-Wcn9k.png](/assets/img/posts/2021-03-02/2021-03-02-Wcn9k.png)

즉, support->postmaster->root로 설정된 alias 때문에 support@example.com으로 보내진 이메일이 root@example.com으로 전달된 것.

support -> postmaster alias를 주석 처리함으로써 문제를 해결할 수 있었다.

이는 리눅스 기본 설정이지만 미처 숙지하지 못한 부분이었으며, 이 외에도 기본적인 alias가 많이 존재하는 것으로 보인다.

이후에는 이러한 이슈가 재발하지 않도록 기록을 남긴다.
