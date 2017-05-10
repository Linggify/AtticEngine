package com.github.linggify.attic.tests.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.linggify.attic.exceptions.AtticRuntimeException;
import com.github.linggify.attic.util.Matrix33;
import com.github.linggify.attic.util.Vector2D;

public class Matrix33Tests {

	@Test
	public void testConstructor() {
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, new Matrix33().getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 1, 1, 2, 2, 2, 3, 3, 3}, new Matrix33(new float[]{1, 1, 1, 2, 2, 2, 3, 3, 3}).getValues(), 0.001f);
		assertArrayEquals(new float[]{2, 2, 2, 1, 2, 1, 4, 3, 2}, new Matrix33(new float[]{2, 2, 2, 1, 2, 1, 4, 3, 2}).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testInvalidArgumentCountError0() {
		new Matrix33(new float[4]);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullArgumentError0() {
		new Matrix33(null);
	}
	
	@Test
	public void testSet() {
		Matrix33 matrix = new Matrix33();
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, matrix.set(new Matrix33()).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 1, 1, 2, 2, 2, 3, 3, 3}, matrix.set(new float[]{1, 1, 1, 2, 2, 2, 3, 3, 3}).getValues(), 0.001f);
		assertArrayEquals(new float[]{2, 2, 2, 1, 2, 1, 4, 3, 2}, matrix.set(new float[]{2, 2, 2, 1, 2, 1, 4, 3, 2}).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testInvalidArgumentCountError1() {
		new Matrix33().set(new float[4]);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullArgumentError1() {
		Matrix33 matrix = null;
		new Matrix33().set(matrix);
	}
	
	@Test
	public void testIdenty() {
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, new Matrix33().identy().getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, new Matrix33(new float[]{1, 1, 1, 2, 2, 2, 3, 3, 3}).identy().getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, new Matrix33(new float[]{2, 2, 2, 1, 2, 1, 4, 3, 2}).identy().getValues(), 0.001f);
	}
	
	@Test
	public void testAdd() {
		Matrix33 matrix = new Matrix33();
		Matrix33 matrix2 = new Matrix33(new float[]{-1, 1, 1, 1, -1, 1, 1, 1, -1});
		assertArrayEquals(new float[]{2, 0, 0, 0, 2, 0, 0, 0, 2}, matrix.add(new Matrix33()).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, matrix.add(matrix2).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullMatrixError0() {
		new Matrix33().add(null);
	}
	
	@Test
	public void testSub() {
		Matrix33 matrix = new Matrix33();
		Matrix33 matrix2 = new Matrix33(new float[]{-1, 1, 1, 1, -1, 1, 1, 1, -1});
		assertArrayEquals(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0}, matrix.sub(new Matrix33()).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, -1, -1, -1, 1, -1, -1, -1, 1}, matrix.sub(matrix2).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullMatrixError1() {
		new Matrix33().sub(null);
	}
	
	@Test
	public void testMultiply() {
		Matrix33 matrix0 = new Matrix33();
		Matrix33 matrix1 = new Matrix33(new float[]{1, 2, 2, 0, 3, 2, 1, 0, 1});
		Matrix33 matrix10 = new Matrix33(new float[]{1, 2, 2, 0, 3, 2, 1, 0, 1});
		Matrix33 matrix2 = new Matrix33(new float[]{2, 1, 0, 3, 2, 2, 3, 1, 0});
		Matrix33 matrix3 = new Matrix33(new float[]{1, 1, 3, 2, 1, 2, 0, 1, 2});
		
		assertArrayEquals(new float[]{1, 2, 2, 0, 3, 2, 1, 0, 1}, matrix1.multiply(matrix0).getValues(), 0.001f);
		assertArrayEquals(new float[]{2, 1, 0, 3, 2, 2, 3, 1, 0}, matrix2.multiply(matrix0).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 1, 3, 2, 1, 2, 0, 1, 2}, matrix3.multiply(matrix0).getValues(), 0.001f);
		assertArrayEquals(new float[]{14, 7, 4, 15, 8, 6, 5, 2, 0}, matrix1.multiply(matrix2).getValues(), 0.001f);
		assertArrayEquals(new float[]{5, 5, 11, 6, 5, 10, 1, 2, 5}, matrix10.multiply(matrix3).getValues(), 0.001f);
		assertArrayEquals(new float[]{4, 3, 8, 7, 7, 17, 5, 4, 11}, matrix2.multiply(matrix3).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullMatrixError2() {
		Matrix33 matrix = null;
		new Matrix33().multiply(matrix);
	}
	
	@Test
	public void testTransform() {
		Matrix33 matrix = new Matrix33();
		assertArrayEquals(new float[]{1, 0, 1, 0, 1, 0, 0, 0, 1}, matrix.transformX(1).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 1, 0, 1, 1, 0, 0, 1}, matrix.transformY(1).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 2, 0, 1, 0, 0, 0, 1}, matrix.transform(1, -1).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 3, 0, 1, 1, 0, 0, 1}, matrix.transform(new Vector2D(1, 1)).getValues(), 0.001f);
	}
	
	@Test (expected = AtticRuntimeException.class)
	public void testNullVectorError0() {
		new Matrix33().transform(null);
	}
	
	@Test
	public void testScale() {
		Matrix33 matrix = new Matrix33(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
		assertArrayEquals(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, matrix.scale(1).getValues(), 0.001f);
		assertArrayEquals(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2}, matrix.scale(2).getValues(), 0.001f);
		assertArrayEquals(new float[]{8, 8, 8, 8, 8, 8, 8, 8, 8}, matrix.scale(4).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, matrix.scale(0.125f).getValues(), 0.001f);
	}
	
	@Test
	public void testRotate() {
		Matrix33 matrix = new Matrix33(new float[]{1, 0, 1, 0, 1, 0, 0, 0, 1});
		assertArrayEquals(new float[]{0, 1, 0, -1, 0, -1, 0, 0, 1}, matrix.rotate(-90).getValues(), 0.001f);
		assertArrayEquals(new float[]{0, -1, 0, 1, 0, 1, 0, 0, 1}, matrix.rotate(180).getValues(), 0.001f);
		assertArrayEquals(new float[]{-1, 0, -1, 0, -1, 0, 0, 0, 1}, matrix.rotate(90).getValues(), 0.001f);
		assertArrayEquals(new float[]{1, 0, 1, 0, 1, 0, 0, 0, 1}, matrix.rotate(-180).getValues(), 0.001f);
	}
	
	@Test
	public void testDet() {
		assertEquals(1.0, new Matrix33().det(), 0.001f);
		assertEquals(-1.0, new Matrix33(new float[]{0, 1, 0, 1, 0, 0, 0, 0, 1}).det(), 0.001f);
		assertEquals(1.0, new Matrix33(new float[]{1, 2, 2, 0, 3, 2, 1, 0, 1}).det(), 0.001f);
		assertEquals(2.0, new Matrix33(new float[]{2, 1, 0, 3, 2, 2, 3, 1, 0}).det(), 0.001f);
		assertEquals(2.0, new Matrix33(new float[]{1, 1, 3, 2, 1, 2, 0, 1, 2}).det(), 0.001f);
	}
	
	@Test
	public void testInvert() {
		Matrix33 matrix = new Matrix33();
		Matrix33 matrix1 = new Matrix33(new float[]{1, 2, 2, 0, 3, 2, 1, 0, 1});
		Matrix33 matrix2 = new Matrix33(new float[]{2, 1, 0, 3, 2, 2, 3, 1, 0});
		Matrix33 matrix3 = new Matrix33(new float[]{1, 1, 3, 2, 1, 2, 0, 1, 2});
		assertArrayEquals(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1}, matrix.invert().getValues(), 0.001f);
		assertArrayEquals(new float[]{3, -2, -2, 2, -1, -2, -3, 2, 3}, matrix1.invert().getValues(), 0.001f);
		assertArrayEquals(new float[]{-1, 0, 1, 3, 0, -2, -1.5f, .5f, .5f}, matrix2.invert().getValues(), 0.001f);
		assertArrayEquals(new float[]{0, .5f, -.5f, -2, 1, 2, 1, -.5f, -.5f}, matrix3.invert().getValues(), 0.001f);
	}
}
