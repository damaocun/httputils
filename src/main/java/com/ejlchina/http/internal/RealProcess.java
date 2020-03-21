package com.ejlchina.http.internal;

import com.ejlchina.http.Process;

public class RealProcess implements Process {

	// 总字节数
	private long totalBytes;
	// 已经完成字节数
	private long doneBytes;
	
	
	public RealProcess(long totalBytes, long doneBytes) {
		this.totalBytes = totalBytes;
		this.doneBytes = doneBytes;
	}
	
	@Override
	public double getRate() {
		return (double) doneBytes / totalBytes;
	}

	@Override
	public long getTotalBytes() {
		return totalBytes;
	}

	@Override
	public long getDoneBytes() {
		return doneBytes;
	}
	
	@Override
	public boolean isDone() {
		return doneBytes >= totalBytes;
	}
	
	public void addDoneBytes(long delt) {
		this.doneBytes += delt;
	}
	
}
