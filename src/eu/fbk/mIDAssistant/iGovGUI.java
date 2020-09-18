package eu.fbk.mIDAssistant;

import eu.fbk.mIDAssistant.IDMP.DB.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class iGovGUI {
    private JPanel Root;
    private JLabel STAnD;
    private JLabel IDMPSelection;
    private JLabel ConfigurationInfo;
    private JLabel IDMPRedirection;
    private JLabel HTTPSScheme;
    private JLabel CustomURI;
    private JTextField CustomURLTxt;
    private JLabel ClientSecret;
    private JTextField AppSecretTxt;
    private JTextField ScopeTxt;
    private JTextField AppIDTxt;
    private JTextField ButtonTxt;
    private JLabel SchemeTag;
    private JLabel HostTag;
    private JLabel PathTag;
    private JTextField SchemeTxt;
    private JTextField HostTxt;
    private JTextField PathTxt;
    private JLabel ScopeTag;
    private JLabel AppIDTag;
    private JLabel ButtonTag;
    private JRadioButton HTTPSButton;
    private JRadioButton customURLButton;
    private JComboBox IDMPBox;
    private JLabel CustomURLTag;
    private JLabel AppSecretTag;
    private JLabel DevTag;
    private JTextField DevDomainTxt;
    private JTextField LoATxt;
    private JLabel LoATag;
    private JLabel ImpInfo;
    private JRadioButton StaticCR;
    private JRadioButton DynamicCR;
    private JRadioButton BothCR;


    public iGovGUI(){

        IDMPBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) IDMPBox.getSelectedItem();
                update(selected);
                GenerateClassiGov.getInstance().setIdmName(selected);
            }
        });


        HTTPSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (HTTPSButton.isSelected()){
                    customURLButton.setEnabled(false);
                    GenerateClassiGov.getInstance().setHasCustom(false);
                    GenerateClassiGov.getInstance().setHasAppLink(true);
                    HTTPSScheme.setVisible(true);
                    SchemeTag.setVisible(true);
                    HostTag.setVisible(true);
                    PathTag.setVisible(true);
                    SchemeTxt.setVisible(true);
                    HostTxt.setVisible(true);
                    PathTxt.setVisible(true);
                } else {
                    customURLButton.setEnabled(true);
                    HTTPSScheme.setVisible(false);
                    SchemeTag.setVisible(false);
                    HostTag.setVisible(false);
                    PathTag.setVisible(false);
                    SchemeTxt.setVisible(false);
                    HostTxt.setVisible(false);
                    PathTxt.setVisible(false);
                    GenerateClassiGov.getInstance().setHasAppLink(false);
                }

            }
        });

        customURLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customURLButton.isSelected()){
                    HTTPSButton.setEnabled(false);
                    GenerateClassiGov.getInstance().setHasAppLink(false);
                    GenerateClassiGov.getInstance().setHasCustom(true);
                    CustomURI.setVisible(true);
                    CustomURLTag.setVisible(true);
                    CustomURLTxt.setVisible(true);
                } else {
                    HTTPSButton.setEnabled(true);
                    CustomURI.setVisible(false);
                    CustomURLTag.setVisible(false);
                    CustomURLTxt.setVisible(false);
                    GenerateClassiGov.getInstance().setHasCustom(false);
                }

            }
        });
        // Read the Scheme text field of HTTPS Redirection
        SchemeTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScheme(SchemeTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScheme(SchemeTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScheme(SchemeTxt.getText());
            }
        });
        // Read the Host text field of HTTPS Redirection
        HostTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setHost(HostTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setHost(HostTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setHost(HostTxt.getText());
            }
        });
        // Read the Path text field of HTTPS Redirection
        PathTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setPath(PathTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setPath(PathTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setPath(PathTxt.getText());
            }
        });
        // Read the Scope text field
        ScopeTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScope(ScopeTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScope(ScopeTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setScope(ScopeTxt.getText());
            }
        });
        // Read the AppID
        AppIDTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppID(AppIDTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppID(AppIDTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppID(AppIDTxt.getText());
            }
        });
        //Read the Button Name
        ButtonTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setBtnName(ButtonTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setBtnName(ButtonTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setBtnName(ButtonTxt.getText());
            }
        });
        //read the developer domain name
        DevDomainTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setDevDomain(DevDomainTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setDevDomain(DevDomainTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setDevDomain(DevDomainTxt.getText());
            }
        });
        //read the Custom URL value from the text field
        CustomURLTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setCustomURL(CustomURLTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setCustomURL(CustomURLTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setCustomURL(CustomURLTxt.getText());
            }
        });
        // read the App Secret from the text field.
        AppSecretTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppSecret(AppSecretTxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppSecret(AppSecretTxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setAppSecret(AppSecretTxt.getText());
            }
        });

        // read the LoA from the text field.
        LoATxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setLoA(LoATxt.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setLoA(LoATxt.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                GenerateClassiGov.getInstance().setLoA(LoATxt.getText());
            }
        });

    }
    /**
     * Update the plugin GUI questions based on the selected IDMP in ComboBoX.
     * @param name : selected IDMP.
     */
    private void update (String name){
        switch (name) {
            case "POSTEID": {
                IDMP idmp = new POSTEID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "LoginGov": {
                IDMP idmp = new LoginGov();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "TIMID": {
                IDMP idmp = new TIMID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }

                break;
            }
            case "IntesaID": {
                IDMP idmp = new IntesaID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "ArubaID": {
                IDMP idmp = new ArubaID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "SPIDIT": {
                IDMP idmp = new SPIDIT();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "NamirialID": {
                IDMP idmp = new NamirialID();

                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "Lepida": {
                IDMP idmp = new Lepida();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }

                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
            case "InfocertID": {
                IDMP idmp = new InfocertID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }

                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }

            default: {
                IDMP idmp = new SielteID();
                switch (idmp.Redirection()) {
                    case "both":
                        IDMPRedirection.setVisible(true);
                        HTTPSButton.setVisible(true);
                        customURLButton.setVisible(true);
                        //hide the https if activated by previous option
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        // hide the custom if activated by previous provider
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    case "HTTPS":
                        // hide the selection of HTTPS or Custom
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //activate Https
                        HTTPSScheme.setVisible(true);
                        SchemeTag.setVisible(true);
                        HostTag.setVisible(true);
                        PathTag.setVisible(true);
                        SchemeTxt.setVisible(true);
                        HostTxt.setVisible(true);
                        PathTxt.setVisible(true);
                        //hide custom
                        CustomURI.setVisible(false);
                        CustomURLTag.setVisible(false);
                        CustomURLTxt.setVisible(false);
                        break;
                    default:
                        //hide the selection
                        IDMPRedirection.setVisible(false);
                        HTTPSButton.setVisible(false);
                        customURLButton.setVisible(false);
                        //hide https
                        HTTPSScheme.setVisible(false);
                        SchemeTag.setVisible(false);
                        HostTag.setVisible(false);
                        PathTag.setVisible(false);
                        SchemeTxt.setVisible(false);
                        HostTxt.setVisible(false);
                        PathTxt.setVisible(false);
                        //activate Custom
                        CustomURI.setVisible(true);
                        CustomURLTag.setVisible(true);
                        CustomURLTxt.setVisible(true);
                        break;
                }
                if (idmp.ClientSecret().equals("yes")) {
                    ClientSecret.setVisible(true);
                    AppSecretTag.setVisible(true);
                    AppSecretTxt.setVisible(true);
                } else {
                    ClientSecret.setVisible(false);
                    AppSecretTag.setVisible(false);
                    AppSecretTxt.setVisible(false);
                }
                if (idmp.ClientRegistration().equals("both") || idmp.ClientRegistration().equals("static")) {
                    AppIDTag.setVisible(true);
                    AppIDTxt.setVisible(true);
                } else {

                    AppIDTag.setVisible(false);
                    AppIDTxt.setVisible(false);
                }
                if (idmp.DevDomain().equals("yes")) {
                    DevTag.setVisible(true);
                    DevDomainTxt.setVisible(true);
                } else {
                    DevTag.setVisible(false);
                    DevDomainTxt.setVisible(false);
                }
                break;
            }
        }
    }

    /**
     *
     * @return Root GUI Panel
     */
    public JPanel getContent(){
        return Root;
    }
}
