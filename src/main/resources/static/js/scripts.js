$.expr[":"].contains = $.expr.createPseudo(function (arg) {
    return function (elem) {
        return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
    };
});

function showError(error) {
    showInfo("Przepraszamy wystapil blad " + error);
}

function showInfo(message) {
    $("#infoText div").text(message);
    $("#infoText").show();

    setTimeout(function () {
        $("#infoText").hide();
    }, 5000);
}

function controlElementVisibility(flagShowOrHide, elementToHideOrShow, displayType) {
    if (flagShowOrHide) {
        if (displayType) {
            $(elementToHideOrShow).css("display", displayType);
        } else {
            $(elementToHideOrShow).css("display", "block");
        }
    } else {
        $(elementToHideOrShow).css("display", "none");
    }
}

Array.prototype.remove = function () {
    let what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};

function removeElementFromListInInput(inputWithList, valueToRemove) {
    let arr = inputWithList.val().split(",");
    arr.remove(valueToRemove);
    inputWithList.val(arr);
}

function addElementFromListInInput(inputWithList, valueToAdd) {
    let arr = inputWithList.val().split(",");
    arr.push(valueToAdd);

    if (arr[0] === "") {
        arr.shift();
    }

    inputWithList.val(arr);
}

function setDropDownListMultiOptionListener(element) {
    $(element).on("click", function () {
        let inputWithValue = $(this).parent().parent().find(".currentValues");
        let inputWithLabel = $(this).parent().parent().find(".currentLabels");

        if ($(this).hasClass("selected")) {
            $(this).removeClass("selected");
            removeElementFromListInInput(inputWithLabel, $(this).text());
            removeElementFromListInInput(inputWithValue, $(this).attr("value"));
        } else {
            $(this).addClass("selected");
            addElementFromListInInput(inputWithLabel, $(this).text());
            addElementFromListInInput(inputWithValue, $(this).attr("value"));
        }
    });
}

function setDropDownListListener(element) {
    $(element).on("click", function () {

        let inputWithValue = $(this).parent().parent().find("input:hidden");
        let inputWithLabel = $(this).parent().parent().find("input:text");

        // jesli kliknieto to sama wartosc to nie wczytujemy ponownie danych
        if (inputWithValue.val() !== $(this).attr("value")) {
            inputWithLabel.val($(this).text());
            inputWithValue.val($(this).attr("value"));
            inputWithValue.trigger("change");

            $(this).parent().find("li").removeClass("selected");
            $(this).addClass("selected");
        }
    });
}

function setDropDownRangeListener(element) {
    $(element).on("click", function () {

        if ($(this).val() !== $(this).parent().find("li:selected").val()) {
            $(this).parent().find("li").removeClass("selected");
            $(this).addClass("selected");
        }
    });
}

// chyba usunac
function showOrHidePositionsInDropDown(element) {
    $(element).parent().find('li:not(:contains("' + element.value + '"))').hide();
    $(element).parent().find('li:contains("' + element.value + '")').show()
}


function select(el) {
    let s = [].indexOf.call(nodes, el);
    if (s === -1) {
        return;
    }

    selected = s;

    let elHeight = $(el).height();
    let scrollTop = $(ul).scrollTop();
    let viewport = scrollTop + $(ul).height();
    let elOffset = elHeight * selected;

    if (elOffset < scrollTop || (elOffset + elHeight) > viewport) {
        $(ul).scrollTop(elOffset);
    }

    if ($(ul).find("li.selected"))
        $(ul).find("li.selected").removeClass("selected");


    el.classList.add("selected");
}

let nodes;
let ul;
let selected = 0;
$(document).keyup(function (e) {
    if ($(document.activeElement).hasClass("labelInAnnouncementSearch")) {
        let dropDownId = "#" + $(document.activeElement).attr("dropDownElementId");

        nodes = $(dropDownId + " li:visible");
        ul = $(dropDownId);
        selected = $(dropDownId + " li.selected:visible").index();

        if (e.keyCode === 38) { // up
            select(nodes[selected - 1]);
        }
        if (e.keyCode === 40) { // down
            select(nodes[selected + 1]);
        }

        e.preventDefault();
    }
});

function clearInputAndFireTrigger(element) {
    $(element).prev(".hiddenInputToFilterResults").val("");
    $(element).prev(".hiddenInputToFilterResults").trigger("change");
}

function loadUserPhoneNumber(userId, elementIdToReturnResult) {
    fetch(`/otomoto/user/loadUserPhoneNumber?userId=${userId}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(result => {
            document.querySelector(`#${elementIdToReturnResult}`).innerHTML = result;
        })
        .catch(error => console.error(error));
}

function toggleAnnouncementIsObserved(userLogin, announcementId, elementIdToReturnResult) {
    fetch(`/otomoto/observedAnnouncements/toggleAnnouncementIsObserved?userLogin=${userLogin}&announcementId=${announcementId}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(result => {
            document.getElementById(elementIdToReturnResult).checked = result;
        })
        .catch(error => console.error(error));
}




