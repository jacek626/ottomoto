function loadUserPhoneNumber(userId, elementIdToReturnResult) {
    fetch(`/ottomoto/user/loadUserPhoneNumber?userId=${userId}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(result => {
            document.querySelector(`#${elementIdToReturnResult}`).innerHTML = result;
        })
        .catch(error => console.error(error));
}

function toggleAnnouncementIsObserved(userLogin, announcementId, elementIdToReturnResult) {
    fetch(`/ottomoto/observedAnnouncements/toggleAnnouncementIsObserved?userLogin=${userLogin}&announcementId=${announcementId}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(result => {
            document.getElementById(elementIdToReturnResult).checked = result;
        })
        .catch(error => console.error(error));
}




