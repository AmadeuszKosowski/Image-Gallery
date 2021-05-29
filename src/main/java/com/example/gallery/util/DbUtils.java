package com.example.gallery.util;

import com.example.gallery.db.MySQLDataSource;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtils {

	public static boolean imageExistsInDb(String key, Connection connection) {
		String sql = "SELECT count(*) FROM images WHERE imageid=" + key;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) == 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot save to the database.");
		}
		return false;
	}

	public static String saveImageWithNoName(String key, Connection connection) {
		String displayName = "Undefined";
		String sql = String.format("INSERT INTO images VALUES (%s,'%s')", key, displayName);
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.executeUpdate();
			return displayName;
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot save to the database.");
		}
	}

	public static void saveImageWithName(int key, String name, Connection connection) {
		String sql = String.format("INSERT INTO images VALUES (%s,'%s')", key, name);
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot save to the database.");
		}
	}

	public static String getImageName(String key, Connection connection) {
		String sql = "SELECT imagename FROM images WHERE imageid=" + key;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return String.valueOf(resultSet.getString(1));
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot get image name.");
		}
		return "";
	}

	public static int getMaximumKey(Connection connection) {
		String sql = "SELECT MAX(imageid) FROM images";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot get image name.");
		}
		return 0;
	}

	public static void deleteImage(String id) {
		Connection connection = new MySQLDataSource().getConnection();
		String sql = String.format("DELETE FROM images WHERE imageid=%s", id);
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot delete image.");
		}
	}
}
