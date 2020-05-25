
package io.cuillgln.toys.infrastructure.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PBufDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() > 0) {
			byte[] data = new byte[in.readableBytes()];
			in.readBytes(data);
			out.add(new PBuf(data));
		}
	}

}
