$(document).ready(function(){
    $("#get-bill").submit(function(e) {
        alert('on submit');
        e.preventDefault();
        var id = $('#get-bill input[name="customer_id"]').val();
        if (id) {
            $.ajax({
                method: "GET",
                url: "/ajax/getCustomerById?customerId="+id,
                headers: {"X-CSRF-TOKEN": $('#get-bill input[name="_csrf"]').val()},
                success: function (response) {
                    $('#result').html(response);
                    console.log(response);

                    alert("The account"+response);
                    $("#test-data").val(response.toString());

                },
                error: function (e) {
                    console.log(e);
                    alert(response.responseText);


                }
            })
        }
    });
});
