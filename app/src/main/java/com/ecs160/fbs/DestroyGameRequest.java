// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class DestroyGameRequest extends Table {
  public static DestroyGameRequest getRootAsDestroyGameRequest(ByteBuffer _bb) { return getRootAsDestroyGameRequest(_bb, new DestroyGameRequest()); }
  public static DestroyGameRequest getRootAsDestroyGameRequest(ByteBuffer _bb, DestroyGameRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public DestroyGameRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String username() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer usernameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String host() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer hostAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }

  public static int createDestroyGameRequest(FlatBufferBuilder builder,
      int usernameOffset,
      int hostOffset) {
    builder.startObject(2);
    DestroyGameRequest.addHost(builder, hostOffset);
    DestroyGameRequest.addUsername(builder, usernameOffset);
    return DestroyGameRequest.endDestroyGameRequest(builder);
  }

  public static void startDestroyGameRequest(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addUsername(FlatBufferBuilder builder, int usernameOffset) { builder.addOffset(0, usernameOffset, 0); }
  public static void addHost(FlatBufferBuilder builder, int hostOffset) { builder.addOffset(1, hostOffset, 0); }
  public static int endDestroyGameRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishDestroyGameRequestBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

