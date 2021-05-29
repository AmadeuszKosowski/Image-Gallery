package com.example.gallery.controller;

import com.example.gallery.model.GalleryImage;
import com.example.gallery.util.DbUtils;
import com.example.gallery.util.ImageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class IndexController {

	@GetMapping("/")
	public String index(Model model)
			throws IOException, SQLException {
		List<GalleryImage> images = ImageUtils.reloadFiles();
		model.addAttribute("images", images);
		return "index";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("imageName") String imageName, Model model, RedirectAttributes attributes)
			throws SQLException, IOException {
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}
		ImageUtils.saveNewImage(imageName, file);
		List<GalleryImage> images = ImageUtils.reloadFiles();
		model.addAttribute("images", images);
		return "redirect:/";
	}

	@PostMapping("/delete")
	public String deleteBuyer(@RequestParam("fileNumber") String id) throws IOException {
		ImageUtils.deleteImage(id);
		DbUtils.deleteImage(id);
		return "redirect:/";
	}

}