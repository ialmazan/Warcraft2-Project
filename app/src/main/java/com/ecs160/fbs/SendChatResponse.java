// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class SendChatResponse extends Table {
  public static SendChatResponse getRootAsSendChatResponse(ByteBuffer _bb) { return getRootAsSendChatResponse(_bb, new SendChatResponse()); }
  public static SendChatResponse getRootAsSendChatResponse(ByteBuffer _bb, SendChatResponse obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public SendChatResponse __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String content() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer contentAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }

  public static int createSendChatResponse(FlatBufferBuilder builder,
      int contentOffset) {
    builder.startObject(1);
    SendChatResponse.addContent(builder, contentOffset);
    return SendChatResponse.endSendChatResponse(builder);
  }

  public static void startSendChatResponse(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addContent(FlatBufferBuilder builder, int contentOffset) { builder.addOffset(0, contentOffset, 0); }
  public static int endSendChatResponse(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishSendChatResponseBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

