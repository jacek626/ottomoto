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
        timeout: 5000,
        data: {
            vehicleType,
            typeOfHtmlElement
        },
        success(result) {
            $("#vehicleSubtype").empty().append(result);
            setDropDownListListener(".valuesDropDown  li");

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

function validateRangeOld(from, to, currentElement) {
    if ($(from).val() !== "" && $(to).val() !== "") {
        let fromAsNumber = parseInt($(from).val().replace(/\D/g, ""), 10);
        let toAsNumber = parseInt($(to).val().replace(/\D/g, ""), 10);

        if (fromAsNumber > toAsNumber) {
            if ($(currentElement).attr("id") === $(from).attr("id")) {
                $(to).val("");
            } else {
                $(from).val("");
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

    //   previousValue = current;
    //  $(this).selectpicker('refresh');
});


/*$(document).on("focusin", "#pageSizeSelect", function () {
    $(this).data("val", $(this).val());
}).on("change", "#pageSizeSelect", function () {
    let prev = $(this).data("val");
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
        href = href.replace("size=" + prev, "size=" + current);

        window.location.replace(href);
    }
});*/


function clearManufacturer() {
    $("#vehicleModelLabel").val("");
    $("#vehicleModelValue").val("");
}

function clearManufacturerDropDown() {
    $("#manufacturer").empty();
}

function clearVehicleModelDropDown() {
    $("#vehicleModel").empty();
}

function clearVehicleModel() {
    $("#vehicleModel").empty().selectpicker('refresh');
}

function clearVehicleSubtype() {
    $("#vehicleSubtypeLabel").val("");
    $("#vehicleSubtypeValue").val("");
}

