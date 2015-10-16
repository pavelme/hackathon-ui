function renderCategories() {
    $.ajax({
        url: "http://localhost:20000/v1/categories/",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            $('#category').empty();
            for(var i in result) {
                $('#category').append("<option>" + result[i] + "</option>");
            }
            generateUrl();
        }
    })
}

function renderCountries() {
    $.ajax({
        url: "http://localhost:20000/v1/countries/",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            $('#country').empty();
            for(var i in result) {
                $('#country').append("<option>" + result[i] + "</option>");
            }
            generateUrl();
        }
    })
}

function generateUrl() {
    $('#url').val("http://crossell.com:20000/v1/advert/get-cross-sell-products/" + $('#category').val() + "/" + $('#country').val() + "?sellerId=" + $('#userId').val())
}

$(function() {
    renderCategories();
    renderCountries();

    $('#country').change(function(e) {
        generateUrl();
    })
    $('#category').change(function(e) {
        generateUrl();
    })
})