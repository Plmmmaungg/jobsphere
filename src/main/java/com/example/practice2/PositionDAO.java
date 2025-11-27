package com.example.practice2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDAO {

    public static void addPosition(String name, int branchId, int locationId, int companyId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "INSERT INTO position (position_name, branch_id, location_id, company_id) VALUES (?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, branchId);
        pst.setInt(3, locationId);
        pst.setInt(4, companyId);

        pst.executeUpdate();
        pst.close();
    }

    public static List<String> getPositions(int companyId, int branchId, int locationId) throws Exception {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT position_name FROM position WHERE company_id = ? AND branch_id = ? AND location_id = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, companyId);
        pst.setInt(2, branchId);
        pst.setInt(3, locationId);

        ResultSet rs = pst.executeQuery();
        List<String> list = new ArrayList<>();

        while (rs.next()) {
            list.add(rs.getString("position_name"));
        }

        pst.close();
        return list;
    }
}
