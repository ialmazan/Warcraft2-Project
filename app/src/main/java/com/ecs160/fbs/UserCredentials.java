// automatically generated by the FlatBuffers compiler, do not modify

package com.ecs160.fbs;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class UserCredentials extends Table {
  public static UserCredentials getRootAsUserCredentials(ByteBuffer _bb) { return getRootAsUserCredentials(_bb, new UserCredentials()); }
  public static UserCredentials getRootAsUserCredentials(ByteBuffer _bb, UserCredentials obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public UserCredentials __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String username() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer usernameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String password() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer passwordAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }

  public static int createUserCredentials(FlatBufferBuilder builder,
      int usernameOffset,
      int passwordOffset) {
    builder.startObject(2);
    UserCredentials.addPassword(builder, passwordOffset);
    UserCredentials.addUsername(builder, usernameOffset);
    return UserCredentials.endUserCredentials(builder);
  }

  public static void startUserCredentials(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addUsername(FlatBufferBuilder builder, int usernameOffset) { builder.addOffset(0, usernameOffset, 0); }
  public static void addPassword(FlatBufferBuilder builder, int passwordOffset) { builder.addOffset(1, passwordOffset, 0); }
  public static int endUserCredentials(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishUserCredentialsBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

