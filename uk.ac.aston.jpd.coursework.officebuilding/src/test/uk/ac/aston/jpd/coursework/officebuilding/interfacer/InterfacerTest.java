package uk.ac.aston.jpd.coursework.officebuilding.interfacer;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import org.junit.Test;

public class InterfacerTest {
	private Interfacer i;

	public InterfacerTest() {
		i = new Interfacer();
	}
	
	public void setScanner(Scanner s) {
		try {
			Field in;
			in = i.getClass().getDeclaredField("sc");
			in.setAccessible(true);
			in.set(i, s);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void readIntMethod() {
		try {
			Method readInt;
			readInt = i.getClass().getMethod("readInt");
			readInt.setAccessible(true);
			readInt.invoke(i);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
		}
	}
	
	public void readDoubleMethod() {
		try {
			Method readD;
			readD = i.getClass().getMethod("readDouble");
			readD.setAccessible(true);
			readD.invoke(i);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
		}
	}
	
	@Test
	public void validIntTest(){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
		
		setScanner(new Scanner("10"));
		readIntMethod();
		
		assertTrue(output.toString().isBlank());
	}
	
	@Test
	public void invalidIntTest() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
		
		setScanner(new Scanner("not an int"));
		readIntMethod();
		
		assertTrue(output.toString().contains("Value must be an integer! Try again: "));
	}
	
	@Test
	public void validDoubleTest() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
		
		setScanner(new Scanner("0.001"));
		readDoubleMethod();
		
		assertTrue(output.toString().isBlank());
	}
	
	@Test
	public void invalidDoubleTest() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
		
		setScanner(new Scanner("a"));
		readDoubleMethod();
		
		assertTrue(output.toString().contains("Value must be a double! Try again: "));
	}

}