package com.example.gallery.util;

import com.example.gallery.db.MySQLDataSource;
import com.example.gallery.model.GalleryImage;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageUtils {

	private static final String BASE_PATH = "C:\\src\\test\\images";
	private static final String IMG_DIR = "\\full\\";
	private static final String MINI_DIR = "\\mini";
	private static final String IMG_EXT = ".png";
	public static final String MINIATURE_SUFFIX = "_mini.png";

	public static List<GalleryImage> reloadFiles() throws IOException, SQLException {
		createDirectories();
		List<GalleryImage> galleryImages = new ArrayList<>();
		List<String> fileNumbers = getFullFileNumbers();
		Connection connection = new MySQLDataSource().getConnection();
		for (String fileNumber : fileNumbers) {
			BufferedImage fullImage = getFullImage(fileNumber);
			BufferedImage rescaledImage = getRescaledImage(fullImage);

			if (!miniatureExists(fileNumber)) {
				ImageIO.write(rescaledImage, "png", new File(BASE_PATH + MINI_DIR + "\\" + fileNumber + MINIATURE_SUFFIX));
			}

			String imageName = getImageName(fileNumber, connection);

			GalleryImage galleryImage = new GalleryImage();
			galleryImage.setFileNumber(fileNumber);
			galleryImage.setDisplayName(imageName);

			ByteArrayOutputStream streamFull = new ByteArrayOutputStream();
			ImageIO.write(fullImage, "png", streamFull);
			ByteArrayOutputStream streamMini = new ByteArrayOutputStream();
			ImageIO.write(rescaledImage, "png", streamMini);

			galleryImage.setImage(new String(Base64.getEncoder().encode(streamFull.toByteArray()),
					StandardCharsets.UTF_8));
			galleryImage.setMiniature(new String(Base64.getEncoder().encode(streamMini.toByteArray()),
					StandardCharsets.UTF_8));
			galleryImages.add(galleryImage);
			streamFull.close();
			streamMini.close();
		}
		connection.close();
		return galleryImages;
	}

	public static void saveNewImage(String name, MultipartFile multipartFile) throws SQLException, IOException {
		Connection connection = new MySQLDataSource().getConnection();
		int max = DbUtils.getMaximumKey(connection);
		if (StringUtils.isEmpty(name)) {
			DbUtils.saveImageWithNoName(String.valueOf(max + 1), connection);
		} else {
			DbUtils.saveImageWithName(max + 1, name, connection);
		}
		saveFileToStorage(String.valueOf(max + 1), multipartFile);
		connection.close();
	}

	public static void saveFileToStorage(String name, MultipartFile multipartFile) throws IOException {
		name = name + ".png";
		Path uploadPath = Paths.get(BASE_PATH + IMG_DIR);
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(name);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save image file: " + name, e);
		}
	}

	private static String getImageName(String key, Connection connection) {
		if (!DbUtils.imageExistsInDb(key, connection)) {
			return DbUtils.saveImageWithNoName(key, connection);
		}
		return DbUtils.getImageName(key, connection);
	}

	private static boolean miniatureExists(String fullFileName) {
		return new File(BASE_PATH + MINI_DIR + "\\" + fullFileName + MINIATURE_SUFFIX).exists();
	}

	private static BufferedImage getRescaledImage(BufferedImage img) {
		return Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS);
	}

	private static List<String> getFullFileNumbers() {
		List<String> result = new ArrayList<>();
		final File dir = new File(BASE_PATH + IMG_DIR);
		for (final File f : dir.listFiles()) {
			if (f.isFile()) {
				if (f.getName().matches(".*\\.png")) {
					String s = f.getName().split("\\.png")[0];
					if (StringUtils.isNumeric(s)) {
						result.add(s);
					}
				}
			}
		}
		return result;
	}

	private static BufferedImage getFullImage(String name) throws IOException {
		return getFile(name, IMG_DIR);
	}

	private static BufferedImage getMiniFile(String name) throws IOException {
		return getFile(name, MINI_DIR);
	}

	private static BufferedImage getFile(String name, String dir) throws IOException {
		return ImageIO.read(new File(BASE_PATH + dir + name + IMG_EXT));
	}

	public static void deleteImage(String id) throws IOException {
		File fullFile = new File(BASE_PATH + IMG_DIR + id + IMG_EXT);
		System.out.println("" + BASE_PATH + IMG_DIR + id + IMG_EXT);
		if (ImageIO.read(fullFile) != null) {
			if (fullFile.delete()) {
				System.out.println("Deleted the file: " + fullFile.getName());
			} else {
				System.out.println("Failed to delete the file.");
			}
		}

		System.out.println("" + BASE_PATH + MINI_DIR + "\\" + id + "_mini" + IMG_EXT);
		File miniFile = new File(BASE_PATH + MINI_DIR + "\\" + id + "_mini" + IMG_EXT);
		if (ImageIO.read(miniFile) != null) {
			if (miniFile.delete()) {
				System.out.println("Deleted the file: " + miniFile.getName());
			} else {
				System.out.println("Failed to delete the file.");
			}
		}
	}
	private static void createDirectories() {
		File fullDir = new File(BASE_PATH + IMG_DIR);
		File miniDir = new File(BASE_PATH + MINI_DIR + "\\");
		if (!fullDir.exists()){
			fullDir.mkdir();
		}
		if (!miniDir.exists()){
			miniDir.mkdir();
		}
	}
}