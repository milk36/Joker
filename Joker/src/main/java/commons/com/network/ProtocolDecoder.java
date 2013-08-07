package com.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.List;


/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {

	/**
	 * 协议解密
	 * 
	 * @param maxFrameLength
	 *            最长协议长度
	 * @param lengthFieldOffset
	 *            长度字段偏移量
	 * @param lengthFieldLength
	 *            长度字段长
	 * @param lengthAdjustment
	 *            补偿值添加到字段的长度值
	 * @param initialBytesToStrip
	 *            第一字节数，以去除从解码帧
	 */
	public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	// @Override
	// protected Object decode(ChannelHandlerContext arg0, Channel arg1,
	// ChannelBuffer arg2) throws Exception {
	// // 协议体 : |协议长度 |协议编号|协议内容|--|short|short|....|
	// Object obj = super.decode(arg0, arg1, arg2);
	// return obj;
	//
	// }
	@Override
	protected Object decode(ChannelHandlerContext arg0, ByteBuf arg1) throws Exception {
		System.out.println("decode");
		return super.decode(arg0, arg1);
	}

	@Override
	protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("decodeLast");
		super.decodeLast(ctx, in, out);
	}
}


