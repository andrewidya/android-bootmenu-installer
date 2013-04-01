package com.xtldpi.recovery.installer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button installButton, rebootButton, uninstallButton;
	ProgressDialog progress;
	ThreadActivity thread = new ThreadActivity();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		installButton = (Button) findViewById(R.id.install_button);
		installButton.setOnClickListener(this);

		rebootButton = (Button) findViewById(R.id.reboot_recovery);
		rebootButton.setOnClickListener(this);

		uninstallButton = (Button) findViewById(R.id.uninstall_recovery);
		uninstallButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View clickedButton) {
		// TODO Auto-generated method stub
		switch (clickedButton.getId()) {
		case R.id.install_button:
			installRecoveryPackage();
			break;
		case R.id.reboot_recovery:
			rebootRecovery();
			break;
		case R.id.uninstall_recovery:
			uninstallRecovery();
			break;
		}
	}

	private void installRecoveryPackage() {
		// TODO Auto-generated method stub
		progress = ProgressDialog.show(this, "", "Loading...");
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					sleep(3000);
					thread.copyAsset(getApplicationContext());
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("Warning", e.getMessage());
				}
				progress.dismiss();
				Looper.loop();
			}
		}.start();
	}

	private void rebootRecovery() {
		// TODO Auto-generated method stub
		progress = ProgressDialog.show(this, "", "Rebooting");
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					sleep(2000);
					thread.reboot();
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("Reboot", e.getMessage());
				}
				Looper.loop();
			}
		}.start();
	}

	private void uninstallRecovery() {
		// TODO Auto-generated method stub
		progress = ProgressDialog.show(this, "", "Uninstalling");
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					sleep(3000);
					thread.uninstall(getApplicationContext());
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("Uninstall", e.getMessage());
				}
				progress.dismiss();
				Looper.loop();
			}
		}.start();
	}

}
