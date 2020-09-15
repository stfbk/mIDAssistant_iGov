
# mIDAssistant_iGov

**mIDAssistant_iGov** is an Android Studio plugin that guides native mobile app developers with secure integeration of OpneID Connect iGov profile ([OpenID Connect iGov](https://openid.net/specs/openid-igov-openid-connect-1_0.html)) solutions within their apps. 

## Features

**mIDAssistant_iGov** provides a wizard-based approach that guides developers to integrate single iGov Identity Management provider (iGov-IdMP) within their native apps. The mIDAssistant_iGov Plugin aims to support native app developers for integration of iGov-IdMPs which are fully-compliant with the both best current practices for native apps [RFC 8252](https://tools.ietf.org/html/rfc8252) and the OpenID Connect iGov draft [OpenID Connect iGov](https://openid.net/specs/openid-igov-openid-connect-1_0.html). The current version of mIDAssistant_iGov is able to:

  - Enforce the usage of best current practices (BCP) for native apps set out in
    [RFC 8252 - OAuth 2.0 for Native Apps](https://tools.ietf.org/html/rfc8252) with thanks to the integration of [AppAuth](https://appauth.io).
  - Avoid the need to download the iGov-IdMP SDK and understand its online documentations (a list of known
    IdMPs with their configuration information is embedded within our tool).
  - Automatically integrating the secure customized code to enable the communication with the different iGov-IdMPs.
  - Support ArubaID, InfocertID, IntesaID, Lepida, LoginGov, NamirialID, PosteID, SielteID, SpidIT, and TimID as OpenID Connect iGov-IdMPs. 

## Requirements

mIDAssistant_iGov demands the following requirments:

- It supports Android API 16 (Jellybean) and above (AppAuth SDK requirement).
- HTTPS App Link redirection for Android API 23 and above.

## Download

You can install mIDAssistant_iGov by downloading the jar file from the Github repository.

##mIDAssistant_iGov Installation

In order to install mIDAssistant_iGov within the Android Studio environment, developers should perform the following steps:

- In the Android Studio IDE, click on the android studio menu tab → preferences → Plugins 
- Select install plugin from disk → locate the mIDAssistant_iGov jar file → select → apply
- Restart the Android Studio IDE

## Setup Phase

Developers must perform the following steps: 

- Create an activity for the iGov Login in the case that developers start the integeration of iGov-IdMP within their apps from the scratch, or they just need to navigate into their Login activity that they want to add the Login button. 
- Create the Layout files related to the Login activity.
- Create "raw" folder within the following path ("src/app/main/res/raw").
- Developers installed the plugin within Android Studio (This step will happen just once).


## Usage

Developers should perform the steps in the setup phase beforehand. Then, developers should navigate into the targeted activity (the activity they are going to add the Login Button) and within the ``onCreate()`` method. Developers can access the plugin GUI by clicking on the mIDAssistant_iGov tab and select iGov Profile. After that, developers are shown with a GUI that contains a list of OpenID Connect iGov-IdMPs and configuration questions. 

Based on the IdMP selection, varied questions will populate within the GUI. Developers should provide the necessary infromation to provide the AppAuth customize code and automatically integrate the secure code within developers app. Indeed, developers are asking to provide the following configuration information:

- Scopes: the authorization scopes that should be added within the Access Token.
- Level of Assurance: enables the IdMPs to request strong authentication methods.
- Client ID: the application specific identifier that developers will obtain after the registration of the app in the IdMPs developer dashboard. 
- Button Name: It is the button name that declared by the developer in the correlated activity layout file. 
- Redirection method: Developers can select the prefered method from the available IdMPs supported redirection methods.
- Valid Domain URL: In the case of the HTTPS redirection scheme, developers must provide scheme, host, and path value.  
- Custom URL: In case of the Custom URL redirection scheme, developers must provide the Custom URL. 

A demo video providing an overview of using the mIDAssistant_iGov plugin for integration of OpenID Connect iGov provider can be accessed [here](https://).

## Limitations 

The current prototype of the mIDAssistant_iGov has the following limitations:

- Developers must create a folder at a specific location ("src/app/main/res/raw").
- The plugin supports the integration of single iGov-IdMP and the integration of multiple IdMPs is a work in progress.
- The plugin supports only the iGov profile following static client registration, while the iGov profile following dyanmic client registration and the iGov profile for SPID are the works in progress.

## License
Copyright 2019-2020, Fondazione Bruno Kessler

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Developed within [Security & Trust](https://st.fbk.eu) Research Unit at [Fondazione Bruno Kessler](https://www.fbk.eu/en/) (Italy)
