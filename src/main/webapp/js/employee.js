let pageNumber = 1;
let pageSize = 5;
let sortColumn = "id";
let sortDirection = "asc";
let modal;

$(document).ready(function () {
    const modalEl = document.getElementById("employeeModal");
    if (modalEl) {
        modal = new bootstrap.Modal(modalEl);
    } else {
        console.error("Modal element not found");
    }

    fetchEmployees();

    $("#addBtn").click(() => {
        $("#modalTitle").text("Add Employee");
        clearValidationErrors();
        $("#employeeForm")[0].reset();
        $("#employeeId").val("");
        modal.show();
    });

    $("#updateBtn").click(() => {
        $('#emailError').hide();
        clearValidationErrors();
        const selected = $(".select-employee:checked");
        if (selected.length !== 1) return alert("Select exactly one employee to update.");
        const row = selected.closest("tr");
        $("#employeeId").val(selected.data("id"));
        $("#name").val(row.find(".name").text());
        $("#email").val(row.find(".email").text());
        $("#phone").val(row.find(".phone").text());
        $("#doj").val(row.find(".doj").text());
        $("#modalTitle").text("Update Employee");
        modal.show();
    });

    $("#deleteBtn").click(() => {
        const ids = $(".select-employee:checked").map((_, el) => $(el).data("id")).get();
        if (ids.length === 0) return alert("Select at least one employee to delete.");
        if (!confirm(`Delete ${ids.length} employee(s)?`)) return;

        $.ajax({
            url: "../delete-employee",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({employeeIds: ids}),
            success: fetchEmployees,
            error: () => alert("Error deleting employees.")
        });
    });

    $("#employeeForm").submit(function (e) {
        e.preventDefault();

        const employee = {
            id: $('#employeeId').val(),
            name: $("#name").val(),
            email: $('#email').val(),
            phone: $("#phone").val(),
            dateOfJoining: $("#doj").val()
        };

        $('#emailError').hide();

        checkEmailExists(employee.email, employee.id, function (exists) {
            if (exists) {
                $('#emailError').show();
                return;
            }

            const isUpdate = !!employee.id;
            const url = isUpdate ? "../update-employee" : "../add-employee";

            $.ajax({
                url,
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(employee),
                success: () => {
                    modal.hide();
                    fetchEmployees();
                },
                error: (xhr) => {
                    if (xhr.status === 400) {
                        const errors = JSON.parse(xhr.responseText);

                        $('.invalid-feedback').removeClass('d-block').text('');
                        $('.form-control').removeClass('is-invalid');

                        Object.entries(errors).forEach(([field, message]) => {
                            $(`.invalid-feedback[data-field='${field}']`)
                                .text(message)
                                .addClass('d-block');
                            $(`[name='${field}']`).addClass('is-invalid');
                        });
                    } else {
                        alert('Server error');
                    }
                }
            });
        });
    });

    $(".sortable").on("click", function () {
        const column = $(this).data("column");
        if (sortColumn === column) {
            sortDirection = (sortDirection === "asc") ? "desc" : "asc";
        } else {
            sortColumn = column;
            sortDirection = "asc";
        }
        updateSortIcons();
        fetchEmployees();
    });

    function updateSortIcons() {
        $(".sort-icon").removeClass("sort-asc sort-desc");
        $(`.sort-icon[data-column='${sortColumn}']`).addClass(sortDirection === "asc" ? "sort-asc" : "sort-desc");
    }

    $(document).on("click", ".page-link", function () {
        pageNumber = $(this).data("page");
        fetchEmployees();
    });
});

function fetchEmployees() {
    $.ajax({
        url: "../list-employees",
        data: {
            pageNumber,
            pageSize,
            sortColumn,
            sortDirection
        },
        success: renderEmployeeTable,
        error: () => alert("Failed to load employees.")
    });
}

function renderEmployeeTable(data) {
    const tbody = $("#employeeTableBody");
    tbody.empty();
    data.employees.forEach(employee => {
        tbody.append(`
            <tr>
                <td><input type="checkbox" class="select-employee" data-id="${employee.id}"></td>
                <td>${employee.id}</td>
                <td class="name">${employee.name}</td>
                <td class="email">${employee.email}</td>
                <td class="phone">${employee.phone}</td>
                <td class="doj">${employee.dateOfJoining}</td>
            </tr>
        `);
    });

    renderPagination(data.totalRecords);
}

function renderPagination(totalRecords) {
    const totalPages = Math.ceil(totalRecords / pageSize);
    const ul = $("#pagination");
    ul.empty();

    for (let i = 1; i <= totalPages; i++) {
        ul.append(`
            <li class="page-item ${i === pageNumber ? "active" : ""}">
                <a class="page-link" data-page="${i}">${i}</a>
            </li>
        `);
    }
}

function checkEmailExists(email, employeeId, callback) {
    $.ajax({
        url: "../check-email",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({email: email, employeeId: employeeId || null}),
        success: function (response) {
            callback(response.exists);
        },
        error: function () {
            alert("Error checking email.");
            callback(false);
        }
    });
}

function clearValidationErrors() {
    $('.invalid-feedback')
        .removeClass('d-block')
        .text('');
    $('.form-control').removeClass('is-invalid');
}
