package com.example.gallery.util;

import com.example.gallery.model.GalleryImage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ImageUtilsTest {

	@Test
	void name() throws IOException, SQLException {
		List<GalleryImage> galleryImages = ImageUtils.reloadFiles();
	}
}
