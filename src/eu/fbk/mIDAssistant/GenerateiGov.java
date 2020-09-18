package eu.fbk.mIDAssistant;

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel;
import com.android.tools.idea.gradle.dsl.api.android.AndroidModel;
import com.android.tools.idea.gradle.dsl.api.dependencies.DependenciesModel;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.json.psi.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import eu.fbk.mIDAssistant.IDMP.DB.IDMP;
import org.jetbrains.android.dom.manifest.Manifest;
import org.jetbrains.android.facet.AndroidFacet;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.android.tools.idea.gradle.dsl.api.dependencies.CommonConfigurationNames.IMPLEMENTATION;

/**
 * Class GenerateAuthentication is in charge of create the main code
 * add the intent-filter inside the manifest.xml
 * add the AppAuth classes
 * add the json configuration file, button name, and TokenActivity class
 */
public class GenerateiGov extends AnAction {
    private InsertCodeHandlerGov insertCodeHandlerGov;
    private Project myProject;
    private PsiDirectory psiDirectory;
    private PsiDirectory resourceDirectory;
    private PsiManager psiManager;
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiClass psiclass = getPsiClassFromContext(e);
        myProject = psiclass.getProject();
        e.getPresentation().setEnabled(psiclass != null);
        GenerateDialogiGov dlg = new GenerateDialogiGov (psiclass);
        dlg.show();
        String idpName = GenerateClassiGov.getInstance().getIdmName();
        if ((idpName.equals("POSTEID") || idpName.equals("TIMID") || idpName.equals("IntesaID") || idpName.equals("ArubaID") || idpName.equals("SPIDItalia")|| idpName.equals("NAMIRIALID") || idpName.equals("LEPIDA") || idpName.equals("INFOCERTID") || idpName.equals("SielteID")) && (!GenerateClassiGov.getInstance().isHasRedirect()) && (GenerateClassiGov.getInstance().getAppID().equals("")) && (GenerateClassiGov.getInstance().getBtnName().equals("")) && (GenerateClassiGov.getInstance().getLoA().equals("")))
            {
                if (dlg.isOK()) {
                    Messages.showErrorDialog("Please complete the needed information ", "Please, Fill The Mandatory Fields");
                }

        } else {
            if (dlg.isOK()) {
                dlg.doValidate();
                generateComparable(psiclass);
                insertCodeHandlerGov = new InsertCodeHandlerGov(psiclass);
                insertCodeHandlerGov.invoke(myProject);
                generateSupportiveClasses();
                generateJsonConfig();
                try {
                    inspectBuildGradle();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                inspectJSONFile();
            }
        }
    }



