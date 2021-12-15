function showInfo(message) {
    $("#infoText div").text(message);
    $("#infoText").show();

    setTimeout(function () {
        $("#infoText").hide();
    }, 5000);
}

function showError(error) {
    showInfo("Sorry, there was an error " + error);
}