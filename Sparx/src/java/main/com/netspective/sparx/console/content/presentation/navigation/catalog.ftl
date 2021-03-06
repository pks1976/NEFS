<#include "/content/library.ftl"/>

<#assign doingFrameworkDeveploment = vc.runtimeEnvironmentFlags.flagIsSet(statics["com.netspective.commons.RuntimeEnvironmentFlags"].FRAMEWORK_DEVELOPMENT)/>
<#assign trees = vc.project.navigationTrees/>
<#assign catalog = []/>
<#list trees.trees.keySet().iterator() as treeName>
    <#if ! (treeName == 'console' && ! doingFrameworkDeveploment)>
        <#assign tree=trees.getNavigationTree(treeName)/>
        <#if tree.isDefaultTree()>
            <#assign isDefault='Yes'>
        <#else>
            <#assign isDefault='&nbsp;'>
        </#if>
        <#assign catalog = catalog + [[ "<img src='${vc.activeTheme.getResourceUrl('/images/navigation/tree.gif')}'/>", "<a href='tree/${tree.name}'>${tree.name}</a>", tree.size(), getClassReference(tree.class.name), isDefault ]]/>
    </#if>
</#list>

<@panel heading="All Available Navigation Trees">
    <@reportTable headings=["&nbsp;", "Tree", "Pages", "Class", "Default"] data=catalog columnAttrs=["", "", "align=right", "", ""]/>
</@panel>
