package org.napu.netty.timeserver.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

	public void bind(int port) throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();

			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// TODO: handle exception
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// TODO Auto-generated method stub
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
			arg0.pipeline().addLast(new StringDecoder());
			arg0.pipeline().addLast(new TimeServerHandler());

		}

	}

	public static void main(String[] args) throws Exception {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		new TimeServer().bind(port);
	}
}
