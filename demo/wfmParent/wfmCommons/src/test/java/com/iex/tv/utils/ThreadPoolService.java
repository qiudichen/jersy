package com.iex.tv.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolService implements IThreadPoolService {
	private int maxPool = 5;
	private Runnable[] workers;
	private MonitorThread monitor;
	
	public ThreadPoolService(int maxPool, Runnable[] workers) {
		this.maxPool = maxPool;
		this.workers = workers;
	}

	public void setWorker(Runnable[] workers) {
		this.workers = workers;
	}
	
	public void execute() {
		ExecutorService executor = Executors.newFixedThreadPool(maxPool);
		
		startMonitor((ThreadPoolExecutor)executor);
		
		for (Runnable worker : workers) {
			executor.execute(worker);
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		
		monitor.setRun(false);
	}
	

	private void startMonitor(ThreadPoolExecutor executor) {
		this.monitor = new MonitorThread(executor, 10);
		Thread monitorThread = new Thread(monitor);
        monitorThread.start();
	}
	
	class MonitorThread implements Runnable {
		private ThreadPoolExecutor executor;
		private int seconds;
		private boolean run = true;

		public MonitorThread(ThreadPoolExecutor executor, int seconds) {
			this.executor = executor;
			this.seconds = seconds;
		}
		
		
		public void setRun(boolean run) {
			this.run = run;
		}


		@Override
		public void run() {
			while (run) {
				System.out
						.println(String
								.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
										this.executor.getPoolSize(),
										this.executor.getCorePoolSize(),
										this.executor.getActiveCount(),
										this.executor.getCompletedTaskCount(),
										this.executor.getTaskCount(),
										this.executor.isShutdown(),
										this.executor.isTerminated()));
				try {
					Thread.sleep(seconds * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
