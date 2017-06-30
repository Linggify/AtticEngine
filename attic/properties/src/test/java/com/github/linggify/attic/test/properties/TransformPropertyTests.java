package com.github.linggify.attic.test.properties;

import org.junit.Test;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.logic.Entity;
import com.github.linggify.attic.logic.IProperty.PropertyEvent;
import com.github.linggify.attic.logic.IProperty.PropertyListener;
import com.github.linggify.attic.properties.TransformProperty;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link TransformProperty}
 * 
 * @author Fredie
 *
 */
public class TransformPropertyTests {

	/**
	 * Tests {@link TransformProperty#TransformProperty()}
	 */
	@Test
	public void testConstructor() {
		TransformProperty property = new TransformProperty();
		assertEquals(new Matrix33(), property.get());
	}

	/**
	 * Tests {@link TransformProperty#setPosition(float, float)}
	 */
	@Test
	public void testSetPositionParameter() {
		TransformProperty property = new TransformProperty();
		property.setPosition(2, -1);
		assertEquals(new Matrix33().transform(2, -1), property.get());
	}

	/**
	 * Tests
	 * {@link TransformProperty#setPosition(Vector2D)}
	 */
	@Test
	public void testSetPositionVector() {
		TransformProperty property = new TransformProperty();
		property.setPosition(new Vector2D(2, -1));
		assertEquals(new Matrix33().transform(2, -1), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#move(float, float)}
	 */
	@Test
	public void testMoveParameter() {
		TransformProperty property = new TransformProperty();
		property.move(2, -1);
		assertEquals(new Matrix33().transform(2, -1), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#move(Vector2D)}
	 */
	@Test
	public void testMoveVector() {
		TransformProperty property = new TransformProperty();
		property.move(new Vector2D(2, -1));
		assertEquals(new Matrix33().transform(2, -1), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#setRotation(float)}
	 */
	@Test
	public void testSetRotation() {
		TransformProperty property = new TransformProperty();
		property.setRotation(90);
		assertEquals(new Matrix33().rotate(90), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#rotate(float)}
	 */
	@Test
	public void testRotate() {
		TransformProperty property = new TransformProperty();
		property.rotate(90);
		assertEquals(new Matrix33().rotate(90), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#setScale(float)}
	 */
	@Test
	public void testSetScale() {
		TransformProperty property = new TransformProperty();
		property.setScale(2);
		assertEquals(new Matrix33().scale(2), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#setScale(float)}
	 */
	@Test
	public void testScale() {
		TransformProperty property = new TransformProperty();
		property.scale(2);
		assertEquals(new Matrix33().scale(2), property.get());
	}
	
	/**
	 * Tests {@link TransformProperty#setParentTransform(TransformProperty)}
	 */
	@Test
	public void testSetParentTransform() {
		TransformProperty parent = new TransformProperty();
		parent.setPosition(2, 0);
		parent.setRotation(90);
		
		TransformProperty child = new TransformProperty();
		child.setPosition(1, 0);
		
		child.setParentTransform(parent);
		assertEquals(new Matrix33().transform(1, 0).rotate(90).transform(2, 0), child.get());
		
		child.setParentTransform(null);
		assertEquals(new Matrix33().transform(1, 0), child.get());
	}
	
	/**
	 * Tests {@link TransformProperty#setParentTransform(TransformProperty)}
	 * setting itself as its parent (expecting an exception)
	 */
	@Test (expected = AtticRuntimeException.class)
	public void testSetParentTransformException() {
		TransformProperty property = new TransformProperty();
		property.setParentTransform(property);
	}
	
	/**
	 * Tests {@link TransformProperty#getContentType()}
	 */
	@Test
	public void testGetContentType() {
		assertEquals(Matrix33.class, new TransformProperty().getContentType());
	}
	
	/**
	 * Tests {@link TransformProperty#addListener(PropertyListener)}
	 */
	@Test
	public void testAddListener() {
		PropertyListener listener = mock(PropertyListener.class);
		
		TransformProperty property = new TransformProperty();
		property.addListener(listener);
		
		verify(listener).onEvent(property, PropertyEvent.PROPERTY_CHANGED);
	}
	
	/**
	 * Tests {@link TransformProperty#addListener(PropertyListener)}
	 * with null as the listener
	 */
	@Test (expected = AtticRuntimeException.class)
	public void testAddListenerException() {
		TransformProperty property = new TransformProperty();
		property.addListener(null);
	}
	
	/**
	 * Tests {@link TransformProperty#onAttach(Entity)}
	 */
	@Test
	public void testOnAttach() {
		PropertyListener listener = mock(PropertyListener.class);
		TransformProperty property = new TransformProperty();
		property.addListener(listener);
		property.onAttach(new Entity(null));
		verify(listener).onEvent(property, PropertyEvent.PROPERTY_ADDED);
	}
	
	/**
	 * Tests {@link TransformProperty#onAttach(Entity)}
	 * on a TransformProperty that already has a parent
	 */
	@Test (expected = AtticRuntimeException.class)
	public void testOnAttachException() {
		TransformProperty property = new TransformProperty();
		property.onAttach(new Entity(null));
		property.onAttach(new Entity(null));
	}
	
	/**
	 * Tests {@link TransformProperty#onDetach()}
	 */
	@Test
	public void testOnDetach() {
		PropertyListener listener = mock(PropertyListener.class);
		TransformProperty property = new TransformProperty();
		property.onAttach(new Entity(null));
		property.addListener(listener);
		property.onDetach();
		
		verify(listener).onEvent(property, PropertyEvent.PROPERTY_REMOVED);
	}
	
	/**
	 * Tests {@link TransformProperty#setActive(boolean)}
	 */
	@Test
	public void testSetActive() {
		PropertyListener listener = mock(PropertyListener.class);
		TransformProperty property = new TransformProperty();
		property.addListener(listener);
		property.setActive(false);
		verify(listener).onEvent(property, PropertyEvent.PROPERTY_DISABLED);
		assertFalse(property.isActive());
		property.setActive(true);
		verify(listener).onEvent(property, PropertyEvent.PROPERTY_ENABLED);
		assertTrue(property.isActive());
	}
}
