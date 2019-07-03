$.expr[":"].contains = $.expr.createPseudo(function(arg) {
    return function( elem ) {
        return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
    };
});


$(document).on('focusin', '#pageSizeSelect', function() {
	$(this).data('val', $(this).val());
}).on('change', '#pageSizeSelect', function() {
	var prev = $(this).data('val');
	var current = $(this).val();
	var href = window.location.href;

	if (href.indexOf("size=") == -1) {
		var lastCharacter = href.slice(-1);

		if (lastCharacter == '/') {
			href = href.slice(0, href.length - 1);
		}
		
		if(href.indexOf("?") == -1) 
			window.location.replace(href + "?size=" + current);
		else
			window.location.replace(href + "&size=" + current);
	} else {
		href = href.replace("size=" + prev, "size=" + current);

		window.location.replace(href);
	}
});

function deletePictureInAnnouncement(element) {
		 $(element).parent().find('input.pictureToDelete').val('true'); 
		 $(element).parent().attr('pictureToDelete','true'); 
		 $(element).parent().css('display','none');
		 
		 // if picture to delete is currently displayed
		 if($(element).parent().find('img').attr("picture") == $('#photoContainerMiniImage').attr('src')) 
			 if($("#imagesScroll li[pictureToDelete='false'] img").length > 0) {
				 // show first picture from all
				 showImage($("#imagesScroll li[pictureToDelete='false'] img")[0],'#photoContainerMiniImage');
			 }
			 else { // no other pictures to display
				 $('#photoContainerMiniImage').attr('src','');
			 }
}


function loadVehicleModel(manufacturer,vehicleType, typeOfHtmlElement) {
		$.ajax({
			type : "GET",
			url : "/otomoto/announcement/loadVehicleModel",
			timeout : 1000,
			data: { 
				'manufacturer': manufacturer,
				'vehicleType':  vehicleType,
				'typeOfHtmlElement' : typeOfHtmlElement
			  },
			success : function(result) {
				console.log("SUCCESS: " + result.data);
				console.log("SUCCESS: " + result);
				$('#vehicleModel').empty().append(result);
				
//				if(resultInDivs) {
				if(typeOfHtmlElement == 'li') {
					setDropDownListListener('#vehicleModel li');
					$('#vehicleModelValue').val('');
					$('#vehicleModelLabel').val('');
				}
			},
			error : function(result) {
				console.log("ERROR: ", result);
			},
			done : function(result) {
				console.log("DONE");
			}
		});
	}

	
