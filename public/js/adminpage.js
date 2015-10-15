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
            showProducts();
        }
    })
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
            showProducts();
        }
    })
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
    $('#adminForm').hide();
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
            renderAreaViewGraph(id);
            renderAreaClickGraph(id);
            renderViewClickGraph(id);
        }
    });
}

function renderAreaViewGraph(id) {
    $.ajax({
        url: "http://localhost:20000/v1/statistic/" + id + "/areas/views",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            var data = [

            ];

            for(var key in result) {
                data[data.length] = {data: result[key][1], label: result[key][0]}
            }

            var options = {
                grid: {
                    hoverable: true
                },
                colors: ['#23b7e5', '#7266ba'],
                tooltip: true,
                tooltipOpts: { content: "%y.0",  defaultTheme: false, shifts: { x: 10, y: -25 } },
                series: {
                    pie: {
                        show: true
                    }
                }
            };

            if(data.length == 0) {
                $('#testPlot1').parents('.col-sm-6').hide();
            } else {
                $('#testPlot1').parents('.col-sm-6').show();
                $('#testPlot1').plot(data, options);
            }
        }
    });
}

function renderAreaClickGraph(id) {
    $.ajax({
        url: "http://localhost:20000/v1/statistic/" + id + "/areas/clicks",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request)
        {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function(result) {
            var data = [

            ];

            for(var key in result) {
                data[data.length] = {data: result[key][1], label: result[key][0]}
            }

            var options = {
                grid: {
                    hoverable: true
                },
                colors: ['#23b7e5', '#7266ba'],
                tooltip: true,
                tooltipOpts: { content: "%y.0",  defaultTheme: false, shifts: { x: 10, y: -25 } },
                series: {
                    pie: {
                        show: true
                    }
                }
            };

            if(data.length == 0) {
                $('#testPlot2').parents('.col-sm-6').hide();
            } else {
                $('#testPlot2').parents('.col-sm-6').show();
                $('#testPlot2').plot(data, options);
            }
        }
    });
}

function renderViewClickGraph(id) {

    var clicks = null;
    $.ajax({
        url: "http://localhost:20000/v1/statistic/" + id + "/daily/clicks",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request) {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function (result) {
            clicks = result;
        }
    });
    $.ajax({
        url: "http://localhost:20000/v1/statistic/" + id + "/daily/views",
        type: "GET",
        contentType: "application/json",
        beforeSend: function (request) {
            request.setRequestHeader("Token", $('#token').val());
        },
        success: function (views) {

            var viewsData = { data: [ ], label:'Views', lines: { show: true, lineWidth: 1, fill: true}, points: { show: true, radius: 1}, splines: { show: true, tension: 0.4, lineWidth: 1, fill: 0.8 } };
            for(var i in views) {
                viewsData.data.push([parseInt(views[i][0]), views[i][1]])
            }

            var clicksData = { data: [ ], label:'Clicks', lines: { show: true, lineWidth: 1, fill: true}, points: { show: true, radius: 1}, splines: { show: true, tension: 0.4, lineWidth: 1, fill: 0.8 } };
            for(var i in clicks) {
                console.log(clicks[i][0]);
                clicksData.data.push([parseInt(clicks[i][0]), clicks[i][1]])
            }

            var data = [
                viewsData, clicksData
            ];

            var options = {
                colors: ['#23b7e5', '#7266ba'],
                series: { shadowSize: 3 },
                xaxis:{
                    font: { color: '#a1a7ac' },
                    mode: "time",
                    timeformat: "%d/%m/%Y",
                    minTickSize: [1, "day"]
                },
                yaxis:{ font: { color: '#a1a7ac' } },
                grid: { hoverable: true, clickable: true, borderWidth: 0},
                tooltip: true,
                tooltipOpts: { content: '%y.0 %s',  defaultTheme: false, shifts: { x: 10, y: -25 } }
            };

            if(viewsData.data.length == 0 && clicksData.data.length == 0) {
                $('#testPlot').parents('.col-sm-12').hide();
            } else {
                $('#testPlot').parents('.col-sm-12').show();
                $('#testPlot').plot(data, options);
            }
        }
    });
}

function renderProduct(product) {
    $('#adminForm').show();
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