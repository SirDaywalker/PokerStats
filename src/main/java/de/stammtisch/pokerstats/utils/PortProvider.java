package de.stammtisch.pokerstats.utils;

import java.net.InetAddress;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.stammtisch.pokerstats.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Component
@AllArgsConstructor
public class PortProvider implements ApplicationListener<ServletWebServerInitializedEvent> {
	
	private final EmailService emailService;
	
	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		this.emailService.setHomeUrlPath("http://" + InetAddress.getLoopbackAddress().getHostAddress() + ":" + event.getWebServer().getPort() + "/");
	}

}
