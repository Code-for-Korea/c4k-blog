let editor = null;
let user_profile = {};
const categories = {
    '일반': ['잡담'],
    '행사': ['일반'],
    '공공데이터': ['일반'],
    '시빅해킹': ['일반'],
    '프로젝트': ['Meet & Hack', '개인 안심번호']
};

function addImage(blob) {
    const reader = new FileReader();
    reader.addEventListener("load", function () {
        const regex = /data:(.*\/*);base64,(.*)/g;
        const result = regex.exec(String(reader.result));
        const file_mime = result[1];
        const file_encode = result[2];

        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'https://codefor.kr:8443/api/github/image');
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.withCredentials = true;
        xhr.onreadystatechange = () => {
            if (xhr.readyState === xhr.DONE) {
                if (xhr.status === 200 || xhr.status === 201) {
                    const json = JSON.parse(xhr.responseText);
                    if (json.data.error && json.data.error === true) {
                        alert("에러발생");
                        console.error(json);
                    } else {
                        const prefix = 'https://raw.githubusercontent.com/Code-for-Korea/c4k-blog-front/main/';
                        const file_name = json.data.msg.content.name;
                        const path = json.data.msg.content.path;
                        const markdown = `![${file_name}](${prefix + path})`;

                        if (editor.isMarkdownMode()) {
                            editor.setMarkdown(markdown);
                        } else {
                            editor.changeMode('markdown', true);
                            editor.insertText(markdown);
                            editor.changeMode('wysiwyg', true);
                        }
                        editor.setMarkdown(markdown);
                    }
                } else {
                    console.error(xhr.responseText);
                }
            }
        };

        xhr.send(JSON.stringify({
            "imageEncode": file_encode,
            "MIME": file_mime
        }));

    });
    reader.readAsDataURL(blob);
}

function initEditor() {
    const chartOptions = {
        /*
                     minWidth: 100,
                     maxWidth: 600,
                     minHeight: 100,
                     maxHeight: 300
        */
    };

    const colorSyntaxOptions = {
        /*preset: ['#181818', '#292929', '#393939'],*/
        useCustomSyntax: true
    };

    editor = new toastui.Editor({
        el: document.querySelector('#editor'),
        height: '50vh',
        /*initialEditType: 'markdown',*/
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical',
        plugins: [
            toastui.Editor.plugin.tableMergedCell,
            [toastui.Editor.plugin.chart, chartOptions],
            toastui.Editor.plugin.codeSyntaxHighlight,
            [toastui.Editor.plugin.colorSyntax, colorSyntaxOptions]
        ],
        hooks: {
            addImageBlobHook: addImage
        }
    });
}

function renewing_access_token_with_refresh_token() {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'https://codefor.kr:8443/api/github/oauth');
    xhr.withCredentials = true;
    xhr.onreadystatechange = () => {
        if (xhr.readyState === xhr.DONE) {
            if (xhr.status === 200 || xhr.status === 201) {
                const json = JSON.parse(xhr.responseText);
                if (json.data.error && json.data.error === true) {
                    alert("에러발생");
                    console.error(json);
                } else {
                    create_new_post();
                }
            } else {
                console.error(xhr.responseText);
            }
        }
    };

    xhr.send(null);
}

function create_new_post() {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'https://codefor.kr:8443/api/github/post');
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.withCredentials = true;
    xhr.onreadystatechange = () => {
        if (xhr.readyState === xhr.DONE) {
            if (xhr.status === 200 || xhr.status === 201) {
                const json = JSON.parse(xhr.responseText);
                if (json.data.error && json.data.error === true) {
                    if (json.data.status === 401) {
                        renewing_access_token_with_refresh_token();
                    } else {
                        alert("에러발생");
                        console.error(json);
                    }
                } else {
                    location.reload();
                }
            } else {
                console.error(xhr.responseText);
            }
        }
    };

    xhr.send(JSON.stringify({
        "profile": user_profile,
        "categories": [
            document.getElementById('categories_1st').value,
            document.getElementById('categories_2st').value
        ],
        "content": editor.getMarkdown(),
        "tags": document.getElementById('tags').value.split(','),
        "title": document.getElementById('title').value
    }));
}

function initElements() {
    document.getElementById('btn-write').addEventListener('click', create_new_post);

    document.getElementById('btn-logout').addEventListener('click', () => {
        const xhr = new XMLHttpRequest();
        xhr.open('DELETE', 'https://codefor.kr:8443/api/github/authorize');
        xhr.withCredentials = true;
        xhr.onreadystatechange = () => {
            if (xhr.readyState === xhr.DONE) {
                if (xhr.status === 200 || xhr.status === 201) {
                    const json = JSON.parse(xhr.responseText);
                    console.log(json);
                    location.reload();
                } else {
                    console.error(xhr.responseText);
                }
            }
        };
        xhr.send(null);
    });

    $('#categories_1st').empty();

    $(Object.entries(categories)).each(function (index, item) {
        const option = $(document.createElement('option'));
        option.text(item[0]);
        $('#categories_1st').append(option);
    });
    $('#categories_1st > option').first().attr('selected', 'true');

    document.getElementById('categories_1st').addEventListener('change', category_change);
    category_change();
}

function category_change() {
    const category_1st = $('#categories_1st').val();

    $('#categories_2st').empty();

    $(categories[category_1st]).each(function (index, item) {
        const option = $(document.createElement('option'));
        option.text(item);
        $('#categories_2st').append(option);
    });
}


$(function () {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'https://codefor.kr:8443/api/github/authorize');
    xhr.withCredentials = true;
    xhr.onreadystatechange = () => {
        if (xhr.readyState === xhr.DONE) {
            if (xhr.status === 200 || xhr.status === 201) {
                const json = JSON.parse(xhr.responseText);

                const articles = document.getElementsByTagName('article');
                /*에러가 있으면 OAuth 출력*/
                if (json.data.error && json.data.error === true) {
                    articles[1].className = 'd-block';
                } else {
                    const profile = json.data.profile;

                    user_profile['id'] = profile.id ? profile.id : '';
                    user_profile['login'] = profile.login ? profile.login : '';
                    user_profile['name'] = profile.name ? profile.name : '';
                    user_profile['avatar_url'] = profile.avatar_url ? profile.avatar_url : '';
                    user_profile['bio'] = profile.bio ? profile.bio : '';
                    initEditor();
                    initElements();
                    articles[0].className = 'd-block';
                    const user =
                        user_profile.name === ""
                            ? user_profile.login
                            : user_profile.name + '(' + user_profile.login + ')';
                    document.getElementById('login_user').innerText = 'logged in ' + user;
                }
            } else {
                console.error(JSON.parse(xhr.responseText));
            }
        }
    };

    xhr.send(null);
});