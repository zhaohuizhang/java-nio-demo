package org.napu.aio.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements Runnable, CompletionHandler<Void, AsyncTimeClientHandler> {

	private AsynchronousSocketChannel client;
	private String host;
	private int port;
	private CountDownLatch latch;

	public AsyncTimeClientHandler(String host, int port) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.port = port;
		try {
			client = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		latch = new CountDownLatch(1);
		client.connect(new InetSocketAddress(host, port), this, this);

		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}

		try {
			client.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void completed(Void result, AsyncTimeClientHandler attachment) {
		// TODO Auto-generated method stub
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				// TODO Auto-generated method stub
				if (attachment.hasRemaining()) {
					client.write(attachment, attachment, this);
				} else {
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);
					client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

						@Override
						public void completed(Integer result, ByteBuffer attachment) {
							// TODO Auto-generated method stub
							attachment.flip();
							byte[] bytes = new byte[attachment.remaining()];
							attachment.get(bytes);
							String body;
							try {
								body = new String(bytes, "UTF-8");
								System.out.println("Now is : " + body);
								latch.countDown();
							} catch (UnsupportedEncodingException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
							// TODO Auto-generated method stub
							try {
								client.close();
								latch.countDown();
							} catch (IOException e) {
								// TODO: handle exception
							}
						}

					});
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				// TODO Auto-generated method stub
				try {
					client.close();
					latch.countDown();
				} catch (IOException e) {
					// TODO: handle exception
				}
			}

		});
	}

	@Override
	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		// TODO Auto-generated method stub
		try {
			client.close();
			latch.countDown();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
