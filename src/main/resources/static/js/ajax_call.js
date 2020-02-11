$(document).ready(function () {
    $("#get-bill").submit(function (e) {
        e.preventDefault();
        var id = $('#get-bill input[name="customer_id"]').val();
        if (id) {
            $.ajax({
                method: "GET",
                url: "/ajax/getCustomerById?customerId=" + id,
                headers: {"X-CSRF-TOKEN": $('#get-bill input[name="_csrf"]').val()},
                beforeSend: function(){
                    $(".loader").show();
                    $(".ajax-result").html("");
                },
                success: function (response) {
                    $(".loader").hide();
                    console.log(response);
                    console.log(typeof response);

                    if (typeof response === "object") {
                        var html = ""
                        $.each(response, function f(key, value) {
                            var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                            var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                            html += '<tr><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                        });
                        console.log(html);
                        $(".ajax-result").html(html);
                    }
                },
                error: function (e) {
                    $(".loader").hide();
                    var html = "";
                    $.each(e, function f(key, value) {
                        var key1 = key.charAt(0).toUpperCase() + key.slice(1);
                        var key2 = key1.replace(/([A-Z])/g, ' $1').trim();
                        if(key.toString() === 'message'){
                            html += '<tr><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                            alert(e);
                        }
                        // html += '<tr><th> ' + key2 + ' </th><td> ' + value + ' </td></tr>';
                    });
                    console.log(e);
                    $(".ajax-result").html(html);



                }
            })
        }
    });
});
