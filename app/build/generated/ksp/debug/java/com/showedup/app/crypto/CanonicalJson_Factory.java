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
public final class CanonicalJson_Factory implements Factory<CanonicalJson> {
  @Override
  public CanonicalJson get() {
    return newInstance();
  }

  public static CanonicalJson_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CanonicalJson newInstance() {
    return new CanonicalJson();
  }

  private static final class InstanceHolder {
    private static final CanonicalJson_Factory INSTANCE = new CanonicalJson_Factory();
  }
}
