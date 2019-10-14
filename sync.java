import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

class Scratch {

	public static void main(String[] args) throws Exception {
		RedisCache redisCache = new RedisCache();

		Runnable r = () -> {
			System.out.println(redisCache.getCommands());
		};

		new Thread(r).start();
		sleep(100);
		new Thread(r).start();
		sleep(100);
		new Thread(r).start();
	}
}

class RedisCache {

	private String connection;
	private String commands;
	private AtomicInteger index;

	public RedisCache() {
		connection = "blah";
		index = new AtomicInteger(0);
	}

	public synchronized String getCommands() {
		if (commands == null) {
			// syncCommands internally runs `command` command on Redis
			String newCommands = syncCommands(connection);
			commands = setCommands(newCommands);
		}

		return commands;
	}

	private String setCommands(String newCommands) {
		if (commands == null) {
			commands = newCommands;
		}
		return commands;
	}

	private String syncCommands(String connection) {
		try {
			System.out.println(Thread.currentThread().getName() + ": Initializing commands");
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		index.getAndIncrement();
		return "commands " + index.toString();
	}
}
