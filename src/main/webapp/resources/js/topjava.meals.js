const mealAjaxUrl = "ui/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$('#filterForm').submit(function () {
    filterTable();
    return false;
});

function filterTable() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + 'filter',
        data: $('#filterForm').serialize(),
        success: updateTableByData
    });
}
function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});