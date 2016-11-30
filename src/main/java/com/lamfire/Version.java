package com.lamfire;

import com.lamfire.utils.JvmInfo;
import com.lamfire.utils.NumberUtils;
import com.lamfire.utils.StringUtils;

/**
 * Version info
 * User: lamfire
 * Date: 14-3-27
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
public class Version {
    public static final String VERSION = "2.3.23";

    public static String getJavaVersion() {
        JvmInfo jvm = JvmInfo.getInstance();
        return jvm.getJdkVersion();
    }

    public static String getVersion() {
        return VERSION;
    }

    public static boolean isJavaVersion(String version) {
        return StringUtils.isStartWith(getJavaVersion(), version);
    }

    static int[] getJavaVersions(String version) {
        int[] versions = new int[4];
        String[] jvs = StringUtils.split(version, ".");
        String[] minVs = StringUtils.split(jvs[2], '_');

        versions[0] = NumberUtils.toInt(jvs[0], 0);
        versions[1] = NumberUtils.toInt(jvs[1], 0);
        versions[2] = NumberUtils.toInt(minVs[0], 0);
        versions[3] = NumberUtils.toInt(minVs[1], 0);
        return versions;
    }

    public static boolean isJavaVersionAndLater(String version) {
        int[] versions = getJavaVersions(version);
        return isJavaVersionAndLater(versions[0], versions[1], versions[2], versions[3]);
    }

    public static boolean isJavaVersionAndLater(int... versions) {
        String javaVer = getJavaVersion();
        int[] jvs = getJavaVersions(javaVer);

        for (int i = 0; i < jvs.length; i++) {
            if (i >= versions.length) {
                break;
            }
            if (jvs[i] < versions[i]) {
                return false;
            }
        }

        return true;
    }
}
