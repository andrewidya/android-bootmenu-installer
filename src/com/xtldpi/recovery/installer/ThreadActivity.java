package com.xtldpi.recovery.installer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ThreadActivity extends Activity {
	public Process process;

	public void copyAsset(Context context) {
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream install = new DataOutputStream(
					process.getOutputStream());
			install.writeBytes("mount -o remount,rw /dev/block/mtdblock8 /system\n");
			install.writeBytes("echo \"get root access = ok\" > /data/local/tmp/log.txt\n");
			AssetManager myAsset = context.getAssets();
			String[] files = null;
			try {
				files = myAsset.list("File");
				for (String filename : files) {
					InputStream in = null;
					in = myAsset.open("File/" + filename);
					OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + filename);
					int read;
					byte[] buffer = new byte[1024];
					while ((read = in.read(buffer)) != -1) {
						out.write(buffer, 0, read);
					}
					out.close();
				}
				install.writeBytes("mkdir /system/bootmenu\n");
				install.writeBytes("tar -xzf /mnt/sdcard/bootmenu.tar -C /system/bootmenu/\n");
				install.writeBytes("rm -r /sdcard/bootmenu.tar\n");
				install.writeBytes("if [ ! -d /system/etc/init.d ]; then mkdir /system/etc/init.d; cp /system/bootmenu/script/97bootmenu /system/etc/init.d/97bootmenu; else cp /system/bootmenu/script/97bootmenu /system/etc/init.d/97bootmenu; fi\n");
				install.writeBytes("chmod 777 /system/etc/init.d/97bootmenu\n");
				install.writeBytes("cd /system/bootmenu/script\n");
				install.writeBytes("./install.sh");
				install.flush();
				install.close();
				Toast.makeText(context, "Installing recovery done",
						Toast.LENGTH_LONG).show();
			} catch (NullPointerException e) {
				// TODO: handle exception
				Log.e("ListAsset", "Error: " + e.getMessage());
			}
		} catch (IOException e) {
			// TODO: handle exception
			Log.e("ShellLinux", "Cannot copy" + e.getMessage());
		}
	}

	public void uninstall(Context context) {
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream uninstall = new DataOutputStream(
					process.getOutputStream());
			uninstall.writeBytes("rm /system/etc/init.d/97bootmenu\n");
			uninstall.writeBytes("rm -r /system/bootmenu\n");
			uninstall.writeBytes("sync\n");
			uninstall.flush();
			uninstall.close();
			Toast.makeText(context, "Package has been uninstalled",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Uninstall", e.getMessage());
		}
	}

	public void reboot() {
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream reboot = new DataOutputStream(
					process.getOutputStream());
			reboot.writeBytes("cp /system/bootmenu/script/97bootmenu /system/etc/init.d/97bootmenu\n");
			reboot.writeBytes("chmod 777 /system/etc/init.d/97bootmenu\n");
			reboot.writeBytes("reboot\n");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Reboot", e.getMessage());
		}
	}
}
