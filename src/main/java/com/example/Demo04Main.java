package com.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Scanner;

public class Demo04Main {
	public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "nilesh";
	
	static {
		try {
			Class.forName(DB_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// invoke stored procedure with IN param and returning multiple rows
		try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			String sql = "CALL sp_getstudents(?)";
			try(CallableStatement stmt = con.prepareCall(sql)) {
				System.out.print("Enter Min Marks: ");
				double minMarks = sc.nextDouble();
				stmt.setDouble(1, minMarks);
				try(ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
						int roll = rs.getInt("roll");
						String name = rs.getString("name");
						double marks = rs.getDouble("marks");
						System.out.println(roll + ", " + name + ", " + marks);
					}					
				}
			}
		} // con.close()
		catch (Exception e) {
			e.printStackTrace();
		}

		// invoke stored procedure with IN param and OUT param
		try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			String sql = "CALL sp_getmarks(?, ?)";
			try(CallableStatement stmt = con.prepareCall(sql)) {
				System.out.print("Enter roll: ");
				int roll = sc.nextInt();
				stmt.setInt(1, roll);
				stmt.registerOutParameter(2, Types.DOUBLE);
				stmt.execute();
				double marks = stmt.getDouble(2);
				System.out.println("Marks : " + marks);
			}
		} // con.close()
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