/*function loadManufacturer(vehicleType, resultWithEmptyOption) {
*/	function loadManufacturer(vehicleType, typeOfHtmlElement) {
		$.ajax({
			type : "GET",
			url : "/otomoto/announcement/loadManufacturer",
			timeout : 1000,
			data: { 
				'vehicleType': vehicleType,
				'typeOfHtmlElement' : typeOfHtmlElement
			  },
			success : function(result) {
				console.log("SUCCESS: " + result.data);
				console.log("SUCCESS: " + result);
				$('#manufacturer').empty().append(result);
				
				if($('#vehicleModel'))
					$('#manufacturer').change();
			},
			error : function(result) {
				console.log("ERROR: ", result);
			},
			done : function(result) {
				console.log("DONE");
			}
		});
	}
	
	function loadVehicleSubtypes(vehicleType, typeOfHtmlElement) {
		$.ajax({
			type : "GET",
			url : "/otomoto/manufacturer/loadVehicleSubtypes",
			timeout : 5000,
			data: { 
				'vehicleType': vehicleType,
				'typeOfHtmlElement' : typeOfHtmlElement
			  },
			success : function(result) {
				console.log("SUCCESS: " + result.data);
				console.log("SUCCESS: " + result);
				$('#vehicleSubtype').empty().append(result);

			},
			error : function(result) {
				console.log("ERROR: ",  result);
			},
			done : function(result) {
				console.log("DONE");
			}
		});
	}
	
	
	function uploadFile() {
		var fileName = '';
		
			
			//upload-file-label
/*		if( this.files && this.files.length > 1 )
			fileName = ( this.getAttribute( 'data-multiple-caption' ) || '' ).replace( '{count}', this.files.length );
		else
			fileName = e.target.value.split( '\' ).pop();
*/
		
		  $.ajax({
		    url:   "/otomoto/announcement/uploadImage",
		    type: "POST",
		    data: new FormData($("#upload-file-form")[0]),
		    enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
		    success: function (result) {
		    	
		    	for(i=0;i < result.length; i++) {
		    		var currentMaxElementId = $('#imagesScroll').children('li').last().attr('index');
				    currentMaxElementId =  parseInt(currentMaxElementId) + 1;
				    result[i] =  result[i].replace(new RegExp('LIST_ID', 'g'), currentMaxElementId);
				    
				    $('#imagesScroll').append(result[i]);
		    	}
		    	
		    	$('#jsPopupText').text('liczba dodanych zdjęć, ' + result.length);
		    	$('#jsPopup').show()	
		    	
		    	 setTimeout(function() {
		    		 	$('#jsPopup').hide()	
		    	    }, 5000);
		    	
		/*    document.getElementById("upload-file-input").value = "";*/
		    	
		    	
		    },
		    error: function () {
		      // Handle upload error
		      // ...
		    }
		  });
		}
	
	function validateDateRange(currentElement) {
		if($('#productionYearFrom').val() > $('#productionYearTo').val()) {
			if($(currentElement).attr('id') == 'productionYearFrom')
				 $('#productionYearTo').prop('selectedIndex',0);
			else
				$('#productionYearFrom').prop('selectedIndex',0);
		}
	}
	
	function validatePriceRange(currentElement) {
		if($('#priceFrom').val() > $('#priceTo').val()) {
			if($(currentElement).attr('id') == 'priceFrom')
				$('#priceTo').val('');
			else
				$('#priceFrom').val('');
		}
	}
	
	function validateRange(from, to, currentElement) {
		if($(from).val() != '' && $(to).val() != '')
			var fromAsNumber = parseInt($(from).val().replace(/ /g,""));
			var toAsNumber = parseInt($(to).val().replace(/ /g,""));
			
			if(fromAsNumber > toAsNumber) {
				if($(currentElement).attr('id') == $(from).attr('id')) {
					$(to).val('');
				}
				else {
					$(from).val('');
				}
		}
	}
	
	
	function controlElementVisibility(flagShowOrHide, elementToHideOrShow, displayType) {
		if(flagShowOrHide)
			if(displayType)
				$(elementToHideOrShow).css('display',displayType);
			else 
				$(elementToHideOrShow).css('display','block');
		else 
			$(elementToHideOrShow).css('display','none');
	}
	
	function showImage(element, target) {
		if(element == null)
			return;
			
		if(!target)
			target = $('#photoContainer img');
		
		if($(target).attr('src') != element.getAttribute('picture'))
			 $(target).fadeOut(100, function() {
		        $(target).attr('src',element.getAttribute('picture'));
		        })
		        .fadeIn(200);
		
		$(target).attr('currentImageIndex',element.getAttribute('index'));
		
	/*	if(target)
			$(target).attr('src',element.getAttribute('picture'));
		else
			$('#photoContainer img').attr('src',element.getAttribute('picture'));*/
	}
	
	function findNextImage(direction) {
		var currentImageIndex = $('.photoGalleryMiniArrow').parent().find('img').attr('currentImageIndex');
		
		if(direction == 'next')
			currentImageIndex =  parseInt(currentImageIndex) + 1;
		else
			currentImageIndex =  parseInt(currentImageIndex) - 1;
		
		var elementToReturn = $("#imagesScroll li[index='" +currentImageIndex +"']").find('img')[0];
		
		if(elementToReturn) {
			$('.photoGalleryMiniArrow').parent().find('img').attr('currentImageIndex',currentImageIndex);
			return elementToReturn;
		}
		else 
			return null;
	}

	function setDropDownListListener(element) {
		$(element).on( "click", function() {

			var inputWithValue = $(this).parent().parent().find('input:hidden');
			var inputWithLabel = $(this).parent().parent().find('input:text');
			
			// jesli kliknieto to sama wartosc to nie wczytujemy ponownie danych
			if(inputWithValue.val() != $(this).attr('value')) {
				inputWithLabel.val($(this).text());
				inputWithValue.val($(this).attr('value'));
				inputWithValue.trigger('change');
				
				$(this).parent().find('li').removeClass('selected');
				$(this).addClass('selected');
			}
		});
	}
	
	function setDropDownRangeListener(element) {
		$(element).on( "click", function() {
			
			if($(this).val() != $(this).parent().find('li:selected').val()) {
				$(this).parent().find('li').removeClass('selected');
				$(this).addClass('selected');
			}
		});
	}

