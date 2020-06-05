function checkEmailAlreadyExists(email, id) {
    $.ajax({
        type: "GET",
        url: "/otomoto/user/checkEmailAlreadyExists",
        timeout: 5000,
        data: {
            id,
            email
        },
        success(result) {
            if (result === true) {
                $("#emailAlreadyExists").css("display", "block");
            } else {
                $("#emailAlreadyExists").css("display", "none");
            }
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}

function checkLoginAlreadyExists(login) {
    $.ajax({
        type: "GET",
        url: "/otomoto/user/checkLoginAlreadyExists",
        timeout: 5000,
        data: {
            login
        },
        success(result) {
            if (result === true) {
                $("#loginAlreadyExists").css("display", "block");
            } else {
                $("#loginAlreadyExists").css("display", "none");
            }
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}
