$(document).ready(function () {
    $('#report_table_footer').hide();
    $("#from_date").datepicker({
        format: "dd-mm-yyyy",
        todayBtn: "linked",
        clearBtn: true,
        autoclose: true,
    });
    $("#to_date").datepicker({
        format: "dd-mm-yyyy",
        todayBtn: "linked",
        clearBtn: true,
        autoclose: true
    });
    $("#get-report").submit(function (e) {
        var printCounter = 0;
        e.preventDefault();
        var fromDate = $('#get-report input[name="from_date"]').val();
        var toDate = $('#get-report input[name="to_date"]').val();
        if (fromDate && toDate) {
         var table =  $('#report_table');
            $.ajax({
                method: "GET",
                url: "/ajax/getReport?fromDate=" + fromDate +"&toDate="+toDate,
                headers: {"X-CSRF-TOKEN": $('#get-report input[name="_csrf"]').val()},
                beforeSend: function () {
                    $(".loader-report").show();
                    table.hide();
                },
                success: function (response) {
                    $(".loader-report").hide();
                    if(typeof response === "object"){
                        table.show();
                        loadDataTable(table, response, printCounter, fromDate, toDate);
                        $('#report_table_footer').show();
                    }
                    else{
                        loadDataTable(table,[],printCounter, fromDate, toDate, response);
                    }
                },
                error: function (xhr, status, error) {
                    $(".loader-report").hide();
                    $('#report_table_footer').hide();
                    loadDataTable(table,[],printCounter, fromDate, toDate, xhr.responseText);
                }
            })
        }
    });

    function loadDataTable(table, jsonData, printCounter, fromDate,toDate, error) {
        table.dataTable({
            oLanguage: {
                "sInfoEmpty": (typeof error =="string") ? error : "Something went wrong!!"
            },
            order: [[ 4, "desc" ]],
            paging: false,
            destroy: true,
            dom: 'Bfrtip',
            buttons: [
                'copy',
                {
                    extend: 'excel',
                    title:"Jalalabad Gas Bill Collection",
                    messageTop: 'Bills Collected From ' + fromDate + " to " + toDate,
                    messageBottom: "National Bank Limited",
                    footer: true
                },
                {
                    extend: 'pdf',
                    title:"Jalalabad Gas Bill Collection",
                    messageTop: 'Bills Collected From ' + fromDate + " to " + toDate,
                    messageBottom: "National Bank Limited",
                    footer: true
                },
                {
                    title:"Jalalabad Gas Bill Collection",
                    extend: 'print',
                    messageTop: function () {
                        printCounter++;

                        if ( printCounter === 1 ) {
                            return 'This is the first time you have printed this document.';
                        }
                        else {
                            return 'You have printed this document '+printCounter+' times';
                        }
                    },
                    messageBottom: "National Bank Limited",
                    footer: true
                }
            ],
            "data": jsonData,
            "columns": [
                {"title": "Customer ID", "data": "customerId"},
                {"title": "Mobile No", "data": "mobileNo"},
                {"title": "Bank", "data": "bankName"},
                {"title": "Txn ID", "data": "transactionId"},
                {"title": "Txn Date", "data": "txndate"},
                {"title": "Paid Amount", "data": "paidAmount"}
            ],
            "footerCallback": function ( row, data, start, end, display ) {
                var api = this.api(), data;

                // Remove the formatting to get integer data for summation
                var intVal = function ( i ) {
                    return typeof i === 'string' ?
                        i.replace(/[\$,]/g, '')*1 :
                        typeof i === 'number' ?
                            i : 0;
                };

                // Total over all pages
                total = api
                    .column( 5 )
                    .data()
                    .reduce( function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0 );

                // Total over this page
                pageTotal = api
                    .column( 5, { page: 'current'} )
                    .data()
                    .reduce( function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0 );

                // Update footer
                $( api.column( 5 ).footer() ).html(
                    total
                );
            }
        })
    }
});
