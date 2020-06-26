function markPictureInAnnouncementAsMain(element) {
    let mainPhotoInAnnouncement = $(element).parent().find("input.mainPhotoInAnnouncement");
    let currentValue = mainPhotoInAnnouncement.val();

    $("input.mainPhotoInAnnouncement").val('false');
    $(".markPictureAsMain ").css("text-decoration", "none");

    if (currentValue == 'true') {
        currentValue = true;
        $(element).css("text-decoration", "none");

    } else {
        currentValue = false;
        $(element).css("text-decoration", "underline");
    }
    $(mainPhotoInAnnouncement).val(!currentValue);
}


function showImage(element, target) {
    if (element === null) {
        return;
    }

    if (!target) {
        target = $("#photoContainer img");
    }

    if ($(target).attr("src") !== element.getAttribute("picture")) {
        $(target).fadeOut(100, function () {
            $(target).attr("src", element.getAttribute("picture"));
        })
            .fadeIn(200);
    }

    $(target).attr("currentImageIndex", element.getAttribute("index"));

    if (target) {
        $(target).attr("src", element.getAttribute("picture"));
    } else {
        $("#photoContainer img").attr("src", element.getAttribute("picture"));
    }
}

function deletePictureInAnnouncement(element) {
    $(element).parent().find("input.pictureToDelete").val("true");
    $(element).parent().attr("pictureToDelete", "true");
    $(element).parent().css("display", "none");

    if ($(element).parent().find("img").attr("picture") === $("#photoContainerMiniImage").attr("src")) {
        if ($("#imagesScroll li[pictureToDelete='false'] img").length > 0) {
            // show first picture from all
            showImage($("#imagesScroll li[pictureToDelete='false'] img")[0], "#photoContainerMiniImage");
            markPictureInAnnouncementAsMain($("#imagesScroll li[pictureToDelete='false'] .markPictureAsMain")[0]);
        } else { // no other pictures to display
            $("#photoContainerMiniImage").attr("src", "");
        }
    }
}


function uploadFile() {
    let formData = new FormData();
    formData.append("file", document.getElementById("upload-file-input").files[0]);

    $.ajax({
        url: "/otomoto/image/uploadImage",
        type: "POST",
        data: formData,
        timeout: 100000,
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        success(result) {

            for (let i = 0; i < result.length; i++) {
                let currentMaxElementId = $("#imagesScroll li:last-child").attr("index");

                if (isNaN(currentMaxElementId)) {
                    currentMaxElementId = 0;
                } else {
                    currentMaxElementId = parseInt(currentMaxElementId, 10) + 1;
                }

                result[i] = result[i].replace(new RegExp("LIST_ID", "g"), currentMaxElementId);
                $("#imagesScroll").append(result[i]);
            }

            showInfo("liczba dodanych zdjęć, " + result.length)


        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}

function findNextImage(direction) {
    let currentImageIndex = $(".photoGalleryMiniArrow").parent().find("img").attr("currentImageIndex");
    currentImageIndex = direction === "next" ? parseInt(currentImageIndex, 10) + 1 : parseInt(currentImageIndex, 10) - 1;

    let elementToReturn = $("#imagesScroll li[index='" + currentImageIndex + "']").find("img")[0];

    if (elementToReturn) {
        $('.photoGalleryMiniArrow').parent().find('img').attr('currentImageIndex', currentImageIndex);
        return elementToReturn;
    }

    currentImageIndex = direction === "next" ? $("#imagesScroll li").first().attr("index") : $("#imagesScroll li").last().attr("index");

    elementToReturn = $("#imagesScroll li[index='" + currentImageIndex + "']").find("img")[0];

    if (elementToReturn) {
        $(".photoGalleryMiniArrow").parent().find("img").attr("currentImageIndex", currentImageIndex);
        return elementToReturn;
    }

    return null;
}
