<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Management</title>
    <link rel="stylesheet" href="../css/styles.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="../js/employee.js"></script>
</head>

<body>
<div class="container mt-4">
    <h2>Employee Management</h2>

    <div class="mb-3">
        <button class="btn btn-success" id="addBtn">Add</button>
        <button class="btn btn-primary" id="updateBtn">Update</button>
        <button class="btn btn-danger" id="deleteBtn">Delete</button>
    </div>

    <table class="employee-table table table-bordered table-hover">
        <thead>
        <tr>
            <th>Select</th>
            <th class="sortable" data-column="id">ID <span class="sort-icon" data-column="id"></span></th>
            <th class="sortable" data-column="name">Name <span class="sort-icon" data-column="name"></span></th>
            <th class="sortable" data-column="email">Email <span class="sort-icon" data-column="email"></span></th>
            <th class="sortable" data-column="phone">Phone <span class="sort-icon" data-column="phone"></span></th>
            <th class="sortable" data-column="dateOfJoining">Joining Date <span class="sort-icon" data-column="dateOfJoining"></span></th>
        </tr>
        </thead>
        <tbody id="employeeTableBody"></tbody>
    </table>

    <nav>
        <ul class="pagination" id="pagination"></ul>
    </nav>
</div>

<div class="modal fade" id="employeeModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="employeeForm">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">Add / Update Employee</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="employeeId">
                    <div class="mb-3">
                        <label for="name">Name</label>
                        <input type="text" class="form-control" id="name" required>
                        <div class="invalid-feedback" data-field="name"></div>
                    </div>
                    <div class="mb-3">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" required>
                        <div id="emailError" class="text-danger" style="display:none;">Email already exists</div>
                        <div class="invalid-feedback" data-field="email"></div>
                    </div>
                    <div class="mb-3">
                        <label for="phone">Phone</label>
                        <input type="text" class="form-control" id="phone" required>
                        <div class="invalid-feedback" data-field="phone"></div>
                    </div>
                    <div class="mb-3">
                        <label for="doj">Date of Joining</label>
                        <input type="date" class="form-control" id="doj" required>
                        <div class="invalid-feedback" data-field="dateOfJoining"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
