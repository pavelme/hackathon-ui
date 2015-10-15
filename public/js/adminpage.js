function getProductJSON() {
    var product = {
        productUrl: $('#productUrl').val(),
        category: $('#category').val(),
        country: $('#country').val(),
        title: $('#title').val(),
        productImage: $('#productImage').val(),
        productPrice: parseFloat($('#productPrice').val()),
        isBannerSellingEnabled: $('#isBannerSellingEnabled').is(":checked"),
        clickPrice: parseFloat($('#clickPrice').val()),
        campaignBudget: parseFloat($('#campaignBudget').val()),
        isCrossSellEnabled: Boolean($('#isCrossSellEnabled').is(":checked"))
    };
    return JSON.stringify(product);
}

function addProduct() {
    $.ajax({
        url: "http://localhost:20000/v1/products/",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        data: getProductJSON(),
        success: function(result) {
            renderProduct(result);
        }
    })
    showProducts();
}

function updateProduct() {
    $.ajax({
        url: "http://localhost:20000/v1/products/" + $('#id').val(),
        type: "POST",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        data: getProductJSON(),
        success: function(result) {
            renderProduct(result);
        }
    })
    showProducts();
}

function showProducts() {
    $.ajax({
        url: "http://localhost:20000/v1/products/",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            $('#productsList').empty();
            for(var i in result) {
                var p = result[i];
                $('#productsList').append('<li id="product_id_' + p.id + '" onclick="showProduct(' + p.id + ')">\
                    <a href="#" style="display: block; padding: 8px 10px; border-bottom: 1px solid #dee5e7">\
                    <img src="' + p.productImage + '" class="thumb-sm img-circle">\
                    <span style="margin-left: 10px; position: relative; top: 2px;">' + p.title + '</span>\
                </a>\
                </li>');

            }
            $('.selected_product').removeClass('selected_product');
            $('#product_id_' + $('#id').val()).addClass("selected_product");
        }
    })
}

function deleteProduct(id) {
    $.ajax({
        url: "http://localhost:20000/v1/products/" + id,
        type: "DELETE",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            $('#product_id_' + $('#id').val()).remove();
        }
    });
}

function showProduct(id) {
    $('.selected_product').removeClass('selected_product');
    $('#product_id_' + id).addClass("selected_product");

    $.ajax({
        url: "http://localhost:20000/v1/products/" + id,
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            renderProduct(result);
        }
    })
}

function renderProduct(product) {
    for(var key in product) {
        if(key == "isBannerSellingEnabled" || key == "isCrossSellEnabled") {
            $('#' + key).prop('checked', product[key]);
        } else {
            $('#' + key).val(product[key]);
        }
    }
}

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
        }
    })
}

$(function() {

    renderCountries();
    renderCategories();
    showProducts();

    $('#addProduct').click(function (e) {
        e.preventDefault();
        var product = {
            id: "",
            productUrl: "",
            category: "",
            country: "",
            title: "",
            productImage: "",
            productPrice: "",
            isBannerSellingEnabled: "",
            clickPrice: "",
            campaignBudget: "",
            isCrossSellEnabled: ""
        };
        $('.selected_product').removeClass('selected_product');
        renderProduct(product)
    });

    $('#adminForm').submit(function(e) {
        e.preventDefault();
        if ($('#id').val() == "") {
            addProduct()
        } else {
            updateProduct()
        }
    });

    $('.save_button').click(function(e) {
        e.preventDefault();
        $('#submitbutton').click();
    });
    $('.delete_button').click(function(e) {
        e.preventDefault();
        deleteProduct($('#id').val());
    });

    $('#productsMenu').addClass("active");
});