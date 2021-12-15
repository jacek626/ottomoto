function loadUserPhoneNumber(userId, elementIdToReturnResult) {
    fetch(`/ottomoto/user/loadUserPhoneNumber?userId=${userId}`, {
        method: 'GET',
    })
        .then(result => {
            document.querySelector(`#${elementIdToReturnResult}`).innerHTML = result;
        });
}

function toggleAnnouncementIsObserved(userLogin, announcementId, elementIdToReturnResult) {
    fetch(`/ottomoto/observedAnnouncements/toggleAnnouncementIsObserved?userLogin=${userLogin}&announcementId=${announcementId}`, {
        method: 'GET',
    })
        .then(result => {
            document.getElementById(elementIdToReturnResult).checked = result;
        });
}




