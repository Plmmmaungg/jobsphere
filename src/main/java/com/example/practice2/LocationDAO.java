package com.example.practice2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public static void addLocation(String name, int branchId, int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "INSERT INTO location (location_name, branch_id, company_id) VALUES (?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, branchId);
        pst.setInt(3, companyId);

        pst.executeUpdate();
        pst.close();
    }

    public static List<String> getLocations(int companyId, int branchId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT location_name FROM location WHERE company_id = ? AND branch_id = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, companyId);
        pst.setInt(2, branchId);

        ResultSet rs = pst.executeQuery();
        List<String> list = new ArrayList<>();

        while (rs.next()) {
            list.add(rs.getString("location_name"));
        }

        pst.close();
        return list;
    }

    public static int getLocationId(String name, int branchId, int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT location_id FROM location WHERE location_name = ? AND branch_id = ? AND company_id = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, branchId);
        pst.setInt(3, companyId);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getInt("location_id");

        return -1;
    }
}
