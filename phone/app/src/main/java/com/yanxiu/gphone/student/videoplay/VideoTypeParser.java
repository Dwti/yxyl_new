package com.yanxiu.gphone.student.videoplay;

import android.net.Uri;

/**
 * Created by cailei on 03/07/2017.
 */

public class VideoTypeParser {
    public enum Type {
        UNKNOWN,
        MP4,
        M3U8,
    }

    public static Type getTypeForUri(Uri uri) {
        String filename = uri.getLastPathSegment().toLowerCase();
        if (filename.endsWith(".mp4")) {
            return Type.MP4;
        }

        if (filename.endsWith(".m3u") ||
            filename.endsWith(".m3u8")) {
            return Type.M3U8;
        }

        return Type.UNKNOWN;
    }
}
