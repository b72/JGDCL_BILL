$(document).ready(function () {
    $("#get-bill").submit(function (e) {
        alert('on submit');
        var table = document.getElementById("displayTable")
        e.preventDefault();
        var id = $('#get-bill input[name="customer_id"]').val();
        if (id) {
            $.ajax({
                method: "GET",
                url: "/ajax/getCustomerById?customerId=" + id,
                success: function (response) {
                    $('#result').html(response);
                    console.log(response);

                    alert("The account" + response.toString());
                    var game_name = [];
                    for (var key in response) {
                        if (response.hasOwnProperty(key)) {
                            var item = response[key];
                            game_name.push({
                                ItemName: item.data.name //Changing the .name to .dlc or .type will then display that result

                            });
                        }
                    }
                    for (var i = 0; i < game_name.length; i++) {
                        table.append("<tr><td>" + game_name[i].ItemName + "</td></tr>");
                    }
                },
                error: function (e) {
                    console.log(e);
                    alert(response.responseText);

                }
            })
        }
    });
});