function showOrHidePositionsInDropDown(element) {
	
	$(element).parent().find('li:not(:contains("' + element.value + '"))').hide()
	$(element).parent().find('li:contains("' + element.value + '")').show()
//	$("#manufacturer li:contains('" + element.value + "')").show()
/*	$("#manufacturer li:not(:contains('" + element.value + "'))").hide()
	$("#manufacturer li:contains('" + element.value + "')").show()
*/}


function clearInputAndFireTrigger(element) {
	$(element).prev('.hiddenInputToFilterResults').val('');  
	$(element).prev('.hiddenInputToFilterResults').trigger('change');
}

function rewriteTextToHiddenInputOnKeyDown(element) {
	element = $(element).parent().find('.hiddenInputToFilterResults');
	
	if(event.key.length == 1) 
		$(element).val($(element).val() + event.key); 
	else if(event.key == 'Backspace') 
		$(element).val($(element).val().slice(0, -1)); 
	
	$(element).trigger('change');
}

function selectValuesInDropDownByKeyboard() {
	if(event.key == 'ArrowDown') {
		console.log('1');
	} 
	else if(event.key == 'ArrowUp') {
		console.log('2');
	} 
}

$('#manufacturerLabel').bind('keydown', function(event) {
    switch(event.keyCode){
    }
 });

var nodes;
var ul;
var selected = 0;
document.addEventListener('keydown', function(e) {
	var focusedElement = $('.labelInAnnouncementSearch:focus');
	
	if($(document.activeElement).hasClass('labelInAnnouncementSearch')) {
		var dropDownId = '#' +  $(document.activeElement).attr('dropDownElementId');
		
		 nodes = $(dropDownId + ' li:visible');
		 ul = $(dropDownId);
		 selected = $(dropDownId + ' li.selected:visible').index();
		 
		 if (e.keyCode === 38) { // up
		        select(nodes[selected - 1]);
		    }
		    if (e.keyCode === 40) { // down
		        select(nodes[selected + 1]);
		    }
		    
		 e.preventDefault();
	}
/*	if($('#manufacturerLabel').is(":focus")) {
		nodes = $('#manufacturer li:visible');
		ul = $('#manufacturer');
		selected = $('#manufacturer li.selected:visible').index();
		if (e.keyCode === 38) { // up
			select(nodes[selected - 1]);
		}
		if (e.keyCode === 40) { // down
			select(nodes[selected + 1]);
		}
		
		e.preventDefault();
	}
	if($('#vehicleModelLabel').is(":focus")) {
	 nodes = $('#vehicleModel li:visible');
		 ul = $('#vehicleModel');
		 selected = $('#vehicleModel li.selected:visible').index();
		 
		if (e.keyCode === 38) { // up
			select(nodes[selected - 1]);
		}
		if (e.keyCode === 40) { // down
			select(nodes[selected + 1]);
		}
		
		e.preventDefault();
	}*/
});



function select(el) {
    var s = [].indexOf.call(nodes, el);
    if (s === -1) return;
    
    selected = s;
    
    var elHeight = $(el).height();
    var scrollTop = $(ul).scrollTop();
    var viewport = scrollTop + $(ul).height();
    var elOffset = elHeight * selected;
    
    console.log('select', selected, ' viewport', viewport, ' elOffset', elOffset);
    if (elOffset < scrollTop || (elOffset + elHeight) > viewport)
        $(ul).scrollTop(elOffset);
    
   /* if(document.querySelector('li.selected') != null)
    	document.querySelector('li.selected').classList.remove('selected');*/
    
    if($(ul).find('li.selected'))
    	$(ul).find('li.selected').removeClass('selected');
    	
    
    el.classList.add('selected');
}

	
	
	
	