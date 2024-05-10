package de.stammtisch.pokerstats.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.stammtisch.pokerstats.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Component
@AllArgsConstructor
public class DomainProvider implements ApplicationListener<ServletWebServerInitializedEvent> {
	
	private final EmailService emailService;
	
	private final DDNSProperties ddnsProperties;
	
	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		ArrayList<String> macAddresses = new ArrayList<>();
		
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	        while (networkInterfaces.hasMoreElements()) {
	        	NetworkInterface networkInterface = networkInterfaces.nextElement();
	            byte[] mac = networkInterface.getHardwareAddress();
	            if (mac != null) {
	            	StringBuilder stringBuilder = new StringBuilder();
	            	for (int i = 0; i < mac.length; i++) {
	            		stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
	            	}
	            	macAddresses.add(stringBuilder.toString());
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (macAddresses.contains("18:03:73:25:40:47")) {
			this.emailService.setHomeDomain("http://" + ddnsProperties.getDomain() + "/");
		}
		else {
			this.emailService.setHomeDomain("http://" + InetAddress.getLoopbackAddress().getHostAddress() + ":" + event.getWebServer().getPort() + "/");
		}
		
	}

}
