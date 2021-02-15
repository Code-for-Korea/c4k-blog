---
title: "RainLoop의 poppassd-change-password 플러그인이 정상적으로 작동하지 않는 문제"
author:
  id: "44586666"
  login: "960813"
  name: "Taeyang Jin"
  avatar_url: "https://avatars.githubusercontent.com/u/44586666?v=4"
  bio: "모든 잘못은 전적으로 우리 프로그래머에게 있다."
date: "2021-02-16 04:27:03"
categories: [기술,  일반]
tags: [poppassd,   리눅스]
---
# 개요

얼마 전 코드포코리아의 이메일 서버를 구축하면서 Webmail client로 RainLoop 최신 릴리즈(게시일 기준 `v1.15.0`)를 설치하게 되었다.

SES로 relay 하는 postfix 세팅과 자체 서버로 이메일을 수신하는 dovecot 세팅까지 모두 마친 상황이었다.

이제 사람들에게 계정을 나눠주면 되는데... 비밀번호는 어떻게 관리해야 하지?

초기 비밀번호를 세팅해주고, 사용자가 직접 변경하게 해주면 되겠지? 라는 생각으로 관련 플러그인을 찾아보게 되었다.

그러던 중 RainLoop의 plugin 중 `poppassd`를 이용해 암호 변경을 지원하는 poppassd-change-password(`v1.1`)를 발견하고 설치하게 되었다.

그런데 여기서 문제가 발생한다.

# 문제의 시작

poppassd 설치하는 것은 전혀 문제가 없었다. (참고 링크: [Install Poppassd in CentOS 7](https://tweenpath.net/install-poppassd-centos-7/))

telnet을 이용해 테스트해봐도 정상 작동한다.

```
telnet localhost 106
user <..>
pass <..>
newpass <..>
```

그런데, 아래와 같은 에러 메시지가 발생한다.

![2021-02-16-7I1Tk.png](/assets/img/posts/2021-02-16/2021-02-16-7I1Tk.png)

## 분석

왜..? 우선 해당 요청에 주요하게 작용하는 파일은 아래와 같았다.

1. `rainloop/v/1.15.0/app/libraries/MailSo/Poppassd/PoppassdClient.php`
2. `data/_data_/_default_/plugins/poppassd-change-password/ChangePasswordPoppassdDrive.php`

코드를 추적해본 결과 2번 파일의 112번 라인에 존재하는 try \~ catch 문에서 Exception이 발생하여 결과가 false로 반환되는 것을 확인했다.

``` php
try
{
 $oPoppassdClient = \MailSo\Poppassd\PoppassdClient::NewInstance();
 if ($this->oLogger instanceof \MailSo\Log\Logger)
 {
 $oPoppassdClient->SetLogger($this->oLogger);
 }

 $oPoppassdClient
 ->Connect($this->sHost, $this->iPort)
 ->Login($oAccount->Login(), $oAccount->Password())
 ->NewPass($sNewPassword)
 ->LogoutAndDisconnect()
 ;

 $bResult = true;
}
catch (\Exception $oException)
{
 $bResult = false;
}
```

왜 Exception이 발생하는 걸까?

poppassd의 데몬인 xinetd의 로그엔 `poppasswd per_source_limit`라고 찍힌다. 그런데 검색해봐도 안 나온다.

그래서 결국 임시 조치로 2번 파일에서 항상 true를 Return 하게끔 수정했다.

```
[기존]
catch (\Exception $oException)
{
 $bResult = false;
}

[변경]
catch (\Exception $oException)
{
 $bResult = true;
}
```

![2021-02-16-ult5y.png](/assets/img/posts/2021-02-16/2021-02-16-ult5y.png)

비밀번호 변경 버튼이 녹색으로 변하며 정상 작동한다.


# 마무리
조금 더 관련 자료를 찾아보고, 문서를 업데이트할 예정이다.