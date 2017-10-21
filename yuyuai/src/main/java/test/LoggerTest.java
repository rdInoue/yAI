package test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

public class LoggerTest {
	private static final Logger LOG = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		LOG.info("ヒープ使用量: " + usage.getUsed() + "/" + usage.getMax());

		String className = new Object(){}.getClass().getEnclosingClass().getName();
        System.out.println(className);

        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);

	}
}
