package com.mda.datagate.sslcertificates;

import android.content.Context;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class MyHttpClient extends DefaultHttpClient {
  final Context context;

  public MyHttpClient(HttpParams httpParams, Context context) {
    super(httpParams);
    this.context = context;
  }

  @Override
  protected ClientConnectionManager createClientConnectionManager() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", newSslSocketFactory(), 443));
    final HttpParams params = new BasicHttpParams();
    final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, registry);
//    final ThreadSafeClientConnManager cm = new SingleClientConnManager(getParams(), registry)
    return cm;
  }

  private SSLSocketFactory newSslSocketFactory() {
    try {
      KeyStore trusted = KeyStore.getInstance("BKS");
//      InputStream in = context.getResources().openRawResource(R.raw.mystore);
      InputStream in = new FileInputStream("face_path");
      try {
        trusted.load(in, "ez24get".toCharArray());
      } finally {
        in.close();
      }
//      return new SSLSocketFactory(trusted);
      return new AdditionalKeyStoresSSLSocketFactory(trusted);
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}