package org.intrigus.kitctf.codeqlctf;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class Hostname {
	public static void main(String[] args) {

		{
			HostnameVerifier verifier = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true; // BAD: accept even if the hostname doesn't match
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(verifier);
		}

		{
			HostnameVerifier verifier = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					try { // GOOD: verify the certificate
						Certificate[] certs = session.getPeerCertificates();
						X509Certificate x509 = (X509Certificate) certs[0];
						check(new String[] { hostname }, x509);
						return true;
					} catch (SSLException e) {
						return false;
					}
				}

				private void check(String[] strings, X509Certificate x509) {
					// a real check should look different of course
					throw new IllegalStateException();
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(verifier);
		}

	}
}
