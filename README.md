# dynamic-link-field-module
##Description
This modules adds an additional link field that can dynamically discover the nodes it makes available to an editor to select from.

The primary use case for this field is where an editor is configuring a page that should only link to content from a certain area of the website. For example, a category landing page which highlights items beneath it in the website tree.

##Installation
This module is available from [the central Maven repository](http://repo1.maven.org/maven2/uk/co/malleusconsulting/magnolia/ui/form/dynamic-link-field-module/1.0.0/) and can be installed using:

```xml
<dependency>
  <groupId>uk.co.malleusconsulting.magnolia.ui.form</groupId>
  <artifactId>dynamic-link-field-module</artifactId>
  <version>1.0.0</version>
</dependency>
```

##Usage
The field type is `uk.co.malleusconsulting.magnolia.ui.form.field.definition.DynamicLinkFieldDefinition` and introduces an additional property, `targetRootNodeType`.

On initialisation, triggered by opening a dialog containing the new field, the ancestors of the node being edited are examined to find the closest of the given type. The path of this node is then provided to a standard Magnolia LinkFieldFactory. This, in turn, produces a link field which provides a tree from that ancestor down.

Where no node type is specified, the field defaults to `mgnl:page`, meeting the primary use case described above.

An example of an STK teaser dialog reconfigured to use the DynamicLinkField is shown below.

<img src="https://raw.githubusercontent.com/malleusconsulting/dynamic-link-field/gh_pages/stk_teaser_dialog_with_dynamiclinkfield.png" width="829" height="310" title="Dialog using a dynamic link field" />
