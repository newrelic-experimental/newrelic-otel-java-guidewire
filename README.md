<a href="https://opensource.newrelic.com/oss-category/#new-relic-experimental"><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/dark/Experimental.png"><source media="(prefers-color-scheme: light)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"><img alt="New Relic Open Source experimental project banner." src="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"></picture></a>

![GitHub forks](https://img.shields.io/github/forks/newrelic-experimental/newrelic-otel-java-guidewire?style=social)
![GitHub stars](https://img.shields.io/github/stars/newrelic-experimental/newrelic-otel-java-guidewire?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/newrelic-experimental/newrelic-otel-java-guidewire?style=social)

![GitHub all releases](https://img.shields.io/github/downloads/newrelic-experimentalnewrelic-otel-java-guidewire/total)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/newrelic-experimental/newrelic-otel-java-guidewire)
![GitHub last commit](https://img.shields.io/github/last-commit/newrelic-experimental/newrelic-otel-java-guidewire)
![GitHub Release Date](https://img.shields.io/github/release-date/newrelic-experimental/newrelic-otel-java-guidewire)


![GitHub issues](https://img.shields.io/github/issues/newrelic-experimental/newrelic-otel-java-guidewire)
![GitHub issues closed](https://img.shields.io/github/issues-closed/newrelic-experimental/newrelic-otel-java-guidewire)
![GitHub pull requests](https://img.shields.io/github/issues-pr/newrelic-experimental/newrelic-otel-java-guidewire)
![GitHub pull requests closed](https://img.shields.io/github/issues-pr-closed/newrelic-experimental/newrelic-otel-java-guidewire)

# Otel Java Instrumentation for Guidewire ClaimCenter and PolicyCenter

Instrumentation for Guidewire applications, including ClaimCenter and PolicyCenter.


## Installation

1. Obtain the latest release from this repository.
2. Extract the release into a local directory.
3. Transfer the extension JAR file to the target server.
4. Set the following environment variables:

   ```bash
   export OTEL_EXPORTER_OTLP_ENDPOINT=https://otlp.nr-data.net:4317
   export OTEL_EXPORTER_OTLP_HEADERS="api-key=<newrelic_ingest_key>"
   export OTEL_RESOURCE_ATTRIBUTES="service.name=<appname>"
   ```

5. Run your application with the following Java agent attributes:

   ```bash
   -javaagent:<path_to_OpenTelemetry_Agent>/opentelemetry-javaagent.jar \
   -Dotel.javaagent.extensions=<path_to_guidewire_extension>/otel-guidewire-instrumentation-v3-1.1.jar \
   -Dotel.javaagent.debug=true
   ```

6. Restart your Java application.
7. After the application has reloaded, generate traffic against your app to trigger the distributed traces you expect to see.
8. To stop debugging, set:

   ```bash
   -Dotel.javaagent.debug=false
   ```


## Usage

Once installed, this extension will perform the following functions:

### Span Renaming

Spans are named based on the names or values of POST parameters as follows:

* eventSource
* eventParam - if eventSource is _refresh_ and eventParam is not null
* Wizard:ScreenName if eventSource contains Wizard:Next_act

### Custom Parameters

These parameters are added to the Span to which they apply:

* JSESSIONID* cookie value
* ThreadName
* eventSource
* eventParam
* Wizard (conditional)
* Screen (conditional)
* SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:ClaimNumber
* SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:PolicyNumber
* SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:FirstName
* SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:LastName
* SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:CompanyName
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:ClaimNumber
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:PolicyNumber
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:FirstName
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:LastName
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:AssignedToGroup
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:AssignedToUser
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CreatedBy
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CatNumber
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:VinNumber
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:lossStateActiveSearch
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimStatus
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:nwPolicyType
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:LossType
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchRangeValue
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchStartDate
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchEndDate
* ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_act
* ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:CreateDocument
* ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:CreateDocument_act
* ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen:NewTemplateDocumentDV:ViewLink_link
* Login:LoginScreen:LoginDV:username
* NewSubmission:NewSubmissionScreen:ProductSettingsDV:DefaultBaseState

## Support

New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR DEDICATED SUPPORT. Issues and contributions should be reported to the project here on GitHub. We encourage you to bring your experiences and questions to the [Explorers Hub](https://discuss.newrelic.com) where our community members collaborate on solutions and new ideas.

## Contributing

We encourage your contributions to improve newrelic-otel-java-guidewire ! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project.

If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company,  please drop us an email at opensource@newrelic.com.

**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).

## License
newrelic-otel-java-guidewire is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.
>[If applicable: The newrelic-java-guidewire also uses source code from third-party libraries. You can find full details on which libraries are used and the terms under which they are licensed in the third-party notices document.]
