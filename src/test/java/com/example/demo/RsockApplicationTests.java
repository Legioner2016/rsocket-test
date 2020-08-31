package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.example.demo.beans.ImageDataRequestBean;
import com.example.demo.model.Image;

import io.rsocket.metadata.WellKnownMimeType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * RSocket Java client
 * (test with basic security) 
 * 
 * @author legioner
 *
 */
@SpringBootTest
@Slf4j
class RsockApplicationTests {

	private static final MimeType SIMPLE_AUTH = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString()); 

	private RSocketRequester rsocketRequester;
	
    private static final String CLIENT_ID = UUID.randomUUID().toString();
	
	@Autowired
    private RSocketRequester.Builder rsocketRequesterBuilder;

    @Test
    void testRSocket() {
    	//Prepare security metadata
        UsernamePasswordMetadata user = new UsernamePasswordMetadata("admin", "test"); 

        try {
        	//Create rsocketRequester
			this.rsocketRequester = rsocketRequesterBuilder
			        .setupRoute("shell-client")
			        .setupData(CLIENT_ID)
			        .setupMetadata(user, SIMPLE_AUTH) 
			        .rsocketStrategies(builder ->
			                builder.encoder(new SimpleAuthenticationEncoder()))
			        .connectWebSocket(new URI("ws://localhost:6565/rsocket"))
			        .block();
		} catch (URISyntaxException e) {
			log.error("error connecting", e);
		}

        //Test get data with Rsocket
        RSocketClient rSocketClient = new RSocketClient();
        Flux<Image> images = rSocketClient.images(0, 10);
        assertNotNull(images);
        
        Flux<Image> fiveImgaes = images.take(5);
        assertEquals(5, fiveImgaes.count().block());
        assertEquals(1, fiveImgaes.blockFirst().getId());
        
        log.info(fiveImgaes.blockLast().toString());
        
    }
	
 	public class RSocketClient {
 	    public Flux<Image> images(Integer from, Integer size) {
 	    	return rsocketRequester.route("images")
 	    			       .data(new ImageDataRequestBean(from, size))
 	    			       .retrieveFlux(Image.class);
 	    }
 	}
 	
	
}
