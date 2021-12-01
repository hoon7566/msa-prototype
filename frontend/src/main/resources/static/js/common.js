let getCookie = function(name) {
    let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
};

let setCookie = function(name, value, day) {
    let date = new Date();
    date.setTime(date.getTime() + day * 60 * 60 * 24 * 1000);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};

let delCookie = function(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
}

let userInfo;

let loginByCookie = function (accessToken){
    $.ajax({
        type: 'get'
        , async: true
        , url: '/user-service/token'
        ,dataType : "text"
        , beforeSend: function(xhr) {
            xhr.setRequestHeader("Content-type","application/json; charset=utf-8");
        }
        , success: function (ajaxData, status, request) {
            ajaxData = JSON.parse(ajaxData);
            userInfo = {userId:ajaxData.userId,name:ajaxData.name}
            console.log(ajaxData,status,request)
            changeNav();
            //alert("로그인 성공");
        }
        , error: function(request, status, err) {
            console.log(status,request,err);

        }
        , complete: function(request, status) {

        }
    });

}