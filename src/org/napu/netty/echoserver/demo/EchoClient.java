package org.napu.netty.echoserver.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
	public void connect(int port, String host) throws Exception {
		// config client NIO thread pool
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel arg0) throws Exception {
							// TODO Auto-generated method stub
							ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
							arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							arg0.pipeline().addLast(new StringDecoder());
							arg0.pipeline().addLast(new EchoClientHandler());
						}

					});
			ChannelFuture f = b.connect(host, port).sync();

			f.channel().closeFuture().sync();

		} finally {
			// TODO: handle exception
			group.shutdownGracefully();
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
		new EchoClient().connect(port, "127.0.0.1");

	}

}
