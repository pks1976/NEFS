<table class="data-table">
    <tr>
        <th>Document</th>
        <th>Purpose</th>
    </tr>
    <tr>
        <td>
            Getting Started with NEFS (<a href="${resourcesPath}/support/docs/nef-articles/getting-started.html" target="nefs-getting-started" title="Online (HTML) Version of doc">HTML</a>)
            (<a href="${resourcesPath}/nefs-getting-started.pdf" target="nefs-getting-started" title="PDF Version of doc">PDF</a>)
        </td>
        <td>Provides instructions for how to evaluate the Netspective Enterprise Frameworks Suite (<i>opens in a new window</i>).</td>
    </tr>
    <tr>
        <td><a href="${resourcesPath}/support/docs/nef-manual/index.html" target="nefs-um">NEFS User's Manual</a></td>
        <td>Provides instructions for how to use the Netspective Enterprise Frameworks Suite (<i>opens in a new window</i>).</td>
    </tr>
    <tr>
        <td><a href="documentation/upgrading">NEFS Upgrade Guide</a></td>
        <td>Provides instructions for how to upgrade one or more of our Java Frameworks.</td>
    </tr>
    <tr>
        <td><a href="documentation/change-log">NEFS Changes Log</a></td>
        <td>Provides a history of the changes to NEFS since 7.0 was released.</td>
    </tr>

    <tr>
        <td><a href="${resourcesPath}/NEFS_Sales_Tech_Intro.ppt">NEFS Introduction Presentation</a></td>
        <td>A PowerPoint presentation that introduces the main features and functions of the NEFS.</td>
    </tr>

    <#list sampleApps as app>
    <#if app.tutorialUrl?exists>
    <tr>
        <td>
            ${app.tutorialName} (<a href="${app.tutorialUrl}" title="Online (HTML) Version of Tutorial">HTML</a>)
            <#if app.tutorialPDF?exists>
            (<a href="${app.tutorialPDF}" title="PDF Version of Tutorial">PDF</a>)
            </#if>
        </td>
        <td>${app.tutorialDescr}</td>
    </tr>
    </#if>
    </#list>

    <tr>
        <td><a href="http://www.netspective.com/old-devel/">Old developer.netspective.com site</a></td>
        <td>In case you need access to the old site, the contents remain available.</td>
    </tr>
</table>
