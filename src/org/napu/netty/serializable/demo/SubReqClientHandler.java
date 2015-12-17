package org.napu.netty.serializable.demo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {

	public SubReqClientHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeReq subReq(int i) {
		// TODO Auto-generated method stub
		SubscribeReq req = new SubscribeReq();
		req.setAddress("西藏北路18号西行天地3楼");
		req.setPhoneNumber("136XXXXXXX");
		req.setProductName("Netty");
		req.setSubReqID(i);
		req.setUserName("napu.zhang");
		return req;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive server response : [" + msg.toString() + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		e.printStackTrace();
		ctx.close();
	}
}
