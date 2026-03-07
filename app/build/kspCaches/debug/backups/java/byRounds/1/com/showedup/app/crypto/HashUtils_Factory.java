package com.showedup.app.crypto;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class HashUtils_Factory implements Factory<HashUtils> {
  @Override
  public HashUtils get() {
    return newInstance();
  }

  public static HashUtils_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HashUtils newInstance() {
    return new HashUtils();
  }

  private static final class InstanceHolder {
    private static final HashUtils_Factory INSTANCE = new HashUtils_Factory();
  }
}
