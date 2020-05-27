
package io.cuillgln.toys.infrastructure.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class PBufEncoder extends MessageToByteEncoder<PBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, PBuf msg, ByteBuf out) throws Exception {
		// TODO crc -> escape
		out.writeBytes(msg.toByteArray());
	}

}
