package com.github.linggify.attic.tests.util;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeometryTests {

	@Test
	public void testVectorMatrixTransform() {
		Matrix33 matrix = new Matrix33();
		matrix.transform(1, 2);
		assertEquals(Vector2D.at(1, 2), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(3, 1), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(20, 13.5f), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.transform(-12, 7);
		assertEquals(Vector2D.at(-11, 9), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(-9, 8), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(8, 20.5f), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixScale() {
		Matrix33 matrix = new Matrix33();
		matrix.scale(2);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(4, -2), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(38, 23), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.scale(2);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(8, -4), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(76, 46), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixRotation() {
		Matrix33 matrix = new Matrix33();
		matrix.rotate(90);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(1, 2), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(-11.5f, 19), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.rotate(-180);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(-1, -2), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(11.5f, -19), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixTransformScale() {
		Matrix33 matrix = new Matrix33();
		matrix.scale(2);
		matrix.transform(2, 2);
		assertEquals(Vector2D.at(2, 2), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(6, 0), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(40, 25), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.scale(2);
		matrix.transform(0, 1);
		assertEquals(Vector2D.at(4, 5), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(12, 1), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(80, 51), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixTransformRotation() {
		Matrix33 matrix = new Matrix33();
		matrix.rotate(90);
		matrix.transform(1, 1);
		assertEquals(Vector2D.at(1, 1), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(2, 3), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(-10.5f, 20), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.rotate(-180);
		matrix.transform(1, 1);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(-1, -2), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(11.5f, -19), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixScaleRotation() {
		Matrix33 matrix = new Matrix33();
		matrix.rotate(90);
		matrix.scale(2);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(2, 4), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(-23, 38), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.rotate(-180);
		matrix.scale(2);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(-4, -8), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(46, -76), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test
	public void testVectorMatrixTransformScaleRotation() {
		Matrix33 matrix = new Matrix33();
		matrix.rotate(90);
		matrix.scale(2);
		matrix.transform(1, 1);
		assertEquals(Vector2D.at(1, 1), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(3, 5), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(-22, 39), matrix.multiply(Vector2D.at(19, 11.5f)));
		matrix.rotate(-180);
		matrix.scale(2);
		matrix.transform(2, 2);
		assertEquals(Vector2D.at(0, 0), matrix.multiply(Vector2D.at(0, 0)));
		assertEquals(Vector2D.at(-4, -8), matrix.multiply(Vector2D.at(2, -1)));
		assertEquals(Vector2D.at(46, -76), matrix.multiply(Vector2D.at(19, 11.5f)));
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError0() {
		Vector2D vector = null;
		new Matrix33().multiply(vector);
	}
}
