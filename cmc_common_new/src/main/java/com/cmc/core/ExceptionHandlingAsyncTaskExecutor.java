package com.cmc.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;



public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor {
	private Logger logger = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);
	private AsyncTaskExecutor executor;

	public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
		this.executor = executor;
	}

	// 用独立的线程来包装，@Async其本质就是如此
	public void execute(Runnable task) {
		executor.execute(createWrappedRunnable(task));
	}

	public void execute(Runnable task, long startTimeout) {
		// 用独立的线程来包装，@Async其本质就是如此
		executor.execute(createWrappedRunnable(task), startTimeout);
	}

	@SuppressWarnings("unchecked")
	public Future submit(Runnable task) {
		return executor.submit(createWrappedRunnable(task));
		// 用独立的线程来包装，@Async其本质就是如此。
	}

	@SuppressWarnings("unchecked")
	public Future submit(final Callable task) {
		// 用独立的线程来包装，@Async其本质就是如此。
		return executor.submit(createCallable(task));
	}

	private <T> Callable<?> createCallable(final Callable<?> task) {
		return new Callable<Object>() {
			@SuppressWarnings("unchecked")
			public T call() throws Exception {
				try {
					return  (T) task.call();
				} catch (Exception ex) {
					handle(ex);
					throw ex;
				}
			}
		};
	}

	private Runnable createWrappedRunnable(final Runnable task) {
		return new Runnable() {
			public void run() {
				try {
					task.run();
				} catch (Exception ex) {
					handle(ex);
				}
			}
		};
	}

	private void handle(Exception ex) {
		// 具体的异常逻辑处理的地方
		logger.debug("Error during @Async execution: " + ex);
		
	}
}
