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
package uk.co.malleusconsulting.magnolia.ui.form.field.factory;

import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.AppController;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;
import info.magnolia.ui.form.field.factory.LinkFieldFactory;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.malleusconsulting.magnolia.ui.form.field.definition.DynamicLinkFieldDefinition;

/**
 * Constructs a Magnolia link field able to dynamically adjust the selection of
 * link targets available to an editor.
 *
 * @param <D>
 *            Field definition type.
 */
public class DynamicLinkFieldFactory<D extends FieldDefinition> extends
		LinkFieldFactory<LinkFieldDefinition> {

	private static final Logger LOG = LoggerFactory
			.getLogger(DynamicLinkFieldFactory.class);

	@Inject
	public DynamicLinkFieldFactory(DynamicLinkFieldDefinition definition,
			JcrNodeAdapter relatedFieldItem, AppController appController,
			UiContext uiContext, ComponentProvider componentProvider) {
		super(definition, relatedFieldItem, appController, uiContext,
				componentProvider);
		super.getFieldDefinition().setTargetTreeRootPath(
				findTargetTreeRootPath(relatedFieldItem,
						definition.getTargetRootNodeType(),
						definition.isIncludeSelf()));
	}

	/**
	 * Examines a given node to find the closest ancestor of the given type. If
	 * unable to find the ancestor, returns null. In the event of a repository
	 * failure, falls back to the specified value in the field definition.
	 * 
	 * @param node
	 *            The node being edited
	 * @param targetNodeType
	 *            The type of node to look up the tree for
	 * @param includeSelf
	 *            Is the node being edited a valid root if it matches the
	 *            template type?
	 * @return
	 */
	private String findTargetTreeRootPath(JcrNodeAdapter relatedFieldItem,
			String targetNodeType, boolean includeSelf) {

		try {
			if (includeSelf
					&& NodeUtil.isNodeType(relatedFieldItem.getJcrItem(),
							targetNodeType)) {
				return relatedFieldItem.getJcrItem().getPath();
			}

			Node rootNode = NodeUtil.getNearestAncestorOfType(
					relatedFieldItem.getJcrItem(), targetNodeType);
			return rootNode == null ? null : rootNode.getPath();
		} catch (RepositoryException e) {
			LOG.error("Failed to read path of node being edited", e);
			return super.getFieldDefinition().getTargetTreeRootPath();
		}
	}
}
