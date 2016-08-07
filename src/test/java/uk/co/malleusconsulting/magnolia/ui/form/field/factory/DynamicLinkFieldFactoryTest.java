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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.test.mock.jcr.MockNode;
import info.magnolia.ui.api.app.AppController;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;
import info.magnolia.ui.form.field.factory.AbstractFieldFactoryTestCase;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.junit.Before;
import org.junit.Test;

import uk.co.malleusconsulting.magnolia.ui.form.field.definition.DynamicLinkFieldDefinition;

public class DynamicLinkFieldFactoryTest extends
		AbstractFieldFactoryTestCase<LinkFieldDefinition> {

	private JcrNodeAdapter relatedFieldItem = mock(JcrNodeAdapter.class);
	private AppController appController = mock(AppController.class);
	private UiContext uiContext = mock(UiContext.class);

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void shouldResetDefinitonToUsePathOfParentPage()
			throws ItemExistsException, PathNotFoundException,
			NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException, RepositoryException {
		/**
		 * Given that the node being edited by the field is a component at
		 * /home/subPage/area/component and the desired type for the root is
		 * mgnl:page
		 */
		Node homePage = new MockNode("home", NodeTypes.Page.NAME);
		Node subPage = homePage.addNode("subPage", NodeTypes.Page.NAME);
		Node subPageArea = subPage.addNode("area", NodeTypes.Area.NAME);
		Node subPageComponent = subPageArea.addNode("component",
				NodeTypes.Component.NAME);
		when(relatedFieldItem.getJcrItem()).thenReturn(subPageComponent);

		DynamicLinkFieldDefinition definition = new DynamicLinkFieldDefinition();
		definition.setTargetRootNodeType(NodeTypes.Page.NAME);

		/**
		 * When the factory is configured
		 */
		DynamicLinkFieldFactory<DynamicLinkFieldDefinition> testFactory = new DynamicLinkFieldFactory<DynamicLinkFieldDefinition>(
				definition, relatedFieldItem, appController, uiContext,
				componentProvider);

		/**
		 * It should set the root path to be /home/subPage
		 */
		assertThat(testFactory.getFieldDefinition().getTargetTreeRootPath(),
				is("/home/subPage"));
	}

	@Test
	public void shouldDefaultToANullRootPathWhenNoSuitableRootNodeIsFound()
			throws ItemExistsException, PathNotFoundException,
			NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException, RepositoryException {
		/**
		 * Given that the desired type for the root is "test:test" and no such
		 * node is present as an ancestor of the one being edited
		 */
		Node rootNode = new MockNode();
		Node homePage = rootNode.addNode("home", NodeTypes.Page.NAME);
		Node homePageArea = homePage.addNode("area", NodeTypes.Area.NAME);
		Node homePageComponent = homePageArea.addNode("component",
				NodeTypes.Component.NAME);
		when(relatedFieldItem.getJcrItem()).thenReturn(homePageComponent);

		DynamicLinkFieldDefinition definition = new DynamicLinkFieldDefinition();
		definition.setTargetRootNodeType("test:test");

		/**
		 * When the factory is configured
		 */
		DynamicLinkFieldFactory<DynamicLinkFieldDefinition> testFactory = new DynamicLinkFieldFactory<DynamicLinkFieldDefinition>(
				definition, relatedFieldItem, appController, uiContext,
				componentProvider);

		/**
		 * It should set the root path to be null
		 */
		assertThat(testFactory.getFieldDefinition().getTargetTreeRootPath(),
				is(nullValue()));
	}

	@Test
	public void factoryHandlesRepositoryExceptionsWhenWalkingTheNodesTree()
			throws RepositoryException {
		/**
		 * Given that the repository will throw an exception on examining the
		 * node being edited and the definition has a desired default root of
		 * /knownDefault.
		 */
		Node subPageComponent = mock(Node.class);
		when(subPageComponent.getDepth()).thenThrow(new RepositoryException());
		when(relatedFieldItem.getJcrItem()).thenReturn(subPageComponent);

		DynamicLinkFieldDefinition definition = new DynamicLinkFieldDefinition();
		definition.setTargetRootNodeType(NodeTypes.Page.NAME);
		definition.setTargetTreeRootPath("/knownDefault");

		/**
		 * When the factory is configured
		 */
		DynamicLinkFieldFactory<DynamicLinkFieldDefinition> testFactory = new DynamicLinkFieldFactory<DynamicLinkFieldDefinition>(
				definition, relatedFieldItem, appController, uiContext,
				componentProvider);

		/**
		 * It should set the root path to /knownDefault
		 */
		assertThat(testFactory.getFieldDefinition().getTargetTreeRootPath(),
				is("/knownDefault"));
	}

	@Override
	protected void createConfiguredFieldDefinition() {
		// TODO Auto-generated method stub
	}
}
