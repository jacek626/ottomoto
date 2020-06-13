function checkEmailAlreadyExists(email) {
    $.ajax({
        type: "GET",
        url: "/otomoto/user/checkEmailAlreadyExists",
        timeout: 5000,
        data: {
            email: email.value
        },
        success(result) {
            if (result === true) {
                $(email).addClass("is-invalid");
            } else {
                $(email).removeClass("is-invalid");
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
            login: login.value
        },
        success(result) {
            if (result === true) {
                $(login).addClass("is-invalid");
            } else {
                $(login).removeClass("is-invalid");
            }
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}
