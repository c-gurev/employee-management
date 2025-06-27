package com.employee.management.dao;

import com.employee.management.dto.EmployeeDTO;
import com.employee.management.dto.EmployeePage;
import com.employee.management.dto.PaginationInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import oracle.jdbc.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmployeeDAO {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

    private DataSource dataSource;

    public EmployeeDAO() {
    }

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean addEmployee(EmployeeDTO employee) throws SQLException {
        logger.info("Adding employee record: {}", employee);
        String sql = "{ call EMPLOYMENT_MODULE.CREATE_OR_EDIT(?, ?, ?, ?, ?) }";
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setDate(5, Date.valueOf(employee.getDateOfJoining()));
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateEmployee(EmployeeDTO employee) throws SQLException {
        logger.info("Updating employee record: {}", employee);
        String sql = "{ call EMPLOYMENT_MODULE.CREATE_OR_EDIT(?, ?, ?, ?, ?) }";
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setDate(5, Date.valueOf(employee.getDateOfJoining()));
            return stmt.executeUpdate() > 0;
        }
    }

    public int deleteEmployee(List<Integer> employeeIds) throws SQLException {
        logger.info("Deleting employee records with IDs: {}", employeeIds);
        if (employeeIds == null || employeeIds.isEmpty()) {
            return 0;
        }
        Integer[] idArray = employeeIds.toArray(new Integer[0]);
        String sql = "{ call EMPLOYMENT_MODULE.REMOVE(?, ?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
            Array oracleIdArray = oracleConnection.createOracleArray("EMPLOYEE_ID_TABLE", idArray);

            stmt.setArray(1, oracleIdArray);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();

            return stmt.getInt(2);
        }
    }

    public EmployeePage getEmployees(int pageNumber, int pageSize, String sortColumn, String sortDirection) throws SQLException {
        logger.info("Retrieving employee records: page #{}, page size {}, sorting  by {}, {}", pageNumber, pageSize, sortColumn, sortDirection);
        List<EmployeeDTO> employees = new ArrayList<>();
        int totalCount;
        String sql = "{call EMPLOYMENT_MODULE.GET_EMPLOYEES(?, ?, ?, ?, ?, ?)}";
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, pageNumber);
            stmt.setInt(2, pageSize);
            stmt.setString(3, sortColumn);
            stmt.setString(4, sortDirection);
            stmt.registerOutParameter(5, Types.REF_CURSOR);
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.execute();

            totalCount = stmt.getInt(6);

            try (ResultSet rs = (ResultSet) stmt.getObject(5)) {
                while (rs.next()) {
                    EmployeeDTO employee = new EmployeeDTO();
                    employee.setId(rs.getInt("EMPLOYEE_ID"));
                    employee.setName(rs.getString("NAME"));
                    employee.setEmail(rs.getString("EMAIL"));
                    employee.setPhone(rs.getString("PHONE_NUMBER"));
                    employee.setDateOfJoining(rs.getDate("DATE_OF_JOINING").toLocalDate());
                    employees.add(employee);
                }
            }
        }
        PaginationInfo pageInfo = new PaginationInfo(pageNumber, pageSize, sortColumn, sortDirection);
        return new EmployeePage(employees, pageInfo, totalCount);
    }

    public boolean checkEmailExists(String email, Integer employeeId) throws SQLException {
        String sql = "{ call EMPLOYMENT_MODULE.CHECK_EMAIL_EXISTS(?, ?, ?) }";
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, email);
            if (employeeId != null) {
                stmt.setInt(2, employeeId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();

            int exists = stmt.getInt(3);
            return exists > 0;
        }
    }
}
