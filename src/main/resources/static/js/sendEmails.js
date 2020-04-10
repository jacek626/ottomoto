function sentMessageToSeller(announcementId, email, messageText, sellerEmailAddress) {
    $.ajax({
        type : "GET",
        url : "/otomoto/announcement/sentMessageToSeller",
        timeout : 15000,
        data: {
            'announcementId': announcementId,
            'messageText' : messageText,
            'email' : email,
            'sellerEmailAddress' : sellerEmailAddress
        },
        success : function(result) {
            if(result)
                showInfo('Wiadomosc wyslana, dziekujemy za kontakt');
            else
                showInfo('Przepraszamy, wystapil blad');
        },
        error : function(result) {
            showInfo('Przepraszamy, wystapil blad');
            console.log("ERROR: ",  result);
        }
    });

    $('#messageToSenderTable input').attr('disabled','true');
    $('#messageToSenderTable textarea').attr('disabled','true');
    showInfo('Wiadomosc do sprzedajacego w trakcie wyslki');
}

function reportAnnouncment(announcementId, reportText, email) {
    $.ajax({
        type : "GET",
        url : "/otomoto/announcement/reportAnnouncement",
        timeout : 15000,
        data: {
            'announcementId': announcementId,
            'reportText' : reportText,
            'email' : email
        },
        success : function(result) {
            $('#reporterEmailAddress').val('');
            $('#reportText').val('');
            showInfo('Wiadomosc wyslana, dziekujemy za kontakt');

        },
        error : function(result) {
            showInfo('Przepraszamy, wystapil blad');
            console.log("ERROR: ",  result);
        }
    });

    document.getElementById('reportAnnouncementPopup').style.visibility = 'hidden';
    hidePopupBacground();
    showInfo('Wiadomosc w trakcie wyslki');
}

function validateEmailAddressAndTextBeforeSend(email,text) {
    if (email[0].checkValidity()) {
        email.removeClass('elementValidationError');
    } else {
        email.addClass('elementValidationError');
        return false;
    }
    if (text[0].checkValidity()) {
        text.removeClass('elementValidationError');
    } else {
        text.addClass('elementValidationError');
        return false;
    }

    return true;
}

function validateEmailAndReportTextInAnnouncementReport() {
    if ($('#reporterEmailAddress')[0].checkValidity()) {
        $('#reporterEmailAddress').removeClass('elementValidationError');
    } else {
        $('#reporterEmailAddress').addClass('elementValidationError');
        return false;
    }
    if ($('#reportText')[0].checkValidity()) {
        $('#reportText').removeClass('elementValidationError');
    } else {
        $('#reportText').addClass('elementValidationError');
        return false;
    }

    return true;
}


function showPopupBacground() {
    document.getElementById('popupBackground').style.visibility = 'visible';
}
function hidePopupBacground() {
    document.getElementById('popupBackground').style.visibility = 'hidden';
}
