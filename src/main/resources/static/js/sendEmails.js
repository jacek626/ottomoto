function validateEmail(email) {
    let re = /\S+@\S+\.\S+/;
    return re.test(email);
}

function sentMessageToSeller(customerEmail, messageText, button) {

    if (!validateEmail(customerEmail.value))
        customerEmail.classList.add('is-invalid');
    else if (messageText.value.length < 5)
        messageText.classList.add('is-invalid');
    else
        fetch(`/otomoto/announcement/sentMessageToSeller`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                messageText: messageText.value,
                customerEmail: customerEmail.value,
                sellerEmail: button.getAttribute('sellerEmail'),
                announcementId: button.getAttribute('announcementId')
            })
        })
            .then(response => response.json())
            .then(result => {
                customerEmail.disabled = true;
                messageText.disabled = true;
                button.disabled = true;
                showInfo("Wiadomosc wyslana, dziekujemy za kontakt");
            })
            .catch(error => {
                showInfo('Wystapil blad');
                console.error(error);
            });
}

function reportAnnouncement(announcementId, reportText, modalWindow) {

    if (reportText.value.length < 5)
        reportText.classList.add('is-invalid');
    else
        fetch(`/otomoto/announcement/reportAnnouncement`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                reportText: reportText.value,
                announcementId: announcementId
            })
        })
            .then(response => response.json())
            .then(result => {
                reportText.value = '';
                $(modalWindow).modal('hide')
                showInfo('Wiadomosc wyslana, dziekujemy za kontakt');
            })
            .catch(error => {
                showInfo('Wystapil blad');
                console.error(error);
            });
}

