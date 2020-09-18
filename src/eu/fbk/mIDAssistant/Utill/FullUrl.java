package eu.fbk.mIDAssistant.Utill;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class FullUrl {
    public static final String PREFIX_HTTPS = "https://";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    private boolean isPortSet = false;
    private String myHost = "";
    private String myMimeType;
    private int myPort = -1;
    private String myScheme = "";

    public FullUrl(String fullUrl) throws MalformedURLException {
        boolean z = false;
        URL url = new URL(fullUrl);
        this.myScheme = url.getProtocol();
        this.myHost = url.getHost();
        this.myPort = url.getPort();
        if (this.myPort >= 0) {
            z = true;
        }
        this.isPortSet = z;
    }

    @NotNull
    public String getScheme() {
        return this.myScheme.toLowerCase(Locale.US);
    }

    public FullUrl setScheme(@NotNull String scheme) {
        this.myScheme = scheme;
        return this;
    }

    @NotNull
    public String getHost() {
        return this.myHost;
    }

    @NotNull
    public FullUrl setHost(@NotNull String host) {
        this.myHost = host;
        return this;
    }

    public int getPort() {
        return this.myPort;
    }

    @NotNull
    public String getPortText() {
        if (this.myPort < 0) {
            return "";
        }
        return String.valueOf(this.myPort);
    }

    @NotNull
    public FullUrl setPort(@NotNull String port) {
        try {
            this.myPort = Integer.valueOf(port).intValue();
            this.isPortSet = true;
        } catch (NumberFormatException e) {
        }
        return this;
    }

    @NotNull
    public FullUrl setPort(int port) {
        this.myPort = port;
        this.isPortSet = true;
        return this;
    }

    public boolean isPortSet() {
        return this.isPortSet;
    }

    @Nullable
    public String getMimeType() {
        return this.myMimeType;
    }

    @NotNull
    public FullUrl setMimeType(@Nullable String mimeType) {
        this.myMimeType = mimeType;
        return this;
    }

    public String toString() {
        if (!StringUtil.isNotEmpty(this.myScheme)) {
            return "";
        }
        return this.myScheme + "://" + this.myHost + (this.isPortSet ? ":" + this.myPort : "");
    }
}