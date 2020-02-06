function ajaxCall() {
    var id = $("#customerId").val();
    id = isNaN(id) ? 0 : id;
    if (id > 0) {
        $.ajax({
            method: "POST",
            url: "/api/ajaxcall",
            contentType: "application/json",
            data: JSON.stringify(id),
            success: function (response) {
                $('#result').html(response);
                console.log(response);

                alert("The account"+response);


            },
            error: function (e) {
                console.log(e);
                //alert(response.responseText);


            }
        })
    }
}
