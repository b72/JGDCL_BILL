$(document).ready(function () {
    $('#report_table_footer').hide();
    $('#report_table_db_footer').hide();
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
        var reportType = $('#get-report input[name="optradio"]:checked').val();
        if (fromDate && toDate) {
         var table =  (reportType == 1)  ? $('#report_table') : $('#report_table_db') ;
         var tableFooter =  (reportType == 1) ? $('#report_table_footer') : $('#report_table_db_footer');
            $.ajax({
                method: "GET",
                url: "/ajax/getReport?fromDate=" + fromDate +"&toDate="+toDate + "&reportType=" + reportType,
                headers: {"X-CSRF-TOKEN": $('#get-report input[name="_csrf"]').val()},
                beforeSend: function () {
                    $(".loader-report").show();
                    table.hide();
                },
                success: function (response) {
                    $(".loader-report").hide();
                    if(typeof response === "object"){
                        table.show();
                       if(reportType == 1)
                             loadDataTable(table, response, printCounter, fromDate, toDate);
                         else
                             loadDataTableDb(table, response, printCounter, fromDate, toDate);
                        tableFooter.show();
                    }
                    else{
                        loadDataTable(table,[],printCounter, fromDate, toDate, response);
                    }
                },
                error: function (xhr, status, error) {
                    console.log(xhr);
                    $(".loader-report").hide();
                   tableFooter.hide();
                    loadDataTable(table,[],printCounter, fromDate, toDate, (xhr.responseText) ? xhr.responseText : xhr.message);
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
            scrollX:true,
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
    function loadDataTableDb(table, jsonData, printCounter, fromDate,toDate, error) {
        table.dataTable({
            oLanguage: {
                "sInfoEmpty": (typeof error =="string") ? error : "Something went wrong!!"
            },
            order: [[ 11, "desc" ]],
            paging: false,
            destroy: true,
            dom: 'Bfrtip',
            scrollX:true,
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
                    orientation:'landscape',
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
            "columns":  [
                {"title" : "Cus.Name", "data" : "customerName"},
                {"title" : "Cus.ID", "data" : "customerId"},
                {"title" : "JTrxID", "data" : "transactionId"},
                {"title" : "Payable Amt", "data" : "payableAmount"},
                {"title" : "Bill Amt", "data" : "billAmount"},
                {"title" : "S.Charge", "data" : "surgeCharge"},
                {"title" : "St.Charge", "data" : "stampCharge"},
                {"title" : "Branch", "data" : "branchName"},
                {"title" : "Paid Amt.", "data" : "paidAmount"},
                {"title" : "Paid By", "data" : "paidBy"},
                {"title" : "Get By", "data" : "getBy"},
                {"title" : "Paid At", "data" : "paidAt"},
                {"title" : "Status", "data" : "status"}],
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
                    .column( 8 )
                    .data()
                    .reduce( function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0 );

                // Total over this page
                pageTotal = api
                    .column( 8, { page: 'current'} )
                    .data()
                    .reduce( function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0 );

                // Update footer
                $( api.column( 8 ).footer() ).html(
                    total
                );
            }
        })
    }
});
