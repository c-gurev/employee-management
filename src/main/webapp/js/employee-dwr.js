let pageNumber = 1;
let pageSize = 5;
let sortColumn = "id";
let sortDirection = "asc";
let modal;

$(document).ready(function () {
    const modalEl = document.getElementById("employeeModal");
    modal = modalEl ? new bootstrap.Modal(modalEl) : null;
    if (!modal) return console.error("Modal element not found");

    fetchEmployees();

    $("#addBtn").click(() => {
        $("#modalTitle").text("Add Employee");
        clearValidationErrors();
        $("#employeeForm")[0].reset();
        $("#employeeId").val("");
        $('#emailError').hide();
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

        EmployeeServiceDWR.deleteEmployee({employeeIds: ids}, function (success) {
            if (success) {
                fetchEmployees();
            } else {
                alert("Error deleting employees.");
            }
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

        EmployeeServiceDWR.checkEmail(employee.email, employee.id || null, function (result) {
            if (result.exists) {
                $('#emailError').show();
                return;
            }

            const isUpdate = !!employee.id;
            const method = isUpdate ? EmployeeServiceDWR.updateEmployee : EmployeeServiceDWR.addEmployee;

            method(employee, function (response) {
                if (response.status === 'success') {
                    modal.hide();
                    fetchEmployees();
                } else if (response.status === 'validation_error') {
                    $('.invalid-feedback').removeClass('d-block').text('');
                    $('.form-control').removeClass('is-invalid');

                    Object.entries(response.validationErrors).forEach(([field, message]) => {
                        $(`.invalid-feedback[data-field='${field}']`)
                            .text(message)
                            .addClass('d-block');
                        $(`[name='${field}']`).addClass('is-invalid');
                    });
                } else {
                    alert(response.message);
                }
            });
        });
    });

    $(".sortable").on("click", function () {
        const column = $(this).data("column");
        sortDirection = (sortColumn === column && sortDirection === "asc") ? "desc" : "asc";
        sortColumn = column;
        updateSortIcons();
        fetchEmployees();
    });

    $(document).on("click", ".page-link", function () {
        pageNumber = $(this).data("page");
        fetchEmployees();
    });

    function updateSortIcons() {
        $(".sort-icon").removeClass("sort-asc sort-desc");
        $(`.sort-icon[data-column='${sortColumn}']`).addClass(sortDirection === "asc" ? "sort-asc" : "sort-desc");
    }
});

function fetchEmployees() {
    EmployeeServiceDWR.listEmployees(pageNumber, pageSize, sortColumn, sortDirection, function (data) {
        if (!data || !data.employees) return alert("Failed to load data.");
        renderEmployeeTable(data.employees);
        renderPagination(data.totalRecords);
    });
}

function renderEmployeeTable(employees) {
    const tbody = $("#employeeTableBody");
    tbody.empty();
    employees.forEach(employee => {
        tbody.append(`
            <tr>
                <td><input type="checkbox" class="select-employee" data-id="${employee.id}"></td>
                <td>${employee.id}</td>
                <td class="name">${employee.name}</td>
                <td class="email">${employee.email}</td>
                <td class="phone">${employee.phone}</td>
                <td class="doj">${employee.dateOfJoining || ""}</td>
            </tr>
        `);
    });
}

function renderPagination(totalRecords) {
    const totalPages = Math.ceil(totalRecords / pageSize);
    const ul = $("#pagination");
    ul.empty();

    for (let i = 1; i <= totalPages; i++) {
        ul.append(`
            <li class="page-item ${i === pageNumber ? "active" : ""}">
                <a class="page-link" data-page="${i}" href="#">${i}</a>
            </li>
        `);
    }
}

function clearValidationErrors() {
    $('.invalid-feedback')
        .removeClass('d-block')
        .text('');
    $('.form-control').removeClass('is-invalid');
}
