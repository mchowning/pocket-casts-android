// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: api.proto

// Protobuf Java Version: 3.25.1
package com.pocketcasts.service.api;

public interface BoolSettingOrBuilder extends
    // @@protoc_insertion_point(interface_extends:api.BoolSetting)
    com.google.protobuf.MessageLiteOrBuilder {

  /**
   * <code>.google.protobuf.BoolValue value = 1;</code>
   * @return Whether the value field is set.
   */
  boolean hasValue();
  /**
   * <code>.google.protobuf.BoolValue value = 1;</code>
   * @return The value.
   */
  com.google.protobuf.BoolValue getValue();

  /**
   * <code>.google.protobuf.BoolValue changed = 2;</code>
   * @return Whether the changed field is set.
   */
  boolean hasChanged();
  /**
   * <code>.google.protobuf.BoolValue changed = 2;</code>
   * @return The changed.
   */
  com.google.protobuf.BoolValue getChanged();

  /**
   * <code>.google.protobuf.Timestamp modified_at = 3;</code>
   * @return Whether the modifiedAt field is set.
   */
  boolean hasModifiedAt();
  /**
   * <code>.google.protobuf.Timestamp modified_at = 3;</code>
   * @return The modifiedAt.
   */
  com.google.protobuf.Timestamp getModifiedAt();
}
