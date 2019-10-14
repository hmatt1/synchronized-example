import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

class Scratch {

	public static void main(String[] args) throws Exception {
		RedisCache redisCache = new RedisCache();

		CompletableFuture completableFuture = redisCache.getAsync().thenAccept(System.out::println);
		CompletableFuture completableFuture1 = redisCache.getAsync().thenAccept(System.out::println);
		CompletableFuture completableFuture2 = redisCache.getAsync().thenAccept(System.out::println);

		completableFuture.join();
		completableFuture1.join();
		completableFuture2.join();
	}
}

class RedisCache {

	private String connection;
	private String asyncCacheCommands;
	private AtomicInteger index;

	public RedisCache() {
		connection = "blah";
		index = new AtomicInteger(0);
	}

	private String asyncCommands(String connection) {
		try {
			System.out.println(Thread.currentThread().getName() + ": Initializing commands");
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		index.getAndIncrement();
		return "commands " + index.toString();
	}

	private synchronized String getAsyncCacheCommands() {
		if (asyncCacheCommands == null) {
			asyncCacheCommands = asyncCommands(connection);
		}

		return asyncCacheCommands;
	}

	public CompletableFuture<String> getAsync() {
		return CompletableFuture.supplyAsync(this::getAsyncCacheCommands);
	}
}
