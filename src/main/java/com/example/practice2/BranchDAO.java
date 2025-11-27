package com.example.practice2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    public static void addBranch(String name, int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "INSERT INTO branch (branch_name, company_id) VALUES (?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, companyId);

        pst.executeUpdate();
        pst.close();
    }

    public static List<String> getBranches(int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT branch_name FROM branch WHERE company_id = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, companyId);

        ResultSet rs = pst.executeQuery();
        List<String> list = new ArrayList<>();

        while (rs.next()) {
            list.add(rs.getString("branch_name"));
        }

        pst.close();
        return list;
    }

    public static int getBranchId(String name, int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT branch_id FROM branch WHERE branch_name = ? AND company_id = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, companyId);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getInt("branch_id");

        return -1;
    }
}
