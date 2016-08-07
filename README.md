# dynamic-link-field-module
##Description
This modules adds an additional link field that can dynamically discover the nodes it makes available to an editor to select from.

The primary use case for this field is where an editor is configuring a page that should only link to content from a certain area of the website. For example, a category landing page which highlights items beneath it in the website tree.

##Usage
The field type is `uk.co.malleusconsulting.magnolia.ui.form.field.definition.DynamicLinkFieldDefinition` and introduces an additional property, `targetRootNodeType`.

On initialisation, triggered by opening a dialog containing the new field, the ancestors of the node being edited are examined to find the closest of the given type. The path of this node is then provided to a standard Magnolia LinkFieldFactory. This, in turn, produces a link field which provides a tree from that ancestor down.

Where no node type is specified, the field defaults to `mgnl:page`, meeting the primary use case described above.

```xml
<sv:node sv:name="workbench">
  <sv:property sv:name="class" sv:type="String">
    <sv:value>uk.co.malleusconsulting.magnolia.apps.extended.workbench.ExtendedWorkbench</sv:value>
  </sv:property>
  <sv:property sv:name="dropConstraintClass" sv:type="String">
    <sv:value>uk.co.malleusconsulting.magnolia.apps.extended.workbench.tree.drop.MaximumDepthDropConstraint</sv:value>
  </sv:property>
```