    private void generateComparable(PsiClass psiClass) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {

            @Override
            protected void run() throws Throwable {

                generateCompareTo(psiClass);
            }
        }.execute();
    }

    /**
     * this class will generate necessary classes that are used to integrate the AppAuth Library into the project
     * In order to implement this part the file template concept has been used
     */
    private void generateSupportiveClasses() {
        String glideTemplate = "AppAuthGlideModuleTemplate.java";
        String configTemplate = "ConfigurationTemplate.java";
        String connectionTemplate = "ConnectionBuilderForTestingTemplate.java";
        String authStateManagerTemplate = "AuthStateManagerClassTemplate.java";
        String oidchWithRefTokentemplate = "TokenActivityOIDCWithRefTokenTemplate.java";
        JavaDirectoryService.getInstance().createClass(psiDirectory, "AppAuthGlideModule", glideTemplate, true);
        JavaDirectoryService.getInstance().createClass(psiDirectory, GenerateClassiGov.getInstance().getClassNameAuth(), authStateManagerTemplate, true);
        JavaDirectoryService.getInstance().createClass(psiDirectory, GenerateClassiGov.getInstance().getClassNameConfiguration(),configTemplate , true);
        JavaDirectoryService.getInstance().createClass(psiDirectory, GenerateClassiGov.getInstance().getClassNameConnectionBuilderForTesting(), connectionTemplate, true);

        if (GenerateClassiGov.getInstance().isHasTest()){
            JavaDirectoryService.getInstance().createClass(psiDirectory, GenerateClassiGov.getInstance().getClassNameTokenOAuth(),oidchWithRefTokentemplate , true);
            generateUserInterfaceFile();
            inspectAndroidManifest();
        }
    }

    /**
     * this class will generate the TokenActivity.xml layout file from the file template
     * by the time in this version we decided not to add the classes with the purpose of testing
     */
    private void generateUserInterfaceFile() {
        psiManager = psiManager.getInstance(myProject);
        String pathToFile = myProject.getBasePath() + "/app/src/main/res/layout/activity_main.xml";
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(pathToFile).getParent();
        FileTemplate fileTemplate = FileTemplateManager.getInstance(myProject).getInternalTemplate("TokenActivity");
        resourceDirectory = psiManager.findDirectory(virtualFile);
        CreateFileFromTemplateAction.createFileFromTemplate("activity_token", fileTemplate, resourceDirectory,null,true);

    }

    private void generateJsonConfig() {
        psiManager = psiManager.getInstance(myProject);
        String pathToFile = myProject.getBasePath() + "/app/src/main/res/raw/";
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(pathToFile);
        FileTemplate fileTemplate = FileTemplateManager.getInstance(myProject).getInternalTemplate("ConfigJson");
        resourceDirectory = psiManager.findDirectory(virtualFile);
        CreateFileFromTemplateAction.createFileFromTemplate("authentication_config", fileTemplate, resourceDirectory,null,true);

    }

    /**
     * inspectAndroidManifest get an instance of manifest file and will add the intent filter related to the usage of applink
     * By the time in order to avoid Merge Manifest error I add an empty appAuthRedirectScheme place holder in the build.gradle.
     * because the problem is related to the attribute called tools:node that should be avoid in the cases that we had custom_uri.
     */
    private void inspectAndroidManifest() {
        //String pathToManifest = myProject.getBasePath() + "/app/src/main/AndroidManifest.xml";
        //VirtualFile manifestFile = LocalFileSystem.getInstance().findFileByPath(pathToManifest);
        Module[] modules = ModuleManager.getInstance(myProject).getSortedModules();
        // get all modules of all open projects
        // Module[] modules = getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getSortedModules();
        Module module = modules[modules.length-2];
        AndroidFacet facet=AndroidFacet.getInstance(module);
        Manifest manifest = facet.getManifest();
        if (manifest==null){
            return;
        }
        final XmlTag manifestTag = manifest.getXmlTag();
        if (manifestTag == null){
            return;
        }

        XmlTag applicationTag = manifestTag.findFirstSubTag("application");
        if (applicationTag==null){
            return;
        }

        final PsiFile manifestFile = manifestTag.getContainingFile();
        if (manifestFile == null){
            return;
        }
        VirtualFile vManifestFile = manifestFile.getVirtualFile();
        if (vManifestFile == null || !ReadonlyStatusHandler.ensureFilesWritable(manifestFile.getProject(),vManifestFile)){
            return;
        }

        XmlTag activityTag = XmlElementFactory.getInstance(myProject).createTagFromText("<activity");
        activityTag.setAttribute("android:name",".TokenActivity");
        XmlTag redActivityTag = XmlElementFactory.getInstance(myProject).createTagFromText("<activity");
        redActivityTag.setAttribute("android:name","net.openid.appauth.RedirectUriReceiverActivity");
        //redActivityTag.setAttribute("tools:node","replace");
        XmlTag intentTag = XmlElementFactory.getInstance(myProject).createTagFromText("<intent-filter");
        if (GenerateClassiGov.getInstance().isHasAppLink()){
            intentTag.setAttribute("android:autoVerify","true");}
        XmlTag actionTag = XmlElementFactory.getInstance(myProject).createTagFromText("<action");
        actionTag.setAttribute("android:name","android.intent.action.VIEW");
        XmlTag categoryTag = XmlElementFactory.getInstance(myProject).createTagFromText("<category");
        categoryTag.setAttribute("android:name","android.intent.category.DEFAULT");
        XmlTag categoryTag2 = XmlElementFactory.getInstance(myProject).createTagFromText("<category");
        categoryTag2.setAttribute("android:name","android.intent.category.BROWSABLE");
        XmlTag dataTag = XmlElementFactory.getInstance(myProject).createTagFromText("<data");

        if (GenerateClassiGov.getInstance().getPath()!= null){
            dataTag.setAttribute("android:scheme",GenerateClassiGov.getInstance().getScheme().substring(0,GenerateClassiGov.getInstance().getScheme().indexOf(":")));
            dataTag.setAttribute("android:host", GenerateClassiGov.getInstance().getHost());
            dataTag.setAttribute("android:path",GenerateClassiGov.getInstance().getPath());
        } else {
            dataTag.setAttribute("android:scheme",GenerateClassiGov.getInstance().getScheme().substring(0,GenerateClassiGov.getInstance().getScheme().indexOf(":")));
            dataTag.setAttribute("android:host", GenerateClassiGov.getInstance().getHost());
        }

        intentTag.addSubTag(actionTag,true);
        intentTag.addSubTag(categoryTag,false);
        intentTag.addSubTag(categoryTag2,false);
        intentTag.addSubTag(dataTag,false);
        redActivityTag.addSubTag(intentTag,true);

        if (GenerateClassiGov.getInstance().isHasAppLink()) {
            WriteCommandAction.runWriteCommandAction(myProject, () -> {applicationTag.addSubTag(activityTag, true);});
            WriteCommandAction.runWriteCommandAction(myProject, () -> {applicationTag.addSubTag(redActivityTag, false);});
            CodeStyleManager.getInstance(manifestFile.getProject()).reformat(manifestFile);
        } else {
            WriteCommandAction.runWriteCommandAction(myProject, () -> {applicationTag.addSubTag(activityTag, true);});
            CodeStyleManager.getInstance(manifestFile.getProject()).reformat(manifestFile);
        }

    }

    /**
     * inspectBuildGradle get a gradle instance and modify it to add the dependencies and in case of the custom_url
     * it will add the manifestPlaceHolder equal to the custom url value.
     * In the final version this method is not working anymore, so I replaced it with the new function.
     */
    /*private void inspectBuildGradle() {
        // Get the module object for the current project
        Module[] modules = ModuleManager.getInstance(myProject).getSortedModules();
        //Module[] modules= ModuleManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getSortedModules();
        Module module = modules[modules.length-2];
        GradleBuildModel model = GradleBuildModel.get(module);
        assert model != null;
        AndroidModel androidModel = model.android();
        if (GenerateClassAuthentication.getInstance().isHasCustom()){

            String cutomUrl = GenerateClassAuthentication.getInstance().getCustomURL();
            if (cutomUrl.contains(":") && androidModel!=null){
                cutomUrl = cutomUrl.substring(0, cutomUrl.indexOf(":"));
                androidModel.defaultConfig().setManifestPlaceholder("appAuthRedirectScheme",cutomUrl);
            }else  {
                cutomUrl = cutomUrl.substring(0, cutomUrl.indexOf("/"));
                assert androidModel != null;
                androidModel.defaultConfig().setManifestPlaceholder("appAuthRedirectScheme", cutomUrl);
            }
        }else {
            assert androidModel != null;
            androidModel.defaultConfig().setManifestPlaceholder("appAuthRedirectScheme","");
        }
        DependenciesModel dependenciesModel = model.dependencies();
        dependenciesModel.addArtifact(IMPLEMENTATION, "net.openid:appauth:0.7.0");
        dependenciesModel.addArtifact(IMPLEMENTATION, "com.android.support:design:28.0.0");
        dependenciesModel.addArtifact(IMPLEMENTATION, "joda-time:joda-time:2.9.9");
        dependenciesModel.addArtifact(IMPLEMENTATION, "com.squareup.okio:okio:1.14.1");
        dependenciesModel.addArtifact(IMPLEMENTATION, "com.github.bumptech.glide:glide:4.7.1");
        dependenciesModel.addArtifact("annotationProcessor", "com.github.bumptech.glide:compiler:4.7.1");
        WriteCommandAction.runWriteCommandAction(myProject, () -> {model.applyChanges();});
    }*/

    private void inspectBuildGradle() throws IOException {

        String buildFilePath = myProject.getBasePath() + "/app/build.gradle";

        String cutomUrl;

        if (GenerateClassiGov.getInstance().isHasCustom()){

            cutomUrl = GenerateClassiGov.getInstance().getCustomURL();
            if (cutomUrl.contains(":")) {
                cutomUrl = cutomUrl.substring(0, cutomUrl.indexOf(":"));

            }else  {
                cutomUrl = cutomUrl.substring(0, cutomUrl.indexOf("/"));

            }
        }else {

            cutomUrl = "";
        }

        BufferedReader br_build = new BufferedReader(new FileReader(buildFilePath));

        StringBuilder newBuildFile = new StringBuilder();
        String line;

        while ((line = br_build.readLine()) != null) {
            newBuildFile.append(line).append("\n");
            if (line.contains("defaultConfig {")){
                newBuildFile.append("manifestPlaceholders" + " " + "appAuthRedirectScheme:").append("\"").append(cutomUrl).append("\"").append("\n");
            }else if (line.contains("dependencies {")) {
                newBuildFile.append("implementation" + " ").append("\'net.openid:appauth:0.7.1\'").append("\n");
                newBuildFile.append("implementation" + " ").append("\'joda-time:joda-time:2.9.9\'").append("\n");
                newBuildFile.append("implementation" + " ").append("\'com.squareup.okio:okio:1.14.1\'").append("\n");
                newBuildFile.append("implementation" + " ").append("\'com.github.bumptech.glide:glide:4.7.1\'").append("\n");
                newBuildFile.append("annotationProcessor" + " ").append("\'com.github.bumptech.glide:compiler:4.7.1\'").append("\n");
            }
        }
        br_build.close();

        FileWriter fw_build = new FileWriter(buildFilePath);
        fw_build.write(newBuildFile.toString());
        fw_build.close();
    }

    /**
     * the purpose of this class is to read the empty json structure in the specifiec path of developer App
     * then create values from UI and IDP classes and assign it into JSON.
     */
    private void inspectJSONFile() {
        String idpName = GenerateClassiGov.getInstance().getIdmName();
        String devDomain, discoveryURL = " ", c_ID = " ", r_URI = " ", scope = " ";
        String JSONPath = myProject.getBasePath() + "/app/src/main/res/raw/authentication_config.json";
        VirtualFile jVFile = LocalFileSystem.getInstance().findFileByPath(JSONPath);
        assert jVFile != null;
        Document document = FileDocumentManager.getInstance().getDocument(jVFile);
        assert document != null;
        PsiFile jFile = PsiDocumentManager.getInstance(myProject).getPsiFile(document);
        JsonElementGenerator jsonElementGenerator = new JsonElementGenerator(myProject);
        System.out.println(idpName);
        Class classINeedAtRuntime;
        try {
            classINeedAtRuntime = Class.forName("eu.fbk.mIDAssistant.IDMP.DB." + idpName);
            Constructor constructor = classINeedAtRuntime.getConstructor();
            Object newObject = constructor.newInstance();
            IDMP idmp = (IDMP)newObject;
            devDomain = GenerateClassiGov.getInstance().getDevDomain();
            if (devDomain != null){

                discoveryURL = devDomain + idmp.DiscoverURL();
            } else {
                discoveryURL = idmp.DiscoverURL();
            }

            c_ID =  GenerateClassiGov.getInstance().getAppID();
            scope = GenerateClassiGov.getInstance().getScope();
            if(GenerateClassiGov.getInstance().isHasCustom()){
                r_URI = GenerateClassiGov.getInstance().getCustomURL();
            } else if (GenerateClassiGov.getInstance().isHasAppLink() & GenerateClassiGov.getInstance().getPath()!=null) {
                r_URI = GenerateClassiGov.getInstance().getScheme() + GenerateClassiGov.getInstance().getHost() + GenerateClassiGov.getInstance().getPath();
            } else {
                r_URI = GenerateClassiGov.getInstance().getScheme() + GenerateClassiGov.getInstance().getHost();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("I dont have the class " + idpName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("I dont have the constructor for the class " + idpName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
         PsiElement c_IDElement = jsonElementGenerator.createValue("\"" + c_ID + "\"");
         PsiElement d_URIElement = jsonElementGenerator.createValue("\"" + discoveryURL + "\"");
         PsiElement r_URIElement = jsonElementGenerator.createValue("\"" + r_URI + "\"");
         PsiElement scope_Element = jsonElementGenerator.createValue("\"" + scope + "\"");
        if (jFile instanceof JsonFile){
            if (((JsonFile) jFile).getTopLevelValue() instanceof JsonObject){
                JsonObject object = (JsonObject) ((JsonFile) jFile).getTopLevelValue();
               if (object!=null){
                   JsonProperty cID = object.findProperty("client_id");
                   JsonProperty R_URI = object.findProperty("redirect_uri");
                   JsonProperty Scope = object.findProperty("authorization_scope");
                   JsonProperty D_URI = object.findProperty("discovery_uri");
                   JsonStringLiteral cIDLiteral = (JsonStringLiteral) cID.getValue();
                   JsonStringLiteral R_URILiteral = (JsonStringLiteral) R_URI.getValue();
                   JsonStringLiteral scopeLiteral = (JsonStringLiteral) Scope.getValue();
                   JsonStringLiteral D_URILiteral = (JsonStringLiteral) D_URI.getValue();
                   WriteCommandAction.runWriteCommandAction(myProject, () -> {cIDLiteral.replace(c_IDElement);});
                   WriteCommandAction.runWriteCommandAction(myProject, () -> {R_URILiteral.replace(r_URIElement);});
                   WriteCommandAction.runWriteCommandAction(myProject, () -> {scopeLiteral.replace(scope_Element);});
                   WriteCommandAction.runWriteCommandAction(myProject, () -> {D_URILiteral.replace(d_URIElement);});
               }
            }
        }

    }
    /**
     * @param psiClass
     * this class will insert the methods and parameters definition into the login activity
     */
    private void generateCompareTo(PsiClass psiClass) {
        String loaPar = GenerateClassiGov.getInstance().getLoA();
        StringBuilder createAuthRequestBuilder = new StringBuilder("private void createAuthRequest() {\n" +
                "        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(\n" +
                "                mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration(),\n" +
                "                mClientId.get(),\n" +
                "                ResponseTypeValues.CODE,\n" +
                "                mConfiguration.getRedirectUri())\n" +
                "                .setScope(mConfiguration.getScope());\n" +
                "        authRequestBuilder.setAdditionalParameters(additionalParams());\n"+
                "        mAuthRequest.set(authRequestBuilder.build());\n" +
                "    }");
        StringBuilder AuthorizationServiceBuilder = new StringBuilder("private AuthorizationService createAuthorizationService() {\n" +
                "        Log.i(TAG, \"Creating authorization service\");\n" +
                "        AppAuthConfiguration.Builder builder = new AppAuthConfiguration.Builder();\n" +
                "        builder.setBrowserMatcher(mBrowserMatcher);\n" +
                "        builder.setConnectionBuilder(mConfiguration.getConnectionBuilder());\n" +
                "\n" +
                "        return new AuthorizationService(this, builder.build());\n" +
                "    }");
        StringBuilder recreateAuthorizationServiceBuilder = new StringBuilder("private void recreateAuthorizationService() {\n" +
                "        if (mAuthService != null) {\n" +
                "            Log.i(TAG, \"Discarding existing AuthService instance\");\n" +
                "            mAuthService.dispose();\n" +
                "        }\n" +
                "        mAuthService = createAuthorizationService();\n" +
                "        mAuthRequest.set(null);\n" +
                "        mAuthIntent.set(null);\n" +
                "    }");
        StringBuilder warmUpBrowserBuilder = new StringBuilder("private void warmUpBrowser() {\n" +
                "        mAuthIntentLatch = new CountDownLatch(1);\n" +
                "        Log.i(TAG, \"Warming up browser instance for auth request\");\n" +
                "        CustomTabsIntent.Builder intentBuilder =\n" +
                "                mAuthService.createCustomTabsIntentBuilder(mAuthRequest.get().toUri());\n" +
                "        mAuthIntent.set(intentBuilder.build());\n" +
                "        mAuthIntentLatch.countDown();\n" +
                "    }");
        StringBuilder initializeAuthReqBuilder = new StringBuilder("private void initializeAuthRequest() {\n" +
                "        createAuthRequest();\n" +
                "        warmUpBrowser();\n" +
                "    }");
        StringBuilder initilizeClient = new StringBuilder("private void initializeClient() {\n" +
                "        if (mConfiguration.getClientId() != null) {\n" +
                "            Log.i(TAG, \"Using static client ID: \" + mConfiguration.getClientId());\n" +
                "            // use a statically configured client ID\n" +
                "            mClientId.set(mConfiguration.getClientId());\n" +
                "            initializeAuthRequest();\n" +
                "        }\n" +
                "    }");
        StringBuilder handelConfigResult = new StringBuilder("private void handleConfigurationRetrievalResult(\n" +
                "            AuthorizationServiceConfiguration config,\n" +
                "            AuthorizationException ex) {\n" +
                "        if (config == null) {\n" +
                "            Log.i(TAG, \"Failed to retrieve discovery document\", ex);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        Log.i(TAG, \"Discovery document retrieved\");\n" +
                "        mAuthStateManager.replace(new AuthState(config));\n" +
                "        initializeClient();\n" +
                "    }");
        StringBuilder initialAppAuth = new StringBuilder("private void initializeAppAuth() {\n" +
                "        Log.i(TAG, \"Initializing AppAuth\");\n" +
                "        recreateAuthorizationService();\n" +
                "\n" +
                "        if (mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration() != null) {\n" +
                "            // configuration is already created, skip to client initialization\n" +
                "            Log.i(TAG, \"auth config already established\");\n" +
                "            initializeClient();\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        // if we are not using discovery, build the authorization service configuration directly\n" +
                "        // from the static configuration values.\n" +
                "        if (mConfiguration.getDiscoveryUri() == null) {\n" +
                "            Log.i(TAG, \"Creating auth config from res/raw/auth_config.json\");\n" +
                "            AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(\n" +
                "                    mConfiguration.getAuthEndpointUri(),\n" +
                "                    mConfiguration.getTokenEndpointUri(),\n" +
                "                    mConfiguration.getRegistrationEndpointUri());\n" +
                "\n" +
                "            mAuthStateManager.replace(new AuthState(config));\n" +
                "            initializeClient();\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        // WrongThread inference is incorrect for lambdas\n" +
                "        // noinspection WrongThread\n" +
                "        Log.i(TAG, \"Retrieving OpenID discovery doc\");\n" +
                "        AuthorizationServiceConfiguration.fetchFromUrl(\n" +
                "                mConfiguration.getDiscoveryUri(),\n" +
                "                this::handleConfigurationRetrievalResult);\n" +
                "    }");
        StringBuilder doAuth = new StringBuilder("private void doAuth() {\n" +
                "        try {\n" +
                "            mAuthIntentLatch.await();\n" +
                "        } catch (InterruptedException ex) {\n" +
                "            Log.w(TAG, \"Interrupted while waiting for auth intent\");\n" +
                "        }\n" +
                "\n" +
                "            Intent intent = mAuthService.getAuthorizationRequestIntent(\n" +
                "                    mAuthRequest.get(),\n" +
                "                    mAuthIntent.get());\n" +
                "            startActivityForResult(intent, RC_AUTH);\n" +
                "    }");
        StringBuilder startAuth = new StringBuilder("void startAuth() {\n" +
                "\n" +
                "        doAuth();\n" +
                "    }");
        StringBuilder onActivityResult = new StringBuilder("@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {\n" +
                "\n" +
                "        Intent intent = new Intent(this, TokenActivity.class);\n" +
                "        intent.putExtras(data.getExtras());\n" +
                "        startActivity(intent);\n" +
                "    }");
        StringBuilder additionalParams = new StringBuilder("private HashMap<String,String> additionalParams() {\n" +
                "\n" +
                "        String nonceVal;\n" +
                "        nonceVal =generateRandomState();\n"+
                "        HashMap <String, String> addParam = new HashMap<>();\n" +
                "        addParam.put(\"acr_values\"," + "\""+ loaPar+"\""+");\n" +
                "        addParam.put(\"nonce\"," + "\"nonceVal\"" +");\n" +
                "        return addParam;\n" +
                "    }");
        StringBuilder genRandom = new StringBuilder("private static String generateRandomState() {\n" +
                "        final int STATE_LENGTH = 16; \n" +
                "        SecureRandom sr = new SecureRandom();\n" +
                "        byte[] random = new byte[STATE_LENGTH];\n" +
                "        sr.nextBytes(random);\n" +
                "        return Base64.encodeToString(random, Base64.NO_WRAP | Base64.NO_PADDING | Base64.URL_SAFE);\n" +
                "    }");

        StringBuilder tag = new StringBuilder("private static final String TAG = \"LoginActivity\";\n");
        StringBuilder RcAuth = new StringBuilder("private static final int RC_AUTH = 100;\n");
        StringBuilder Var = new StringBuilder("private AuthorizationService mAuthService;\n");
        StringBuilder Var1 = new StringBuilder("private AuthStateManager mAuthStateManager;\n");
        StringBuilder Var2 = new StringBuilder("private Configuration mConfiguration;\n");
        StringBuilder Var3 = new StringBuilder("private final AtomicReference<String> mClientId = new AtomicReference<>();\n");
        StringBuilder Var4 = new StringBuilder("private final AtomicReference<AuthorizationRequest> mAuthRequest = new AtomicReference<>();\n");
        StringBuilder Var5 = new StringBuilder("private final AtomicReference<CustomTabsIntent> mAuthIntent = new AtomicReference<>();\n");
        StringBuilder Var6 = new StringBuilder("private CountDownLatch mAuthIntentLatch = new CountDownLatch(1);\n");
        StringBuilder Var7 = new StringBuilder("@NonNull\n" +
                "    private BrowserMatcher mBrowserMatcher = AnyBrowserMatcher.INSTANCE;\n");
        StringBuilder Var8 = new StringBuilder("Button " + GenerateClassiGov.getInstance().getBtnName() + ";");
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiMethod createAuthRequest= elementFactory.createMethodFromText(createAuthRequestBuilder.toString(),psiClass);
        PsiMethod AuthorizationService= elementFactory.createMethodFromText(AuthorizationServiceBuilder.toString(),psiClass);
        PsiMethod recreateAuthorizationService= elementFactory.createMethodFromText(recreateAuthorizationServiceBuilder.toString(),psiClass);
        PsiMethod warmUpBrowser= elementFactory.createMethodFromText(warmUpBrowserBuilder.toString(),psiClass);
        PsiMethod initializeAuth= elementFactory.createMethodFromText(initializeAuthReqBuilder.toString(),psiClass);
        PsiMethod initializeClient= elementFactory.createMethodFromText(initilizeClient.toString(),psiClass);
        PsiMethod handleConfig= elementFactory.createMethodFromText(handelConfigResult.toString(),psiClass);
        PsiMethod initialappAuth= elementFactory.createMethodFromText(initialAppAuth.toString(),psiClass);
        PsiMethod doauth= elementFactory.createMethodFromText(doAuth.toString(),psiClass);
        PsiMethod startauth= elementFactory.createMethodFromText(startAuth.toString(),psiClass);
        PsiMethod onactivityResult= elementFactory.createMethodFromText(onActivityResult.toString(),psiClass);
        PsiMethod additionalparams= elementFactory.createMethodFromText(additionalParams.toString(),psiClass);
        PsiMethod genrandom= elementFactory.createMethodFromText(genRandom.toString(),psiClass);
        PsiStatement psiTag = elementFactory.createStatementFromText(tag.toString(),psiClass);
        PsiStatement psiRcAuth = elementFactory.createStatementFromText(RcAuth.toString(),psiClass);
        PsiStatement var = elementFactory.createStatementFromText(Var.toString(),psiClass);
        PsiStatement var1 = elementFactory.createStatementFromText(Var1.toString(),psiClass);
        PsiStatement var2 = elementFactory.createStatementFromText(Var2.toString(),psiClass);
        PsiStatement var3 = elementFactory.createStatementFromText(Var3.toString(),psiClass);
        PsiStatement var4 = elementFactory.createStatementFromText(Var4.toString(),psiClass);
        PsiStatement var5 = elementFactory.createStatementFromText(Var5.toString(),psiClass);
        PsiStatement var6 = elementFactory.createStatementFromText(Var6.toString(),psiClass);
        PsiStatement var7 = elementFactory.createStatementFromText(Var7.toString(),psiClass);
        PsiStatement var8 = elementFactory.createStatementFromText(Var8.toString(),psiClass);
        PsiElement createAuth = psiClass.add(createAuthRequest);
        PsiElement authService = psiClass.add(AuthorizationService);
        PsiElement recreateAuth = psiClass.add(recreateAuthorizationService);
        PsiElement warmBrowser = psiClass.add(warmUpBrowser);
        PsiElement initialAuth = psiClass.add(initializeAuth);
        PsiElement initialClnt = psiClass.add(initializeClient);
        PsiElement handleConf = psiClass.add(handleConfig);
        PsiElement initappAuth = psiClass.add(initialappAuth);
        PsiElement doauthReq = psiClass.add(doauth);
        PsiElement strtauth = psiClass.add(startauth);
        PsiElement onActivityRes = psiClass.add(onactivityResult);
        PsiElement addparam = psiClass.add(additionalparams);
        PsiElement genrand = psiClass.add(genrandom);
        PsiElement tage = psiClass.addBefore(psiTag,createAuth);
        PsiElement rcauth = psiClass.addAfter(psiRcAuth,tage);
        PsiElement varr = psiClass.addAfter(var,rcauth);
        PsiElement varr1 = psiClass.addAfter(var1,varr);
        PsiElement varr2 = psiClass.addAfter(var2,varr1);
        PsiElement varr3 = psiClass.addAfter(var3,varr2);
        PsiElement varr4 = psiClass.addAfter(var4,varr3);
        PsiElement varr5 = psiClass.addAfter(var5,varr4);
        PsiElement varr6 = psiClass.addAfter(var6,varr5);
        PsiElement varr7 = psiClass.addAfter(var7,varr6);
        PsiElement varr8 = psiClass.addAfter(var8,varr7);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(createAuth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(authService);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(recreateAuth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(warmBrowser);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(initialAuth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(initialClnt);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(handleConf);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(initappAuth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(doauthReq);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(strtauth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(onActivityRes);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(genrand);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(addparam);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(tage);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(rcauth);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr1);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr2);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr3);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr4);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr5);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr6);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr7);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(varr8);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiClass psiclass = getPsiClassFromContext(e);
        PsiFile psiFile = psiclass.getContainingFile();
        psiDirectory = psiFile.getContainingDirectory();
        e.getPresentation().setEnabled(psiclass != null);
    }

    private PsiClass getPsiClassFromContext(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiclass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        return psiclass;
    }
}
