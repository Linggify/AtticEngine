package com.github.linggify.attic.tests.util;

import com.github.linggify.attic.util.Color;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColorTests {

	/**
	 * Tests {@link Color#Color()}
	 */
	@Test
	public void testDefaultConstructor() {
		Color color = new Color();
		assertEquals(0, color.red(), 0.001f);
		assertEquals(0, color.green(), 0.001f);
		assertEquals(0, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#Color(float, float, float, float)}
	 */
	@Test
	public void testParameterConstructor() {
		Color color = new Color(1, 1, 1, 1);
		assertEquals(1, color.red(), 0.001f);
		assertEquals(1, color.green(), 0.001f);
		assertEquals(1, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
		
		color = new Color(.5f, .5f, .5f, 1);
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#Color(Color)}
	 */
	@Test
	public void testColorConstructor() {
		Color color = new Color(new Color(1, 1, 1, 1));
		assertEquals(1, color.red(), 0.001f);
		assertEquals(1, color.green(), 0.001f);
		assertEquals(1, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
		
		color = new Color(new Color(.5f, .5f, .5f, 1));
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#set(float, float, float, float)}
	 */
	@Test
	public void testParameterSet() {
		Color color = new Color();
		color.set(1, 1, 1, 1);
		assertEquals(1, color.red(), 0.001f);
		assertEquals(1, color.green(), 0.001f);
		assertEquals(1, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
		
		color.set(.5f, .5f, .5f, 1);
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#set(Color)}
	 */
	@Test
	public void testColorSet() {
		Color color = new Color();
		color.set(new Color(1, 1, 1, 1));
		assertEquals(1, color.red(), 0.001f);
		assertEquals(1, color.green(), 0.001f);
		assertEquals(1, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
		
		color.set(new Color(.5f, .5f, .5f, 1));
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(1, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#tint(float, float, float, float)}
	 */
	@Test
	public void testParameterTint() {
		Color color = new Color(1, 1, 1, 1);
		color.tint(.5f, .5f, .5f, .5f);
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(.5f, color.alpha(), 0.001f);
		
		color.tint(.5f, .5f, .5f, 1);
		assertEquals(.25f, color.red(), 0.001f);
		assertEquals(.25f, color.green(), 0.001f);
		assertEquals(.25f, color.blue(), 0.001f);
		assertEquals(.5f, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#tint(Color)}
	 */
	@Test
	public void testColorTint() {
		Color color = new Color(1, 1, 1, 1);
		color.tint(new Color(.5f, .5f, .5f, .5f));
		assertEquals(.5f, color.red(), 0.001f);
		assertEquals(.5f, color.green(), 0.001f);
		assertEquals(.5f, color.blue(), 0.001f);
		assertEquals(.5f, color.alpha(), 0.001f);
		
		color.tint(new Color(.5f, .5f, .5f, 1));
		assertEquals(.25f, color.red(), 0.001f);
		assertEquals(.25f, color.green(), 0.001f);
		assertEquals(.25f, color.blue(), 0.001f);
		assertEquals(.5f, color.alpha(), 0.001f);
	}
	
	/**
	 * Tests {@link Color#asBytes()}
	 */
	@Test
	public void testAsBytes() {
		Color color = new Color(1, 1, 1, 1);
		assertArrayEquals(new byte[]{-1, -1, -1, -1}, color.asBytes());
		color = new Color(.5f, .5f, .5f, 0.25f);
		assertArrayEquals(new byte[]{127, 127, 127, 63}, color.asBytes());
	}
}
