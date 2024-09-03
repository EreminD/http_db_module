package ru.inno.x_clients.db;

import ru.inno.x_clients.model.CompanyDB;

import java.sql.SQLException;

public interface CompanyRepository {

    int createCompany(String name, String descr) throws SQLException;

    CompanyDB selectById(int companyId) throws SQLException;
}
