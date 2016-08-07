/**
 * This file Copyright 2016 Malleus Consulting Ltd.
 * All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.malleusconsulting.magnolia.ui.form.field.definition;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;

/**
 * Extends the provided link field to allow selection from a dynamically selected area of related nodes.
 * For example, all child nodes to ensure a landing page only links to relevant content.
 */
public class DynamicLinkFieldDefinition extends LinkFieldDefinition {

	/**
	 * A predefined default of mgnl:page for the example use case above.
	 */
	private String targetRootNodeType =  NodeTypes.Page.NAME;

	public String getTargetRootNodeType() {
		return targetRootNodeType;
	}

	public void setTargetRootNodeType(String targetRootNodeType) {
		this.targetRootNodeType = targetRootNodeType;
	}
}
