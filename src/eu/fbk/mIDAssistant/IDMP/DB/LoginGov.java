package eu.fbk.mIDAssistant.IDMP.DB;

public class LoginGov implements IDMP {

    String solution = "";
    String redirection ="";
    String pkce = "";
    String clientSecret = "";
    String discoverURL = "";
    String authorizationURL ="";
    String tokenURL = "";
    String userInfoURL ="";
    String devDomain = "";
    String clientRegistration = "";

    @Override
    public String Solution() {
        return solution = "iGov";
    }

    @Override
    public String Redirection() {
        return redirection = "both";
    }

    @Override
    public String Pkce() {
        return pkce = "yes";
    }

    @Override
    public String ClientRegistration() {
        return clientRegistration = "both";
    }

    @Override
    public String ClientSecret() {
        return clientSecret = "no";
    }

    @Override
    public String DiscoverURL() {
        return discoverURL = "https://idp.int.identitysandbox.gov/.well-known/openid-configuration";
    }

    @Override
    public String AuthorizationURL() {
        return authorizationURL = "https://idp.int.identitysandbox.gov/openid_connect/authorize";
    }

    @Override
    public String TokenURL() {
        return tokenURL = "https://idp.int.identitysandbox.gov/api/openid_connect/token";
    }

    @Override
    public String UserInfoURL() {
        return userInfoURL ="https://idp.int.identitysandbox.gov/api/openid_connect/userinfo";
    }

    @Override
    public String DevDomain() {
        return devDomain = "no";
    }
}
