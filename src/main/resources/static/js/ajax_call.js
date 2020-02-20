$(document).ready(function () {
    $("#pay-information").hide();
    $(".pay-information-loader").hide();
    $("#get-bill").submit(function (e) {
        e.preventDefault();
        var id = $('#get-bill input[name="customer_id"]').val();
        if (id) {
            $.ajax({
                method: "GET",
                url: "/ajax/getCustomerById?customerId=" + id,
                headers: {"X-CSRF-TOKEN": $('#get-bill input[name="_csrf"]').val()},
                beforeSend: function () {
                    $(".loader").show();
                    $(".ajax-result").html("");
                    $("#pay-information").hide();
                    $(".pay-information-loader").hide();
                    $(".ajax-pay-result").hide();
                },
                success: function (response) {
                    $(".loader").hide();
                    console.log(response);
                    console.log(typeof response);

                    if (typeof response === "object") {
                        var html = "" ;
                        $.each(response, function f(key, value) {
                            if (
                                ["customerId",
                                    "customerName",
                                    "monyear",
                                    "billAmount",
                                    "surcharge",
                                    "paybleAmount",
                                    "billcount",
                                    "transactionId"
                                ].includes(key)) {
                                var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                                var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                                html += '<tr id="'+key+'"><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                            }
                        });
                        if (html !== "") {
                            var form = '';
                            html += '<tr><th> Paid Amount </th><td> ' +
                                '<input type="text" id="paid-amount" name="paidAmount" class="form-control" placeholder="Amount Paid by Customer" required>' +
                                ' </td></tr>';
                            html += '<tr><th>  Mobile No </th><td> ' +
                                '<input type="text" id="mobile-No" name="mobileNo" class="form-control" placeholder="Customer\'s mobile number" required>' +
                                ' </td></tr>';
                            html += '<tr><th></th><td><button type="submit" class="btn btn-primary">Pay</button></td></tr>';
                        }
                        $(".ajax-result").html(html);
                    }
                },
                error: function (xhr, status, error) {
                    $(".loader").hide();
                    var html = "";
                    console.log(xhr);
                    console.log(status);
                    console.log(error);
                    $.each(xhr.responseJSON, function f(key, value) {
                        var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                        var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                        if (["message", "status", "error"].includes(key)) {
                            html += '<tr><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                        }
                    });
                    $(".ajax-result").html(html);
                }
            })
        }
    });

    $("#pay-bill").submit(function (e) {
        e.preventDefault();
        var header = {};
        var data = {};
        header[$('meta[name=_csrf_header]').attr("content")] = $('meta[name=_csrf]').attr("content");
        data["paidAmount"] = $('#pay-bill input[name="paidAmount"]').val();
        data["mobileNo"] = $('#pay-bill input[name="mobileNo"]').val();
        data["customerId"] = $('#get-bill input[name="customer_id"]').val();
        data["transactionId"] = $("#transactionId").find("td:eq(0)").text().trim();
        $.ajax({
            method: "POST",
            url: "/ajax/payment",
            headers: header,
            data:JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function () {
                $(".pay-information-loader").show();
                $("#pay-information").show();
                $(".ajax-pay-result").hide();
            },
            success: function (response) {
                $(".pay-information-loader").hide();
                if (typeof response === "object") {
                    var html = "";
                    $.each(response, function f(key, value) {
                        var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                        var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                        html += '<tr id="' + key + '"><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                    });
                    $(".ajax-pay-result").html(html);
                    $(".ajax-pay-result").show();
                }
            },
            error: function (xhr, status, error) {
                $(".pay-information-loader").hide();
                var html = "";
                console.log(xhr);
                console.log(xhr.responseJSON);
                $(".ajax-result").show();
                $.each(xhr.responseJSON, function f(key, value) {
                    var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                    var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                        html += '<tr><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                });
                $(".ajax-pay-result").html(html);
                $(".ajax-pay-result").show();
            }
        })
    });
});
