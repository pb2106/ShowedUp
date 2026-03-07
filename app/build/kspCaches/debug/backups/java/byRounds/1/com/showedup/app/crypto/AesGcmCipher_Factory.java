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
public final class AesGcmCipher_Factory implements Factory<AesGcmCipher> {
  @Override
  public AesGcmCipher get() {
    return newInstance();
  }

  public static AesGcmCipher_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AesGcmCipher newInstance() {
    return new AesGcmCipher();
  }

  private static final class InstanceHolder {
    private static final AesGcmCipher_Factory INSTANCE = new AesGcmCipher_Factory();
  }
}
