package com.github.linggify.attic.tests.util;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.util.Vector2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector2DTests {

	@Test
	public void testConstructor() {
		assertEquals("(0.0, 0.0)", new Vector2D().toString());
		assertEquals("(1.0, 0.0)", new Vector2D(1, 0).toString());
		assertEquals("(0.0, 1.0)", new Vector2D(0, 1).toString());
		assertEquals("(2.0, 3.0)", new Vector2D(2, 3).toString());
		assertEquals("(2.0, 3.0)", new Vector2D(new Vector2D(2, 3)).toString());
		assertEquals("(1.0, 0.0)", new Vector2D(new Vector2D(1, 0)).toString());
		assertEquals("(0.0, 1.0)", new Vector2D(new Vector2D(0, 1)).toString());
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError0() {
		new Vector2D(null);
	}
	
	@Test
	public void testSet() {
		Vector2D vector = new Vector2D();
		assertEquals("(1.0, 0.0)", vector.setX(1).toString());
		assertEquals("(1.0, 1.0)", vector.setY(1).toString());
		assertEquals("(2.0, 2.0)", vector.set(2, 2).toString());
		assertEquals("(1.0, 1.0)", vector.set(new Vector2D(1, 1)).toString());
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError1() {
		new Vector2D().set(null);
	}
	
	@Test
	public void testAdd() {
		Vector2D vector = new Vector2D();
		assertEquals("(1.0, 1.0)", vector.add(1, 1).toString());
		assertEquals("(2.0, 2.0)", vector.add(new Vector2D(1, 1)).toString());
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError2() {
		new Vector2D().add(null);
	}
	
	@Test
	public void testSub() {
		Vector2D vector = new Vector2D();
		assertEquals("(-1.0, -1.0)", vector.sub(1, 1).toString());
		assertEquals("(-2.0, -2.0)", vector.sub(new Vector2D(1, 1)).toString());
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError3() {
		new Vector2D().sub(null);
	}
	
	@Test
	public void testScale() {
		Vector2D vector = new Vector2D(1, 1);
		assertEquals("(2.0, 1.0)", vector.scaleX(2).toString());
		assertEquals("(2.0, 2.0)", vector.scaleY(2).toString());
		assertEquals("(1.0, 1.0)", vector.scale(.5f, .5f).toString());
		assertEquals("(2.0, 2.0)", vector.scale(2).toString());
		assertEquals("(1.0, 1.0)", vector.scale(new Vector2D(.5f, .5f)).toString());
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError4() {
		new Vector2D().scale(null);
	}
	
	@Test
	public void testLength2() {
		assertEquals(1.0, new Vector2D(1, 0).length2(), 0.001);
		assertEquals(1.0, new Vector2D(0, 1).length2(), 0.001);
		assertEquals(2.0, new Vector2D(1, 1).length2(), 0.001);
		assertEquals(13.0, new Vector2D(3, 2).length2(), 0.001);
	}
	
	@Test
	public void testLength() {
		assertEquals(1.0, new Vector2D(1, 0).length(), 0.001);
		assertEquals(1.0, new Vector2D(0, 1).length(), 0.001);
		assertEquals(Math.sqrt(2), new Vector2D(1, 1).length(), 0.001);
		assertEquals(Math.sqrt(13), new Vector2D(3, 2).length(), 0.001);
	}
	
	@Test
	public void testNormalize() {
		assertEquals(1.0, new Vector2D(1, 1).normalize().length2(), 0.001);
		assertEquals(1.0, new Vector2D(3, 2).normalize().length2(), 0.001);
		assertEquals(1.0, new Vector2D(5, 4).normalize().length2(), 0.001);
		assertEquals(1.0, new Vector2D(7, 3).normalize().length2(), 0.001);
	}
	
	@Test
	public void testDot() {
		assertEquals(0.0, new Vector2D().dot(new Vector2D(5, 4)), 0.001);
		assertEquals(0.0, new Vector2D(4, 0).dot(new Vector2D(0, 4)), 0.001);
		assertEquals(0.0, new Vector2D(1, 1).dot(new Vector2D(-1, 1)), 0.001);
		assertEquals(23.0, new Vector2D(2, 3).dot(new Vector2D(4, 5)), 0.001);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError5() {
		new Vector2D().dot(null);
	}
}
