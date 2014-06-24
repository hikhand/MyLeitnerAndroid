package ir.khaled.myleitner.model;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import ir.khaled.myleitner.webservice.Request;

/**
 * Created by kh.bakhtiari on 10/29/13.
 */

public class Device {
    private static final String PARAM_DEVICE_INFO = "deviceInfo";
    private static Device device = null;
    private static String UDK;
    public String appVersionName = "";
    public int appVersionCode = 0;
    private Context context;
    private DeviceInfo deviceInfo = null;
    private SystemInfo systemInfo = null;
    private ProcessorInfo processorInfo = null;
    private MemoryInfo memoryInfo = null;
    private DisplayInfo displayInfo = null;

    private Device(Context context) {
        this.context = context;

        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionCode = pinfo.versionCode;
            appVersionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Device getInstance(Context context) {
        if (device == null) {
            device = new Device(context);
        }
        return device;
    }

    public static String getUDK(Context context) {
        if (UDK == null || UDK.length() == 0)
            UDK = md5(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        return UDK;
    }

    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String createJson(Context context) {
        DeviceInfo deviceInfo = Device.getInstance(context).getDeviceInfo();
        String jsonString = null;

        try {
            Gson gson = new Gson();
            jsonString = gson.toJson(deviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static Request getRequestRegisterDevice(Context context) {
        Request request = new Request(context, Request.REQUEST_REGISTER_DEVICE);
        request.addParam(PARAM_DEVICE_INFO, createJson(context));
        return request;
    }

    public DeviceInfo getDeviceInfo() {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo();
        }
        return deviceInfo;
    }

    public SystemInfo getSystemInfo() {
        if (systemInfo == null) {
            systemInfo = new SystemInfo();
        }
        return systemInfo;
    }

    public MemoryInfo getMemoryInfo() {
        if (memoryInfo == null) {
            memoryInfo = new MemoryInfo();
        }
        return memoryInfo;
    }

    public ProcessorInfo getProcessorInfo() {
        if (processorInfo == null) {
            processorInfo = new ProcessorInfo();
        }
        return processorInfo;
    }

    public DisplayInfo getDisplayInfo() {
        if (displayInfo == null) {
            displayInfo = new DisplayInfo();
        }
        return displayInfo;
    }

    private String getImei() {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

    private String getWlanAddress() {
        WifiManager m_wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return m_wm.getConnectionInfo().getMacAddress();
    }

    private String getBluetoothAddress() {
        String address = "";
        try {
            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (m_BluetoothAdapter.isEnabled()) {
                address = m_BluetoothAdapter.getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

    private int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]+", pathname.getName());
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    private int getFrequencyMax() {
        int temp = -1;
        try {
            BufferedReader localBufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"))));
            temp = Integer.valueOf(localBufferedReader2.readLine());
            localBufferedReader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    @SuppressLint("NewApi")
    private long getTotalMemory() {
        try {
            ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            return memInfo.totalMem;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getTotalMemory1() {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();//meminfo
            arrayOfString = str2.split("\\s+");
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

    private long getInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        if (getSystemInfo().sdk_version >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        } else {
            long count = statFs.getBlockCount();
            long size = statFs.getBlockSize();
            return count * size;
        }
    }

    private long getExternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (getSystemInfo().sdk_version >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        } else {
            long count = statFs.getBlockCount();
            long size = statFs.getBlockSize();
            return count * size;
        }
    }

    private long getExternalStorageFreeSize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (getSystemInfo().sdk_version >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        } else {
            long count = statFs.getBlockCount();
            long size = statFs.getBlockSize();
            return count * size;
        }
    }

    private double getScreenSizeInInch() {
        try {
            DisplayMetrics localDisplayMetrics = context.getResources().getDisplayMetrics();

            double d1 = localDisplayMetrics.widthPixels / localDisplayMetrics.xdpi;
            double d2 = localDisplayMetrics.heightPixels / localDisplayMetrics.ydpi;
            return Math.sqrt(d1 * d1 + d2 * d2);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void invalidateMemoryInfo() {
        memoryInfo = null;
    }

    public class DeviceInfo {
        public SystemInfo system = getSystemInfo();
        public ProcessorInfo processor = getProcessorInfo();
        public MemoryInfo memory = getMemoryInfo();
        public DisplayInfo display = getDisplayInfo();
    }

    public class SystemInfo {
        public String label = Build.ID;                        //Either a changelist number, or a label like "M4-rc20".
        public String displayName = Build.DISPLAY;              //A build ID string meant for displaying to the user
        public String product = Build.PRODUCT;              //The name of the overall product.
        public String deviceName = Build.DEVICE;                //The name of the industrial design.
        public String board = Build.BOARD;                  //The name of the underlying board, like "goldfish".
        public String brand = Build.BRAND;                  //The brand (e.g., carrier) the software is customized for, if any.
        public String model = Build.MODEL;                  //The end-user-visible name for the end product.
        public String manufacture = Build.MANUFACTURER;     //The manufacturer of the product/hardware.

        public String imei;
        public String wlan_address;
        public String bluetooth_address;

        public int sdk_version = Build.VERSION.SDK_INT;

        public String android_id;

        public String udk;

        private SystemInfo() {
            this.imei = Device.device.getImei();
            this.wlan_address = Device.device.getWlanAddress();
            this.bluetooth_address = Device.device.getBluetoothAddress();

            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            udk = getUDK(context);
        }
    }

    public class ProcessorInfo {
        public String cpu_abi = Build.CPU_ABI;              //The name of the instruction set (CPU type + ABI convention) of native code.
        public String cpu_abi2 = null;              //The name of the instruction set (CPU type + ABI convention) of native code.
        public int cores;
        public int frequency_max;

        private ProcessorInfo() {
            this.cores = device.getNumCores();
            this.frequency_max = device.getFrequencyMax();
            int currentApi = Build.VERSION.SDK_INT;
            if (currentApi >= Build.VERSION_CODES.FROYO) {
                this.cpu_abi2 = Build.CPU_ABI2;
            }
        }
    }

    public class MemoryInfo {
        public long ram_size;
        public long storage_internal;
        public long storage_external;
        public long storage_external_free;

        private MemoryInfo() {
            int currentApi = Build.VERSION.SDK_INT;
            if (currentApi >= Build.VERSION_CODES.JELLY_BEAN) {
                this.ram_size = device.getTotalMemory();
            } else {
                this.ram_size = device.getTotalMemory1();
            }
            this.storage_internal = device.getInternalStorageSize();
            this.storage_external = device.getExternalStorageSize();
            this.storage_external_free = device.getExternalStorageFreeSize();
        }
    }

    public class DisplayInfo {
        public int width = 0;
        public int height = 0;
        public double size_inches;
        public float density;
        public int densityDpi;
        public float xdpi;
        public float ydpi;

        private DisplayInfo() {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                this.width = displayMetrics.heightPixels;
                this.height = displayMetrics.widthPixels;
            } else {
                this.width = displayMetrics.widthPixels;
                this.height = displayMetrics.heightPixels;
            }


            size_inches = device.getScreenSizeInInch();

            this.density = displayMetrics.density;
            this.densityDpi = displayMetrics.densityDpi;
            this.xdpi = displayMetrics.xdpi;
            this.ydpi = displayMetrics.ydpi;

        }
    }
}