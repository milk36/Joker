package com.network;


import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

/**
 * 
 * @author lyh
 * @date 2012-2-29
 */
public class ProtocolEncoder extends LengthFieldPrepender {
	Logger log = Logger.getLogger(ProtocolEncoder.class);

	/**
	 * 协议加密
	 * 
	 * @param lengthFieldLength
	 *            长度字段长
	 * @param lengthIncludesLengthFieldLength
	 *            是否包含长度字段
	 */
	public ProtocolEncoder(int lengthFieldLength, boolean lengthIncludesLengthFieldLength) {
		super(lengthFieldLength, lengthIncludesLengthFieldLength);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) msg;
		int len = buffer.writerIndex() - 2;
		log.info("send length:" + len);
		buffer.setShort(0, len);
		return buffer;
		
	}
}


