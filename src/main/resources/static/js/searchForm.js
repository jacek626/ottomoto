function loadVehicleModel(manufacturer, vehicleType, typeOfHtmlElement) {
    $.ajax({
        type: "GET",
        url: "/otomoto/vehicleModel/loadVehicleModel",
        timeout: 1000,
        data: {
            manufacturer,
            vehicleType,
            typeOfHtmlElement
        },
        success(result) {
            $('#vehicleModel').empty().append(result).selectpicker('refresh');
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}


function loadManufacturer(vehicleType, typeOfHtmlElement) {
    $.ajax({
        type: "GET",
        url: "/otomoto/manufacturer/loadManufacturer",
        timeout: 1000,
        data: {
            vehicleType,
            typeOfHtmlElement
        },
        success(result) {
            $('#manufacturer').empty().append(result).selectpicker('refresh');
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}

function loadVehicleSubtypes(vehicleType, typeOfHtmlElement) {
    $.ajax({
        type: "GET",
        url: "/otomoto/manufacturer/loadVehicleSubtypes",
        timeout: 1000,
        data: {
            vehicleType,
            typeOfHtmlElement
        },
        success(result) {
            $('#vehicleSubtype').empty().append(result).selectpicker('refresh');
        },
        error(jqXHR, textStatus, errorThrown) {
            showError(errorThrown);
        }
    });
}

function validateRange(from, to, currentElementValue) {
    if (from === undefined || to === undefined)
        return;

    let fromAsNumber = parseInt(from.val().replace(/\D/g, ""));
    let toAsNumber = parseInt(to.val().replace(/\D/g, ""));

    if (fromAsNumber > toAsNumber) {
        if (currentElementValue === undefined) {
            from.val("");
            from.selectpicker('refresh')
        } else {
            if (currentElementValue === from.val()) {
                to.val("");
                to.selectpicker('refresh')
            } else {
                from.val("");
                from.selectpicker('refresh')
            }
        }
    }
}

$('#pageSizeSelect').on('changed.bs.select', function (event, clickedIndex, isSelected, previousValue) {
    let current = $(this).val();
    let href = window.location.href;

    if (href.indexOf("size=") === -1) {
        let lastCharacter = href.slice(-1);

        if (lastCharacter === "/") {
            href = href.slice(0, href.length - 1);
        }

        if (href.indexOf("?") === -1) {
            window.location.replace(href + "?size=" + current);
        } else {
            window.location.replace(href + "&size=" + current);
        }
    } else {
        href = href.replace("size=" + previousValue, "size=" + current);

        window.location.replace(href);
    }
});


function clearVehicleModel() {
    $("#vehicleModel").empty().selectpicker('refresh');
}


