package com.am.design.development.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private Environment env;

    // Specific SSL context for mail only to use custom certificate

    @Bean
    public JavaMailSender javaMailSender(
            SSLContext mailSslContext
    ) {
        // Use properties set in application.yml

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        props.put("mail.smtp.ssl.enable", env.getProperty("spring.mail.properties.mail.smtp.ssl.enable"));
        props.put("mail.smtp.ssl.socketFactory", mailSslContext.getSocketFactory());

        return mailSender;
    }

    @Bean
    public SSLContext mailSslContext() throws Exception {
        // Carica il certificato dal file
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream certStream = getClass().getResourceAsStream("/cert/mail.crt");
        if (certStream == null) {
            throw new RuntimeException("Certificate not found: /cert/mail.crt");
        }
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(certStream);

        // Crea un keystore e aggiungi il certificato
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("caCert", caCert);

        // Crea un TrustManager che utilizza il keystore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];

        // Crea un SSLContext che utilizza il TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new javax.net.ssl.TrustManager[]{trustManager}, new java.security.SecureRandom());

        return sslContext;
    }
}
