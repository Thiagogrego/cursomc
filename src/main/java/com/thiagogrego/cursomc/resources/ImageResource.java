package com.thiagogrego.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thiagogrego.cursomc.services.ImageService;

@RestController
@RequestMapping(value = "/images")
public class ImageResource {

	@Autowired
	private ImageService imageService;
	
	@GetMapping
	@ResponseBody
	public byte[] getBytes () {
		return imageService.getBytes();
	}
}
