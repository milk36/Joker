package com.testdb;

public class MainTest {

	static void cloneTest(int time) {
		TestObj obj = new TestObj();
		for (int i = 0; i < time; i++) {
			obj.clone();
		}
	}

	static void newTest(int time) {
		TestObj obj = new TestObj();
		for (int i = 0; i < time; i++) {
			obj = new TestObj();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
		long start;
		long stop;
		int times = 1000000;

		System.gc();

		start = System.currentTimeMillis();
		newTest(times);
		stop = System.currentTimeMillis();
		System.out.println("newTest Time:" + (stop - start));

		System.gc();

		start = System.currentTimeMillis();
		cloneTest(times);
		stop = System.currentTimeMillis();
			System.out.println("cloneTest Time:" + (stop - start));
		}

	}

}

class TestObj implements Cloneable {
	private int i;
	private String aa;
	private String name = "sdfsf";
	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (Exception e) {
		}
		return obj;
	}
}