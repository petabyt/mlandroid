package fm.magiclantern.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import fm.magiclantern.app.ptpip.PtpIpConnection;
import fm.magiclantern.app.PtpTransport.*;
import fm.magiclantern.app.PtpExceptions.*;

public class MainActivity extends Activity {
	public static class main { public static WebView webview; }

	public static class log {
		public static void webview(final String foo) {
			// Run on webview thread
			main.webview.post(new Runnable() {
				@Override
				public void run() {
					main.webview.loadUrl("javascript:log(`" + foo + "`)");
				}
			});
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		main.webview = new WebView(this);
		WebSettings settings = main.webview.getSettings();
		settings.setJavaScriptEnabled(true);

		settings.setBuiltInZoomControls(true);
		settings.setDomStorageEnabled(true);
		settings.setLoadWithOverviewMode(true);
		main.webview.loadUrl("file:///android_asset/index.html");

		main.webview.addJavascriptInterface(new backend(), "b");

		setContentView(main.webview);
	}

	// Access like "b.test()"
	public class backend {
		@JavascriptInterface
		public void connect() {
			String friendlyName = "MyName.name";
			short[] guid =
				new short[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					      0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

			new Thread(() -> {
				InetAddress ipAddr;
				try {
					ipAddr = InetAddress.getByName("192.168.1.2");
				} catch (UnknownHostException e) {
					log.webview("Couldn't get IP address.");
					return;
				}

				log.webview("Initializing...");
				PtpTransport.ResponderAddress address = new PtpIpConnection.PtpIpAddress(ipAddr);
				PtpTransport.HostId hostId = new PtpIpConnection.PtpIpHostId(guid, friendlyName, 1, 1);
				PtpTransport transport = new PtpIpConnection();
				PtpConnection connection = new PtpConnection(transport);

				log.webview("Connecting...");
				try {
					connection.connect(address, hostId);
				} catch (Exception e) {
					e.printStackTrace();
					log.webview("Couldn't connect.");
					return;
				}

				log.webview("Connected. May need to press OK.");

				log.webview("Opening a PTP session...");
				PtpSession session;
				try {
					session = connection.openSession();
				} catch (Exception e) {
					e.printStackTrace();
					log.webview("Couldn't open session");
					return;
				}
	
				log.webview("Getting device info...");
				PtpDataType.DeviceInfoDataSet deviceInfo =
					session.getConnection().getDeviceInfo();
				log.webview("Model: " + deviceInfo.mModel);
				log.webview("Firmware: " + deviceInfo.mDeviceVersion);

				log.webview("Running event proc...");
				try {
						session.eventProcedure();
				} catch (Exception e) {
					log.webview("Failed to run event proc.");
					e.printStackTrace();
					return;
				}

				log.webview("Closing session...");
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
					log.webview("Couldn't close session");
					return;
				}
			}).start();
		}
	}

	//public native String stringFromJNI();
	//public native String[] arrayTest();

	//static {
	//    System.loadLibrary("hello-jni");
	//}
}
