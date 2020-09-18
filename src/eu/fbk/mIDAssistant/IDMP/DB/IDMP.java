package eu.fbk.mIDAssistant.IDMP.DB;

public interface IDMP {
     String Solution();
     String Redirection();
     String Pkce();
     String ClientSecret();
     String DiscoverURL();
     String AuthorizationURL();
     String TokenURL();
     String UserInfoURL();
     String DevDomain();
     String ClientRegistration();
}
