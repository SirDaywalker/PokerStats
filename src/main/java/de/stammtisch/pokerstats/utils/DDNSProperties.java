package de.stammtisch.pokerstats.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("stammtischhub.ddns")
@Component
public class DDNSProperties {

	private String domain;
	
}
