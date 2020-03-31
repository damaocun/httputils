package com.ejlchina.http.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

import com.ejlchina.http.Download;
import com.ejlchina.http.HttpResult;
import com.ejlchina.http.HttpResult.State;
import com.ejlchina.http.HttpTask;
import com.ejlchina.http.OnCallback;
import com.ejlchina.http.TaskListener;

public class TaskExecutor {

	private Executor ioExecutor;
	private Executor mainExecutor;
	private TaskListener<Download> downloadListener;
	private TaskListener<HttpResult> responseListener;
	private TaskListener<IOException> exceptionListener;
	private TaskListener<State> completeListener;
	
	public TaskExecutor(Executor ioExecutor, Executor mainExecutor, TaskListener<Download> downloadListener, 
			TaskListener<HttpResult> responseListener, TaskListener<IOException> exceptionListener, 
			TaskListener<State> completeListener) {
		this.ioExecutor = ioExecutor;
		this.mainExecutor = mainExecutor;
		this.downloadListener = downloadListener;
		this.responseListener = responseListener;
		this.exceptionListener = exceptionListener;
		this.completeListener = completeListener;
	}

	public Executor getExecutor(boolean onIoThread) {
		if (onIoThread || mainExecutor == null) {
			return ioExecutor;
		}
		return mainExecutor;
	}

	public Download download(HttpTask<?> httpTask, File file, InputStream input, long skipBytes) {
		Download download = new Download(file, input, this, skipBytes);
		if (downloadListener != null) {
			downloadListener.listen(httpTask, download);
		}
		return download;
	}
	
	public void execute(Runnable command, boolean onIoThread) {
		Executor executor = ioExecutor;
		if (mainExecutor != null && !onIoThread) {
			executor = mainExecutor;
		}
		executor.execute(command);
	}
	
	public void executeOnResponse(HttpTask<?> task, OnCallback<HttpResult> onResponse, HttpResult result, boolean onIoThread) {
		if (responseListener != null) {
			execute(() -> {
				if (responseListener.listen(task, result) && onResponse != null) {
					onResponse.on(result);
				}
			}, onIoThread);
		} else if (onResponse != null) {
			execute(() -> { onResponse.on(result); }, onIoThread);
		}
	}

	public boolean executeOnException(HttpTask<?> task, OnCallback<IOException> onException, IOException error, boolean onIoThread) {
		if (exceptionListener != null) {
			execute(() -> {
				if (exceptionListener.listen(task, error) && onException != null) {
					onException.on(error);
				}
			}, onIoThread);
			return true;
		} else if (onException != null) {
			execute(() -> { onException.on(error); }, onIoThread);
			return true;
		}
		return false;
	}
	
	public void executeOnComplete(HttpTask<?> task, OnCallback<State> onComplete, State state, boolean onIoThread) {
		if (completeListener != null) {
			execute(() -> {
				if (completeListener.listen(task, state) && onComplete != null) {
					onComplete.on(state);
				}
			}, onIoThread);
		} else if (onComplete != null) {
			execute(() -> { onComplete.on(state); }, onIoThread);
		}
	}

}
