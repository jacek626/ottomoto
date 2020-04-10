function checkEmailAlreadyExists(email,id) {
    $.ajax({
        type : "POST",
        url : "/otomoto/user/checkEmailAlreadyExists",
        timeout : 5000,
        data: {
            'id': id,
            'email': email
        },
        success : function(result) {
            if(result == true)
                $('#emailAlreadyExists').css('display','block');
            else
                $('#emailAlreadyExists').css('display','none');
        },
        error : function(result) {
            console.error("checkEmailAlreadyExists: ",  result);
        }
    });
}

function checkLoginAlreadyExists(login) {
    $.ajax({
        type : "POST",
        url : "/otomoto/user/checkLoginAlreadyExists",
        timeout : 5000,
        data: {
            'login': login
        },
        success : function(result) {
            if(result == true)
                $('#loginAlreadyExists').css('display','block');
            else
                $('#loginAlreadyExists').css('display','none');
        },
        error : function(result) {
            console.error("checkLoginAlreadyExists: ",  result);
        }
    });
}
