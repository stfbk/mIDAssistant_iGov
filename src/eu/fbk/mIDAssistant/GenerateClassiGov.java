package eu.fbk.mIDAssistant;

public class GenerateClassiGov {
    private String idmName;
    private static GenerateClassiGov INSTANCE;
    private GenerateClassiGov(){
        idmName = "test";
    }
    public static GenerateClassiGov getInstance () {
        if (INSTANCE == null){
            INSTANCE = new GenerateClassiGov();
        }
        return INSTANCE;
    }
    private String classNameAuth = "AuthStateManager";
    private String classNameConf = "Configuration";
    private String classNameCon = "ConnectionBuilderForTesting";
    private String classNameTokenOIDC = "TokenActivity";
    private String classNameTokenOAuth= "TokenActivity";
    private String scheme;
    private String host;
    private String path;
    private String customURL;
    private String appSecret;
    private String scope;
    private String appID;
    private String btnName;
    private String devDomain;
    private String loA;
    private boolean hasCustom =false;
    private boolean hasAppLink= false;
    private boolean hasRedirect= false;
    private boolean hasAppID, hasBtnName, hasDevDomain, hasSecret = false;

    String getClassNameAuth() {
        return classNameAuth;
    }

    String getClassNameConfiguration() {
        return classNameConf;
    }

    String getClassNameConnectionBuilderForTesting() {
        return classNameCon;
    }

    public String getClassNameTokenOIDC() {
        return classNameTokenOIDC;
    }

    String getClassNameTokenOAuth() {
        return classNameTokenOAuth;
    }

    public String getScheme(){
        return scheme;
    }

    public String getHost(){
        return host;
    }

    String getPath(){
        return path;
    }

    String getCustomURL() {return customURL;}

    String getAppSecret() {return appSecret;}

    public String getScope() {return scope;}

    String getAppID() {return appID;}

    String getBtnName() {return btnName;}

    String getDevDomain(){return devDomain;}

    String getLoA() {return loA;}

    String getIdmName() {return idmName;}

    public void setHost(String host){this.host=host;}

    public void setScheme(String scheme){this.scheme=scheme;}

    void setPath(String path){this.path=path;}

    void setCustomURL(String customURL){this.customURL=customURL;}

    public void setScope(String scope){this.scope=scope;}

    void setAppID(String appID){this.appID=appID;}

    void setBtnName(String btnName){this.btnName=btnName;}

    void setDevDomain(String devDomain){this.devDomain=devDomain;}

    void setLoA (String loA) {this.loA=loA;}

    void setAppSecret(String appSecret){this.appSecret=appSecret;}

    void setIdmName(String idmName){this.idmName = idmName;}

    void setHasCustom(boolean hasCustom){
        this.hasCustom = hasCustom;
    }


    void setHasAppLink(boolean hasAppLink){
        this.hasAppLink = hasAppLink;
    }

    boolean isHasCustom(){
        return hasCustom;
    }


    boolean isHasTest(){
        return true;
    }

    boolean isHasAppLink(){
        return hasAppLink;
    }



    boolean isHasRedirect(){
        if (isHasCustom() || isHasAppLink())
            hasRedirect=true;
        return hasRedirect;
    }


}
