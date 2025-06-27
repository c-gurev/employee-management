CREATE TABLE employees
(
    employee_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR2(100),
    email           VARCHAR2(100) UNIQUE,
    phone_number    VARCHAR2(20),
    date_of_joining DATE
);

CREATE OR REPLACE TYPE employee_id_table AS TABLE OF NUMBER;
/

CREATE OR REPLACE PACKAGE employment_module AS
    PROCEDURE create_or_edit(
        p_employee_id IN employees.employee_id%TYPE,
        p_name IN employees.name%TYPE,
        p_email IN employees.email%TYPE,
        p_phone_number IN employees.phone_number%TYPE,
        p_date_of_joining IN employees.date_of_joining%TYPE
    );

    PROCEDURE remove(
        p_employee_ids IN employee_id_table,
        p_deleted_count OUT NUMBER
    );

    PROCEDURE get_employees(
        p_page_number IN NUMBER,
        p_page_size IN NUMBER,
        p_sort_column IN VARCHAR2,
        p_sort_direction IN VARCHAR2,
        p_employees OUT SYS_REFCURSOR,
        p_total_count OUT NUMBER
    );

    PROCEDURE check_email_exists(
        p_email IN VARCHAR2,
        p_employee_id IN NUMBER,
        p_exists OUT NUMBER
    );
END employment_module;
/

CREATE OR REPLACE PACKAGE BODY employment_module AS
    PROCEDURE create_or_edit(
        p_employee_id IN employees.employee_id%TYPE,
        p_name IN employees.name%TYPE,
        p_email IN employees.email%TYPE,
        p_phone_number IN employees.phone_number%TYPE,
        p_date_of_joining IN employees.date_of_joining%TYPE
    ) IS
        v_count NUMBER;

    BEGIN
        -- Check for duplicate email
        SELECT COUNT(*)
        INTO v_count
        FROM employees
        WHERE email = p_email
          AND (employee_id != p_employee_id OR p_employee_id IS NULL);
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Email already exists');
        END IF;
        --
        IF p_employee_id IS NULL THEN
            INSERT INTO employees (name, email, phone_number, date_of_joining)
            VALUES (p_name, p_email, p_phone_number, p_date_of_joining);
        ELSE
            UPDATE employees
            SET name            = p_name,
                email           = p_email,
                phone_number    = p_phone_number,
                date_of_joining = p_date_of_joining
            WHERE employee_id = p_employee_id;
        END IF;
    END create_or_edit;

    PROCEDURE remove(
        p_employee_ids IN employee_id_table,
        p_deleted_count OUT NUMBER
    ) IS
    BEGIN
        DELETE
        FROM employees
        WHERE employee_id IN (SELECT COLUMN_VALUE FROM TABLE (p_employee_ids));
        p_deleted_count := SQL%ROWCOUNT;
        COMMIT;
    END remove;

    PROCEDURE get_employees(
        p_page_number IN NUMBER,
        p_page_size IN NUMBER,
        p_sort_column IN VARCHAR2,
        p_sort_direction IN VARCHAR2,
        p_employees OUT SYS_REFCURSOR,
        p_total_count OUT NUMBER
    ) AS
        v_offset         NUMBER       := (p_page_number - 1) * p_page_size;
        v_sort_column    VARCHAR2(50) := NVL(p_sort_column, 'EMPLOYEE_ID');
        v_sort_direction VARCHAR2(4)  := UPPER(NVL(p_sort_direction, 'ASC'));
        v_sql            VARCHAR2(1000);
    BEGIN
        IF v_sort_direction NOT IN ('ASC', 'DESC') THEN
            v_sort_direction := 'ASC';
        END IF;

        IF v_sort_column NOT IN ('EMPLOYEE_ID', 'NAME', 'EMAIL', 'PHONE_NUMBER', 'DATE_OF_JOINING') THEN
            v_sort_column := 'EMPLOYEE_ID';
        END IF;

        SELECT COUNT(*) INTO p_total_count FROM EMPLOYEES;

        v_sql := '
        SELECT *
        FROM (
            SELECT e.*, ROWNUM AS rn
            FROM (
                SELECT EMPLOYEE_ID, NAME, EMAIL, PHONE_NUMBER, DATE_OF_JOINING
                FROM EMPLOYEES
                ORDER BY ' || v_sort_column || ' ' || v_sort_direction || '
            ) e
            WHERE ROWNUM <= :max_row
        )
        WHERE rn > :min_row';

        OPEN p_employees FOR v_sql
            USING (v_offset + p_page_size), v_offset;
    END get_employees;

    PROCEDURE check_email_exists(
        p_email IN VARCHAR2,
        p_employee_id IN NUMBER,
        p_exists OUT NUMBER
    ) IS
    BEGIN
        SELECT COUNT(*) INTO p_exists FROM employees
        WHERE email = p_email
          AND (p_employee_id IS NULL OR employee_id != p_employee_id);
    END check_email_exists;
END employment_module;
/
