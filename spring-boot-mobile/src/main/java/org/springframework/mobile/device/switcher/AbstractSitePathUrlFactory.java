package org.springframework.mobile.device.switcher;

public abstract class AbstractSitePathUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {
    private final String mobilePath;
    private final String tabletPath;
    private final String rootPath;

    public AbstractSitePathUrlFactory(String mobilePath, String tabletPath, String rootPath) {
        this.mobilePath = this.formatPath(mobilePath);
        this.tabletPath = this.formatPath(tabletPath);
        this.rootPath = this.formatPath(rootPath);
    }

    public String getMobilePath() {
        return this.mobilePath;
    }

    public String getTabletPath() {
        return this.tabletPath;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public boolean hasMobilePath() {
        return this.mobilePath != null;
    }

    public boolean hasTabletPath() {
        return this.tabletPath != null;
    }

    public boolean hasRootPath() {
        return this.rootPath != null;
    }

    public String getFullNormalPath() {
        return this.rootPath == null ? "/" : this.getCleanPath(this.rootPath) + "/";
    }

    public String getFullMobilePath() {
        String path = null;
        if (this.mobilePath != null) {
            path = this.rootPath == null ? this.mobilePath : this.getCleanPath(this.rootPath) + this.mobilePath;
        }

        return path;
    }

    public String getFullTabletPath() {
        String path = null;
        if (this.tabletPath != null) {
            path = this.rootPath == null ? this.tabletPath : this.getCleanPath(this.rootPath) + this.tabletPath;
        }

        return path;
    }

    protected String getCleanNormalPath() {
        return this.getCleanPath(this.getFullNormalPath());
    }

    protected String getCleanMobilePath() {
        return this.getCleanPath(this.getFullMobilePath());
    }

    protected String getCleanTabletPath() {
        return this.getCleanPath(this.getFullTabletPath());
    }

    protected String getCleanRootPath() {
        return this.getCleanPath(this.getRootPath());
    }

    private String formatPath(String path) {
        String formattedPath = null;
        if (path != null) {
            formattedPath = path.startsWith("/") ? path : "/" + path;
            formattedPath = formattedPath.endsWith("/") ? formattedPath : formattedPath + "/";
        }

        return formattedPath;
    }

    private String getCleanPath(String path) {
        String cleanPath = null;
        if (path != null) {
            cleanPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        }

        return cleanPath;
    }
}