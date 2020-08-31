package com.example.demo.rsocket;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.beans.ImageDataRequestBean;
import com.example.demo.beans.ImageRequestBean;
import com.example.demo.beans.ImageResponseBean;
import com.example.demo.dao.ImageRepository;
import com.example.demo.model.Image;

import reactor.core.publisher.Flux;

/**
 * RSocket controller
 * 
 * @author legioner
 *
 */
@Controller
public class RsocketController {
	
	@Autowired
	private ImageRepository	imageRepository;
	
	/**
	 * Returns file data for files in file names list
	 * @param imageRequest - files names
	 * @return - {@link ImageResponseBean} - file content + file name
	 * @throws Exception 
	 */
    //@MessageMapping("images.request.stream")
	@MessageMapping("")
    public Flux<ImageResponseBean> requestStream(ImageRequestBean imageRequest) throws Exception {
    	String[] files = imageRequest.getFileNames().split("\t");
    	return Flux.fromStream(Stream.of(files).filter(name -> name != null && !name.strip().isEmpty())
   			.map(name -> {
   				var res = new ImageResponseBean();
   				res.setFileName(name);
   				try {
   					res.setData(this.getClass().getClassLoader()
							.getResourceAsStream("images/" + name).readAllBytes());
   				} catch (IOException e) {
   					throw new RuntimeException(e);
   				}
   				return res;
   			}));
    }

	/**
	 * Returns Flux<Image>
	 * @param request - parameter for paging (from, size)
	 * @return
	 */
	@MessageMapping("images")
	public Flux<Image> imagesData(ImageDataRequestBean request) {
		return imageRepository.findAllFromTo(request.getFrom(), request.getSize());
	}
	
}
